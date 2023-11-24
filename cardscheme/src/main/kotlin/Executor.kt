import java.util.*

class Executor : ExpressionVisitor<SchemeValue>, StatementVisitor<Unit> {
    private var environment = Environment(null, hashMapOf())

    /**
     * This method must not be called more than once.
     * On error the internal environment may be messed up and repeated calls will result in a faulty execution.
     */
    fun execute(
        ast: Ast,
        env: Environment,
    ): SchemeValue? {
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

    override fun visitedBy(node: BoolNode): SchemeValue {
        return BooleanValue(node.value)
    }

    override fun visitedBy(node: IntNode): IntegerValue {
        return IntegerValue(node.value)
    }

    override fun visitedBy(node: FloatNode): SchemeValue {
        return FloatValue(node.value)
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

    override fun visitedBy(node: ListNode): ListValue {
        return ListValue(LinkedList(node.expressions.map { e -> e.visit(this) }))
    }

    override fun visitedBy(node: DefineNode) {
        val value = node.body.visit(this)
        environment.put(node.name.identifier, value)
    }

    override fun visitedBy(node: LambdaNode): FuncValue {
        return FuncValue(node.params.map { a -> a.identifier }, Arity(node.params.size, node.params.size), node.body, environment)
    }

    /**
     * Evaluating an if expression.
     *
     * Spec: R7R, chapter 4.1.5
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

    override fun visitedBy(node: BodyNode): SchemeValue {
        for (d in node.definitions) {
            d.visit(this)
        }
        return node.expressions.map { e -> e.visit(this) }.last()
    }

    override fun visitedBy(node: ApplicationNode): SchemeValue {
        // FIXME: Replace by proper function loading from the environment
        val func = node.expressions.first().visit(this)

        if (func is NativeFuncValue) {
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

            val args =
                node.expressions.drop(1).map { e ->
                    NativeFuncArg(e.visit(this), e.location)
                }
            try {
                return func.func(args, environment)
            } catch (e: SchemeError) {
                throw SchemeError(e.header, e.reason, e.location ?: node.location, e.tip)
            }
        } else if (func is FuncValue) {
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

            val args =
                node.expressions.drop(1).map { e ->
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
            throw SchemeError(
                "Type Mismatch",
                "The first argument of a function call must be a function," +
                    " but it was a ${func.typeName()}",
                node.expressions.first().location,
                null,
            )
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
