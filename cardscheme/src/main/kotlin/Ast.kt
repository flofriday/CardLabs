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

    protected fun getIndentation(indent: Int): String {
        return " ".repeat(indent * 2)
    }

    abstract fun <T> visit(visitor: AstVisitor<T>): T
}


abstract class ExpressionNode(location: Location) : AstNode(location) {

}

class ApplicationNode(val expressions: List<ExpressionNode>, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "Application\n"
        for (child in expressions) {
            out += child.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: AstVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class IdentifierNode(val identifier: String, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Identifier: '$identifier'\n"
    }

    override fun <T> visit(visitor: AstVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class IntNode(val value: Int, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Int: $value \n"
    }

    override fun <T> visit(visitor: AstVisitor<T>): T {
        return visitor.visited_by(this);
    }
}


abstract class AstVisitor<T>() {
    abstract fun visited_by(node: IntNode): T
    abstract fun visited_by(node: IdentifierNode): T
    abstract fun visited_by(node: ApplicationNode): T
}
