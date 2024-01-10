package cardscheme

class Executor(var environment: Environment, val buffer: StringBuffer) :
    ExpressionVisitor<SchemeValue>,
    StatementVisitor<Unit> {
    /**
     * On error the internal environment may be messed up and repeated calls will result in a faulty execution.
     */
    fun execute(ast: Ast): SchemeValue? {
        if (ast.forms.isEmpty()) return null

        for (form in ast.forms.dropLast(1)) {
            when (form) {
                is ExpressionNode -> form.visit(this)
                is StatementNode -> form.visit(this)
                else -> throw Exception("Forms in AST must either be ExpressionNodes or StatementNodes")
            }
        }

        return when (val last = ast.forms.last()) {
            is ExpressionNode -> (last).visit(this)
            is StatementNode -> {
                last.visit(this)
                null
            }

            else -> throw Exception("Forms in AST must either be ExpressionNodes or StatementNodes")
        }
    }

    /**
     * On error the internal environment may be messed up and repeated calls will result in a faulty execution.
     */
    fun execute(
        func: CallableValue,
        args: List<SchemeValue>,
    ): SchemeValue {
        return this.callFunction(func, args.map { v -> FuncArg(v, null) })
    }

    override fun visitedBy(node: BoolNode): SchemeValue {
        return BooleanValue(node.value)
    }

    override fun visitedBy(node: IntNode): IntegerValue {
        return IntegerValue(node.value)
    }

    override fun visitedBy(node: FloatNode): SchemeValue {
        return FloatValue(node.value)
    }

    override fun visitedBy(node: StringNode): SchemeValue {
        return StringValue(node.value)
    }

    override fun visitedBy(node: CharNode): SchemeValue {
        return CharacterValue(node.value)
    }

    override fun visitedBy(node: IdentifierNode): SchemeValue {
        val res = environment.get(node.identifier)
        if (res == null) {
            throw SchemeError(
                "Variable not found",
                "No variable with the name ${node.identifier} exists.",
                node.location,
                null,
            )
        }
        return res
    }

    override fun visitedBy(node: DefineNode) {
        val value = node.body.visit(this)
        environment.put(node.name.identifier, value)
    }

    override fun visitedBy(node: LambdaNode): FuncValue {
        return FuncValue(node.params.map { a -> a.identifier }, Arity(node.params.size, node.params.size), node.body, environment)
    }

    /**
     * Evaluate an if expression.
     *
     * Spec: R7R, chapter 4.1.5
     * Semantic: An if expression is evaluated as follows: first, ⟨test⟩ is evaluated. If it yields a true value
     * (see section 6.3), then ⟨consequent⟩ is evaluated and its values are returned. Otherwise, ⟨alternate⟩ is
     * evaluated and its values are returned. If ⟨test⟩ yields a false value and no ⟨alternate⟩ is specified,
     * then the result of the expression is unspecified.
     */
    override fun visitedBy(node: IfNode): SchemeValue {
        val condition = node.condition.visit(this)

        if (condition.isTruthy()) {
            return node.thenExpression.visit(this)
        }

        if (node.elseExpression != null) {
            return node.elseExpression.visit(this)
        }

        return VoidValue()
    }

    /**
     * Evaluate a do expression.
     *
     * Spec: R7R, chapter 4.2.4
     * Semantic: The ⟨init⟩ expressions are evaluated (in some unspecified order), the ⟨variable⟩s are bound to fresh
     * locations, the results of the ⟨init⟩ expressions are stored in the bindings of the ⟨variable⟩s, and then the
     * iteration phase begins.
     * Each iteration begins by evaluating ⟨test⟩; if the result is false (see section 6.3), then the ⟨command⟩
     * expressions are evaluated in order for effect, the ⟨step⟩ expressions are evaluated in some unspecified order,
     * the ⟨variable⟩s are bound to fresh locations, the results of the ⟨step⟩s are stored in the bindings of the
     * ⟨variable⟩s, and the next iteration begins.
     * If ⟨test⟩ evaluates to a true value, then the ⟨expression⟩s are evaluated from left to right and the values of
     * the last ⟨expression⟩ are returned. If no ⟨expression⟩s are present, then the value of the do expression is
     * unspecified.
     *
     */
    override fun visitedBy(node: DoNode): SchemeValue {
        val initValues = node.variableInitSteps.map { t -> t.init.visit(this) }
        pushEnv()
        node.variableInitSteps
            .map { t -> t.name }
            .zip(initValues)
            .map { (t, v) -> environment.put(t.identifier, v) }

        while (!node.test.visit(this).isTruthy()) {
            if (node.command != null) {
                node.command.visit(this)
            }

            val newValues = node.variableInitSteps.map { t -> t.step?.visit(this) }
            node.variableInitSteps
                .map { t -> t.name }
                .zip(newValues)
                .map { (t, v) -> if (v != null) environment.put(t.identifier, v) }
        }

        val result = node.body?.visit(this) ?: VoidValue()
        popEnv()
        return result
    }

    override fun visitedBy(node: BodyNode): SchemeValue {
        for (d in node.definitions) {
            d.visit(this)
        }
        return node.expressions.map { e -> e.visit(this) }.last()
    }

    /**
     * Call a function in the current environment.
     *
     * The caller must ensure that the number of arguments match the functions arity.
     */
    fun callFunction(
        func: CallableValue,
        args: List<FuncArg>,
    ): SchemeValue {
        if (func is NativeFuncValue) {
            return func.func(args, this)
        } else if (func is FuncValue) {
            val old = environment
            // environment for the lambda function
            environment = func.env
            // environment for the CONTENT of the lambda function
            pushEnv()
            func.params.zip(args).map { (name, arg) -> environment.put(name, arg.value) }
            val result = func.body.visit(this)
            popEnv()
            environment = old
            return result
        }
        throw Exception("Should never happen")
    }

    override fun visitedBy(node: ApplicationNode): SchemeValue {
        val func = node.expressions.first().visit(this)

        if (func !is CallableValue) {
            val infixTip = "Maybe you wrote the C-like infix notation instead of the Lisp-like prefix notation?"
            throw SchemeError(
                "Type Mismatch",
                "The first argument of a function call must be a function," +
                    " but it was a ${func.typeName()}.",
                node.expressions.first().location,
                if (node.expressions.size >= 3 && func is NumberValue) infixTip else null,
            )
        }

        if (!func.arity.inside(node.expressions.size -1)) {
            val message =
                if (func.arity.min == func.arity.max) {
                    "The function expects ${func.arity.min} arguments, but you provided ${node.expressions.size - 1}"
                } else {
                    "The function expects between ${func.arity.min} and ${func.arity.max} arguments, " +
                        "but you provided ${node.expressions.size - 1}"
                }
            throw SchemeError("Invalid number of arguments", message, node.location, null)
        }

        val args = node.expressions.drop(1).map { e -> FuncArg(e.visit(this), e.location) }
        try {
            return callFunction(func, args)
        } catch (e: SchemeError) {
            throw SchemeError(e.header, e.reason, e.location ?: node.location, e.tip)
        }
    }

    private fun pushEnv() {
        val newEnv = Environment(environment, hashMapOf())
        environment = newEnv
    }

    private fun popEnv(): Environment {
        val tmp = environment
        environment = environment.enclosing!!
        return tmp
    }
}
