class Executor : ExpressionVisitor<SchemeValue>, StatementVisitor<Unit> {

    private var environment = Environment(null, hashMapOf())

    fun execute(ast: Ast, env: Environment): SchemeValue? {
        // FIXME: Replace by empty list
        this.environment = env
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

    override fun visited_by(node: BoolNode): SchemeValue {
        return BooleanValue(node.value)
    }

    override fun visited_by(node: IntNode): IntegerValue {
        return IntegerValue(node.value)
    }

    override fun visited_by(node: FloatNode): SchemeValue {
        return FloatValue(node.value)
    }

    override fun visited_by(node: IdentifierNode): SchemeValue {
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

    override fun visited_by(node: ListNode): ListValue {
        return ListValue(node.expressions.map { e -> e.visit(this) })
    }

    override fun visited_by(node: DefineNode) {
        val values = node.bodies.map { b -> b.visit(this) }

        for ((name, value) in node.names.zip(values)) {
            environment.getGlobal().put(name.identifier, value)
        }
    }

    override fun visited_by(node: LambdaNode): FuncValue {
        return FuncValue(node.args.map { a -> a.identifier }, node.body, environment)
    }

    override fun visited_by(node: IfNode): SchemeValue {
        val condition = node.condition.visit(this)
        if (condition !is BooleanValue) {
            throw SchemeError(
                "Expected boolean",
                "Expected Condition inside if to evaluate to a boolean value",
                node.location,
                null,
            )
        }

        if (condition.value) {
            return node.thenExpression.visit(this)
        }

        if (node.elseExpression != null) {
            return node.elseExpression.visit(this)
        }

        return VoidValue()
    }

    override fun visited_by(node: BodyNode): SchemeValue {
        for (d in node.definitions) {
            d.visit(this)
        }
        return node.expressions.map { e -> e.visit(this) }.last()
    }

    override fun visited_by(node: ApplicationNode): SchemeValue {
        // FIXME: Replace by proper function loading from the environment
        val func = node.expressions.first().visit(this)

        if (func is NativeFuncValue) {
            val args = node.expressions.drop(1).map { e ->
                NativeFuncArg(e.visit(this), e.location)
            }
            try {
                return func.func(args, environment)
            } catch (e: SchemeError) {
                throw SchemeError(e.header, e.reason, e.location ?: node.location, e.tip)
            }
        } else if (func is FuncValue) {
            val args = node.expressions.drop(1).map { e ->
                e.visit(this)
            }

            val old = environment
            // environment for the lambda function
            environment = func.env
            // environment for the CONTENT of the lambda function
            pushEnv()
            func.args.zip(args).map { (name, value) -> environment.put(name, value) }
            val result = func.body.visit(this)
            popEnv()
            environment = old
            return result
        } else {
            throw Exception("Functions must be callable!")
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
