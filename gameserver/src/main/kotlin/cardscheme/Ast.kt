package cardscheme

/**
 * The internal representation (abstract syntax tree) for a complete program.
 *
 * Keep in mind this really is just an internal representation and since the parser already does
 * some desugarinig, it might not look exactly like the input program. However, semantically they
 * should be equally. So it happens quite often that multiple Scheme constructs are mapped to one
 * Ast entity.
 *
 * @param forms the statements and expressions that make up the program.
 */
class Ast(val forms: List<AstNode>) {
    /**
     * Dump the complete AST to a string.
     *
     * @return the dump.
     */
    fun dump(): String {
        var out = "AST\n"
        for (child in forms) {
            out += child.dump(1)
        }
        return out
    }
}

/**
 * Everything in the AST is a AstNode.
 *
 * @param location the location in the source document.
 */
abstract class AstNode(val location: Location) {
    /**
     * Recursively dump the node and all of it's children to a string for debugging.
     *
     * @param indent is the number of indentation for the current node.
     * @return the string with the AST tree
     */
    abstract fun dump(indent: Int): String

    /**
     * The indentation string for a given indent.
     *
     * @param indent the number of indentations.
     * @return the indentation string.
     */
    protected fun getIndentation(indent: Int): String {
        return " ".repeat(indent * 2)
    }
}

/** Any kind of expression in the AST. (Everything that returns something) */
abstract class ExpressionNode(location: Location) : AstNode(location) {
    abstract fun <T> visit(visitor: ExpressionVisitor<T>): T
}

/** Any kind of statement in the AST. (Everything that doesn't return anything) */
abstract class StatementNode(location: Location) : AstNode(location) {
    abstract fun <T> visit(visitor: StatementVisitor<T>): T
}

/**
 * A application of a procedure (aka function call).
 *
 * @param expressions the expressions making up the call where the first one will (hopefully)
 *   evaluate to a procedure.
 */
class ApplicationNode(val expressions: List<ExpressionNode>, location: Location) :
    ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "Application\n"
        for (child in expressions) {
            out += child.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * Definition and initialization of a single variable.
 *
 * @param name of the variable.
 * @param body which gets evaluated to initialize the variable.
 */
class DefineNode(val name: IdentifierNode, val body: ExpressionNode, location: Location) :
    StatementNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "DefineNode:\n"
        out += name.dump(indent + 1)
        out += body.dump(indent + 1)
        return out
    }

    override fun <T> visit(visitor: StatementVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A boolean literal.
 *
 * @param value of the literal.
 */
class BoolNode(val value: Boolean, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Boolean: $value \n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A integer literal.
 *
 * @param value of the literal.
 */
class IntNode(val value: Int, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Int: $value \n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A float literal.
 *
 * @param value of the literal.
 */
class FloatNode(val value: Float, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Float: $value \n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A float literal.
 *
 * @param value of the literal.
 */
class StringNode(val value: String, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "String: $value \n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A identifier.
 *
 * If evaluated on it's own it is a variable read. However, this node is also often used in other
 * nodes since it stores the location which means that other nodes that want to store a variable
 * name with a location also use it.
 *
 * @param identifier the name of the identifier.
 */
class IdentifierNode(val identifier: String, location: Location) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) + "Identifier: '$identifier'\n"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A body of a lambda, let etc.
 *
 * In general this can also be used if multiple expressions need to be evaluated sequentially.
 *
 * @param definitions zero or more definitions which get evaluated before the expressions.
 * @param expressions one or more expressions
 */
class BodyNode(
    val definitions: List<StatementNode>,
    val expressions: List<ExpressionNode>,
    location: Location,
) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "Body:\n"
        for (d in definitions) {
            out += d.dump(indent + 1)
        }
        for (e in expressions) {
            out += e.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * A lambda expression.
 *
 * Lambdas evaluate to a procedure (aka function).
 *
 * @param params are the names to which the arguments upon calling get bound.
 * @param body of the function.
 */
class LambdaNode(val params: List<IdentifierNode>, val body: BodyNode, location: Location) :
    ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) +
            "Lambda: '${params.joinToString(", ") { a -> a.identifier }}'\n" +
            body.dump(indent + 1)
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

data class VariableInitStep(
    val name: IdentifierNode,
    val init: ExpressionNode,
    val step: ExpressionNode?,
)

class DoNode(
    val variableInitSteps: List<VariableInitStep>,
    val test: ExpressionNode,
    val body: BodyNode?,
    val command: BodyNode?,
    location: Location,
) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        var out = getIndentation(indent) + "Do:\n"
        for (t in variableInitSteps) {
            out += t.name.dump(indent + 1)
            out += t.init.dump(indent + 1)
            if (t.step != null) {
                out += t.step.dump(indent + 1)
            }
        }
        out += test.dump(indent + 1)
        // FIXME: I don't like to omit it in the dump if it is null
        if (body != null) {
            out += body.dump(indent + 1)
        }
        if (command != null) {
            out += command.dump(indent + 1)
        }
        return out
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/**
 * If expression.
 *
 * Evaluating a if returns the value of the branch executed. If the else isn't present #void will be
 * returned.
 *
 * @param condition of the if, is evaluated first.
 * @param thenExpression is evaluated if the condition is true.
 * @param elseExpression is evaluated if it exists and the condition is false.
 */
class IfNode(
    val condition: ExpressionNode,
    val thenExpression: ExpressionNode,
    val elseExpression: ExpressionNode?,
    location: Location,
) : ExpressionNode(location) {
    override fun dump(indent: Int): String {
        return getIndentation(indent) +
            "If: ${condition.dump(indent)} \n ${thenExpression.dump(indent + 1)} \n ${
                elseExpression?.dump(
                    indent + 1,
                )
            }"
    }

    override fun <T> visit(visitor: ExpressionVisitor<T>): T {
        return visitor.visitedBy(this)
    }
}

/** The interface for a visitor that can visit all expressions in the AST. */
interface ExpressionVisitor<T> {
    fun visitedBy(node: BoolNode): T

    fun visitedBy(node: IntNode): T

    fun visitedBy(node: FloatNode): T

    fun visitedBy(node: StringNode): T

    fun visitedBy(node: IdentifierNode): T

    fun visitedBy(node: ApplicationNode): T

    fun visitedBy(node: BodyNode): T

    fun visitedBy(node: LambdaNode): T

    fun visitedBy(node: IfNode): T

    fun visitedBy(node: DoNode): T
}

/** The interface for a visitor that can visit all statements in the AST. */
interface StatementVisitor<T> {
    fun visitedBy(node: DefineNode): T
}
