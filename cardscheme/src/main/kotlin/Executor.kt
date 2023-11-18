class Executor() : ExpressionVisitor<SchemeValue>, StatementVisitor<Unit> {

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

    override fun visited_by(node: IntNode): IntValue {
        return IntValue(node.value)
    }

    override fun visited_by(node: IdentifierNode): SchemeValue {
        // FIXME: Crash with better error
        val res = environment.get(node.identifier)
        if (res == null) {
            throw SchemeError("Variable not found", "No variable with the name ${node.identifier} exists.", node.location, null)
        }
        return res
    }

    override fun visited_by(node: ListNode) : ListValue {
        return ListValue(node.expressions.map { e -> e.visit(this) })
    }

    override fun visited_by(node: DefineNode) {
        val values = node.bodies.map { b -> b.visit(this) }

        for ((name, value) in node.names.zip(values)){
            environment.getGlobal().put(name.identifier, value)
        }
    }

    override fun visited_by(node: ApplicationNode): SchemeValue {
        // FIXME: Replace by proper function loading from the environment
        val func = node.expressions.first().visit(this)
        val args = node.expressions.drop(1).map { e ->
            e.visit(this)
        }

        if (func !is NativeFunc) {
            throw Exception("Functions must be callable!")
        }

        val nativeFunc = func as NativeFunc
        return nativeFunc.func(args, environment)
    }
}