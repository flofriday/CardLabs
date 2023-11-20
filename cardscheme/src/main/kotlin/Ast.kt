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
}


abstract class ExpressionNode(location: Location) : AstNode(location) {
    abstract fun <T> visit(visitor: ExpressionVisitor<T>): T
}

abstract class StatementNode(location: Location) : AstNode(location) {
    abstract fun <T> visit(visitor: StatementVisitor<T>): T
}

class ApplicationNode(val expressions: List<ExpressionNode>, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "Application\n"
        for (child in expressions) {
            out += child.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class DefineNode(val names: List<IdentifierNode>, val bodies: List<ExpressionNode>, location: Location) :
    StatementNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "DefineNode:\n"
        for (child in names) {
            out += child.dump(indent + 1)
        }
        for (child in bodies) {
            out += child.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: StatementVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class IdentifierNode(val identifier: String, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Identifier: '$identifier'\n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class LambdaNode(val args: List<IdentifierNode>, val body: List<ExpressionNode>, location: Location) :
    ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Lambda: '${args.joinToString(", ") { a -> a.identifier }}'\n" +
                body.joinToString { c -> c.dump(indent + 1) + "\n" }
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class ListNode(val expressions: List<ExpressionNode>, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "List: \n" + expressions.joinToString(", ") { e -> e.dump(indent + 1) }
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

class IntNode(val value: Int, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Int: $value \n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visited_by(this);
    }
}

interface ExpressionVisitor<T> {
    fun visited_by(node: IntNode): T
    fun visited_by(node: IdentifierNode): T
    fun visited_by(node: ApplicationNode): T
    fun visited_by(node: ListNode): T
    fun visited_by(node: LambdaNode): T
}

interface StatementVisitor<T> {
    abstract fun visited_by(node: DefineNode): T
}
