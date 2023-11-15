class Executor : AstVisitor<SchemeValue>() {
    fun execute(ast: Ast, env: Environment): SchemeValue {
        // FIXME: REplace by empty list
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
        TODO("Not yet implemented")
    }

    override fun visited_by(node: ApplicationNode): SchemeValue {
        // FIXME: Replace by proper function loading from the environment
        val funcExpr = node.expressions.first()

        val args = node.expressions.drop(1).map { e ->
            e.visit(this)
        }

        if (funcExpr !is IdentifierNode)
            throw Exception("Function must be an identifier for now")

        val funcName = (funcExpr as IdentifierNode).identifier
        if (funcName == "+") {
            // FIXME: define value addition better
            var sum = 0
            for (arg in args) {
                if (arg !is IntValue) throw Exception("Only Ints can be added for now")
                sum += (arg as IntValue).value
            }
            return IntValue(sum)
        } else if (funcName == "-") {
            // FIXME: define value addition better
            if (args.first() !is IntValue) throw Exception("Only Ints can be subtracted for now")
            var sum = (args.first() as IntValue).value

            for (arg in args.drop(1)) {
                if (arg !is IntValue) throw Exception("Only Ints can be subtracted for now")
                sum -= (arg as IntValue).value
            }
            return IntValue(sum)
        } else {
            throw Exception("Unkonwn functionname: '$funcName'")
        }
    }
}