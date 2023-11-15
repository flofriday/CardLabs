class Ast(val forms: List<AstNode>) {
    fun dump(): String {
        var out = "AST\n"
        for (child in forms) {
            out += child.dump(1)
        }
        return out
    }
}

abstract class AstNode(val location: Location) {
    abstract fun dump(indent: Int): String

    protected fun getIntentdation(indent: Int): String {
        return " ".repeat(indent * 2)
    }
}


abstract class ExpressionNode(location: Location) : AstNode(location) {

}

class ApplicationNode(val expressions: List<ExpressionNode>, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIntentdation(indent) + "Application\n"
        for (child in expressions) {
            out += child.dump(indent + 1)
        }
        return out
    }
}

class IdentifierNode(val identifier: String, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIntentdation(indent) + "Identifier: '$identifier'\n"
    }
}

class IntNode(val value: Int, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIntentdation(indent) + "Int: $value \n"
    }
}