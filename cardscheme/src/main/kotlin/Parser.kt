class Parser {
    var tokens: List<Token> = mutableListOf()
    var index = 0

    fun parse(tokens: List<Token>): Ast {
        this.tokens = tokens
        var ast = Ast(mutableListOf())
        while (index < tokens.size) {
            ast.forms.addLast(parseForm())
        }
        return ast
    }

    private fun parseForm(): AstNode {
        return parseExpression()
    }

    private fun parseExpression(): ExpressionNode {
        if (peek() is IntegerToken) {
            val token = consume() as IntegerToken
            return IntNode(token.value, token.location)
        } else if (peek() is IdentifierToken) {
            val token = consume() as IdentifierToken
            return IdentifierNode(token.value, token.location)
        } else if (peek() is LParenToken) {
            val lparen = consume()
            val expressions: List<ExpressionNode> = mutableListOf()

            while (peek() !is RParenToken) {
                expressions.addLast(parseExpression())
            }

            if (peek() !is RParenToken) {
                throw Exception("Expected a right parent here")
            }

            val rparen = consume()
            return ApplicationNode(expressions, Location.merge(lparen.location, rparen.location))
        }
        throw Exception("Unexpected token ${tokens[index]} at index $index")
    }

    private fun peek(): Token {
        return tokens[index]
    }

    private fun peekn(n: Int): Token {
        return tokens[index + n - 1]
    }


    private fun consume(): Token {
        index++
        return tokens[index - 1]
    }
}