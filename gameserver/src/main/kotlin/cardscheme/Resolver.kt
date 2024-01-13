package cardscheme

/**
 * The resolver does some static analysis.
 *
 * At the moment it is only used to determine if a function call is a tail call.
 */
class Resolver : StatementVisitor<Unit>, ExpressionVisitor<Unit> {
    var isTailContext = false

    fun resolve(ast: Ast) {
        for (form in ast.forms) {
            if (form is StatementNode) {
                form.visit(this)
            }
            if (form is ExpressionNode) {
                form.visit(this)
            }
        }
    }

    override fun visitedBy(node: BoolNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: IntNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: FloatNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: StringNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: CharNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: IdentifierNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: SymbolNode) {
        // Intentionally left empty
    }

    override fun visitedBy(node: ApplicationNode) {
        val oldIsTail = isTailContext
        isTailContext = false
        node.expressions.map { e -> e.visit(this) }
        isTailContext = oldIsTail

        if (isTailContext) {
            node.isLast = true
        }
    }

    override fun visitedBy(node: BodyNode) {
        val oldIsTail = isTailContext
        isTailContext = false
        node.expressions.dropLast(1).map { e -> e.visit(this) }
        isTailContext = oldIsTail
        node.expressions.last().visit(this)
    }

    override fun visitedBy(node: LambdaNode) {
        val oldIsTailContext = isTailContext
        isTailContext = true
        node.body.visit(this)
        isTailContext = oldIsTailContext
    }

    override fun visitedBy(node: LetNode) {
        val oldIsTailContext = isTailContext
        isTailContext = false
        node.bindings.map { b -> b.init.visit(this) }
        isTailContext = oldIsTailContext

        node.body.visit(this)
    }

    override fun visitedBy(node: SetNode) {
        val oldIsTailContext = isTailContext
        isTailContext = false
        node.expression.visit(this)
        isTailContext = oldIsTailContext
    }

    override fun visitedBy(node: IfNode) {
        val oldIsTailContext = isTailContext
        isTailContext = false
        node.condition.visit(this)
        isTailContext = oldIsTailContext

        node.thenExpression.visit(this)
        node.elseExpression?.visit(this)
    }

    override fun visitedBy(node: DoNode) {
        val oldIsTailContext = isTailContext
        isTailContext = false
        node.test.visit(this)
        node.command?.visit(this)
        node.body?.visit(this)
        isTailContext = oldIsTailContext
    }

    override fun visitedBy(node: DefineNode) {
        val oldIsTailContext = isTailContext
        isTailContext = false
        node.body.visit(this)
        isTailContext = oldIsTailContext
    }
}
