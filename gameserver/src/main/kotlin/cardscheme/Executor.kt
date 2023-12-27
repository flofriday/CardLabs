package cardscheme

data class TailCallInfo(val func: FuncValue, val args: List<FuncArg>)

class Executor(var environment: Environment, val outputBuffer: StringBuilder) :
    ExpressionVisitor<SchemeValue>,
    StatementVisitor<Unit> {
    private var tailCallInfo: TailCallInfo? = null

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

    override fun visitedBy(node: SymbolNode): SchemeValue {
        return SymbolValue(node.name)
    }

    override fun visitedBy(node: DefineNode) {
        val value = node.body.visit(this)
        environment.put(node.name.identifier, value)
    }

    override fun visitedBy(node: LambdaNode): FuncValue {
        return FuncValue(
            node.params.map { a -> a.identifier },
            Arity(if (node.isVarArg) node.params.size - 1 else node.params.size, node.params.size, node.isVarArg),
            node.body,
            environment,
        )
    }

    /**
     * Evaluate a let expression.
     *
     * Spec: R7RS, chapter 4.2.2
     * The init are evaluated in the current environment
     * (in some unspecified order), the variable are
     * bound to fresh locations holding the results, the body is
     * evaluated in the extended environment, and the values of
     * the last expression of body are returned. Each binding
     * of a variable has body as its region.
     */
    private fun evalLet(node: LetNode): SchemeValue {
        val values = node.bindings.map { b -> b.init.visit(this) }

        pushEnv()
        node.bindings.map { b -> b.name.identifier }.zip(values).forEach { (n, v) -> environment.put(n, v) }
        val result = node.body.visit(this)
        popEnv()

        return result
    }

    /**
     * Evaluate a let* expression.
     *
     * Spec: R7RS, chapter 4.2.4
     * The let* binding construct is similar to let,
     * but the bindings are performed sequentially from left to
     * right, and the region of a binding indicated by (variable
     * init) is that part of the let* expression to the right of
     * the binding. Thus the second binding is done in an
     * environment in which the first binding is visible, and so on.
     * The variables need not be distinct.
     */
    private fun evalLetStar(node: LetNode): SchemeValue {
        for (binding in node.bindings) {
            pushEnv()
            val value = binding.init.visit(this)
            environment.put(binding.name.identifier, value)
        }

        val result = node.body.visit(this)
        node.bindings.forEach { _ -> popEnv() }

        return result
    }

    /**
     * Evaluate a letrec expression.
     *
     * Spec: R7RS, chapter 4.2.4
     * The variables are bound to fresh locations
     * holding unspecified values, the inits are evaluated in the
     * resulting environment (in some unspecified order), each
     * variable is assigned to the result of the corresponding
     * init, the body is evaluated in the resulting environment,
     * and the values of the last expression in body are returned.
     * Each binding of a variable has the entire letrec expression
     * as its region, making it possible to define mutually
     * recursive procedures.
     */
    private fun evalLetRec(node: LetNode): SchemeValue {
        pushEnv()
        node.bindings.forEach { b -> environment.put(b.name.identifier, VoidValue()) }
        val values = node.bindings.map { b -> b.init.visit(this) }
        node.bindings.map { b -> b.name.identifier }.zip(values).forEach { (n, v) -> environment.update(n, v) }
        val result = node.body.visit(this)
        popEnv()

        return result
    }

    /**
     * Evaluate a letrec* expression.
     *
     * Spec: R7RS, chapter 4.2.4
     * The variables are bound to fresh locations,
     * each variable is assigned in left-to-right
     * order to the result of evaluating the
     * corresponding * init, the body is evaluated in
     * the resulting environment, and the values of
     * the last expression in body are returned.
     * Despite the left to right evaluation and assignment order, each binding of a
     * variable has the entire letrec* expression as its region,
     * making it possible to define mutually recursive procedures.
     */
    private fun evalLetRecStar(node: LetNode): SchemeValue {
        pushEnv()
        node.bindings.forEach { b -> environment.put(b.name.identifier, VoidValue()) }
        for (binding in node.bindings) {
            val value = binding.init.visit(this)
            environment.update(binding.name.identifier, value)
        }
        val result = node.body.visit(this)
        popEnv()

        return result
    }

    override fun visitedBy(node: LetNode): SchemeValue {
        if (!node.star && !node.rec) {
            return evalLet(node)
        } else if (node.star && !node.rec) {
            return evalLetStar(node)
        } else if (!node.star && node.rec) {
            return evalLetRec(node)
        } else if (node.star && node.rec) {
            return evalLetRecStar(node)
        }
        throw Exception("Invalid combination")
    }

    /**
     * Evaluating a set! expression.
     *
     * Spec: R7RS, chapter 4.1.6
     * Expression is evaluated, and the resulting
     * value is stored in the location to which variable is bound.
     */
    override fun visitedBy(node: SetNode): SchemeValue {
        val value = node.expression.visit(this)
        try {
            environment.update(node.name.identifier, value)
        } catch (e: SchemeError) {
            throw SchemeError(e.header, e.reason, node.name.location, e.tip)
        }
        return VoidValue()
    }

    /**
     * Evaluate an if expression.
     *
     * Spec: R7RS, chapter 4.1.5
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
     * Spec: R7RS, chapter 4.2.4
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

    private fun insertArguments(
        func: FuncValue,
        args: List<FuncArg>,
    ) {
        // If not a var-args function, just insert them all
        if (!func.arity.isVarArg) {
            func.params.zip(args).map { (name, arg) -> environment.put(name, arg.value) }
            return
        }

        // For var-args first insert all 1-1 mappings
        func.params.dropLast(1).zip(args).map { (name, arg) -> environment.put(name, arg.value) }

        // Then bundle all other into a list and bind them to the last variable
        val name = func.params.last()
        val list = ListValue(args.drop(func.params.size - 1).map { a -> a.value })
        environment.put(name, list)
    }

    /**
     * Call a function in the current environment.
     */
    fun callFunction(
        func: CallableValue,
        args: List<FuncArg>,
    ): SchemeValue {
        if (!func.arity.inside(args.size)) {
            val message =
                if (func.arity.min == func.arity.max) {
                    "The function expects ${func.arity.min} arguments, but you provided ${args.size}"
                } else {
                    "The function expects between ${func.arity.min} and ${func.arity.max} arguments, " +
                        "but you provided ${args.size}"
                }
            throw SchemeError("Invalid number of arguments", message, null, null)
        }

        if (func is NativeFuncValue) {
            return func.func(args, this)
        } else if (func is FuncValue) {
            val old = environment
            // environment for the lambda function
            environment = func.env
            // environment for the CONTENT of the lambda function
            pushEnv()
            insertArguments(func, args)
            var result = func.body.visit(this)

            // Tail call optimization
            // To unwind the call stack, visit doesn't evaluate them so we need to do this now here.
            var currentFunc = func
            while (tailCallInfo != null) {
                val tfunc = tailCallInfo!!.func
                val targs = tailCallInfo!!.args
                tailCallInfo = null

                if (tfunc !== currentFunc) {
                    // Clear the environment if the functions aren't equal, however if they are
                    // than we can keep as the syntax of scheme doesn't allow that variables are
                    // leaked between iteration.
                    popEnv()
                    pushEnv()
                }

                currentFunc = tfunc
                insertArguments(tfunc, targs)
                result = tfunc.body.visit(this)
            }

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

        val args = node.expressions.drop(1).map { e -> FuncArg(e.visit(this), e.location) }

        // Tail call optimization
        if (node.isLast && func is FuncValue) {
            this.tailCallInfo = TailCallInfo(func, args)
            return VoidValue()
        }

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

    fun printToBuffer(output: Any) {
        outputBuffer.append(output)
    }
}
