class Parser {
    var tokens: List<Token> = mutableListOf()
    var index = 0

    fun parse(tokens: List<Token>): Ast {
        this.tokens = tokens
        val ast = Ast(mutableListOf())
        while (index < tokens.size) {
            ast.forms.addLast(parseForm())
        }
        return ast
    }

    private fun parseForm(): AstNode {
        if (peek() is LParenToken) {
            if (peekn(2) is DefineToken) {
                return parseDefine()
            }
        }
        return parseExpression()
    }

    private fun parseExpression(): ExpressionNode {
        if (peek() is SingleQuoteToken) {
            return parseSingleQuote()
        } else if (peek() is BooleanToken) {
            val token = consume() as BooleanToken
            return BoolNode(token.value, token.location)
        } else if (peek() is IntegerToken) {
            val token = consume() as IntegerToken
            return IntNode(token.value, token.location)
        } else if (peek() is IdentifierToken) {
            val token = consume() as IdentifierToken
            return IdentifierNode(token.value, token.location)
        } else if (peek() is LParenToken) {
            if (peekn(2) is QuoteToken) {
                return parseQuote()
            } else if (peekn(2) is LambdaToken) {
                return parseLambda()
            } else if (peekn(2) is IfToken) {
                return parseIf()
            }
            return parseApplication()
        }
        throw SchemeError("Unexpected character", "I got confused whith this character", peek().location, null)
    }

    private fun parseExpressions(): List<ExpressionNode> {
        val expressionNodes = mutableListOf<ExpressionNode>()
        while (peek() !is RParenToken) {
            expressionNodes.addLast(parseExpression())
        }
        return expressionNodes
    }

    // (lambda () (display 42))
    // (lambda a 42)
    //           ^
    // (lambda (a b) (display (+ a b)))
    private fun parseLambda(): LambdaNode {
        val lparen = consume()
        consume()

        val args = mutableListOf<IdentifierNode>()

        if (peek() is IdentifierToken) {
            val token = consume() as IdentifierToken
            args.addLast(IdentifierNode(token.value, token.location))
        }

        val body = parseExpressions()
        val rparen = consume()

        return LambdaNode(args, body, Location.merge(lparen.location, rparen.location))
    }

    /*
    (if #T
	    (display "x is greater than 5"))
     */
    private fun parseIf(): IfNode {
        val lparen = consume()
        consume()

        val condition = parseExpression()
        val thenExpression = parseExpression()


        if (peek() is RParenToken) {
            //TODO: how to implement empty expression
            val rparen = consume()
            return IfNode(condition, thenExpression, null, Location.merge(lparen.location, rparen.location))
        }

        val elseExpression = parseExpression()
        val rparen = must(RParenToken::class.java, "Expected a right parenthesis here")
        return IfNode(condition, thenExpression, elseExpression, Location.merge(lparen.location, rparen.location))
    }


    private fun parseSingleQuote(): ListNode {
        val token = consume() as SingleQuoteToken
        must(LParenToken::class.java, "Quoted List must be followed my left parenthesis")
        val expressionNodes = parseExpressions()
        val rparen = consume()
        return ListNode(expressionNodes, Location.merge(token.location, rparen.location))
    }

    private fun parseQuote(): ListNode {
        val lparen = consume()
        consume()
        must(LParenToken::class.java, "Quoted List must be followed my left parenthesis")
        val expressionNodes = parseExpressions()
        consume()
        val rparen = must(RParenToken::class.java, "Expected a right parenthesis here")
        return ListNode(expressionNodes, Location.merge(lparen.location, rparen.location))
    }

    private fun parseDefine(): DefineNode {
        val lparen = consume()
        consume()

        val variableName = must(IdentifierToken::class.java, "Expected an identifier here") as IdentifierToken
        val body = parseExpression()
        val rparen = must(RParenToken::class.java, "Expected a right parenthesis here")

        return DefineNode(
            listOf(IdentifierNode(variableName.value, variableName.location)),
            listOf(body),
            Location.merge(lparen.location, rparen.location)
        )
    }

    private fun parseApplication(): ApplicationNode {
        val lparen = consume()
        val expressions: List<ExpressionNode> = mutableListOf()

        while (peek() !is RParenToken) {
            expressions.addLast(parseExpression())
        }

        val rparen = must(RParenToken::class.java, "Expected a right parenthesis here")
        return ApplicationNode(expressions, Location.merge(lparen.location, rparen.location))
    }

    private fun must(classs: Class<*>, error: String): Token {
        if (classs != peek()::class.java) {
            throw SchemeError("Unexpected symbol", error, peek().location, null)
        }
        return consume()
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