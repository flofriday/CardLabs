class Executor() : AstVisitor<SchemeValue>() {

    private var environment = Environment(null, hashMapOf())

    fun execute(ast: Ast, env: Environment): SchemeValue {
        // FIXME: Replace by empty list
        this.environment = env
        var result: SchemeValue = IntValue(-1)
        for (form in ast.forms) {
            result = form.visit(this)
        }
        return result
    }

    override fun visited_by(node: IntNode): SchemeValue {
        return IntValue(node.value)
    }

    override fun visited_by(node: IdentifierNode): SchemeValue {
        // FIXME: Crash with better error
        return environment.get(node.identifier)!!
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