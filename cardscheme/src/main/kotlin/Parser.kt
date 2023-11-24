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
        if (isDefinition()) {
            return parseDefinition()
        }
        return parseExpression()
    }

    private fun isDefinition(): Boolean {
        if (peek() is LParenToken) {
            if (peekn(2) is DefineToken) {
                return true
            }
        }
        return false
    }

    private fun parseDefinition(): StatementNode {
        if (peek() is LParenToken) {
            if (peekn(2) is DefineToken) {
                return parseDefine()
            }
        }
        throw Exception("Unknown Definition")
    }

    private fun parseDefinitions(): List<StatementNode> {
        val definitionNodes = mutableListOf<StatementNode>()
        while (isDefinition()) {
            definitionNodes.addLast(parseDefinition())
        }
        return definitionNodes
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
        } else if (peek() is FloatToken) {
            val token = consume() as FloatToken
            return FloatNode(token.value, token.location)
        } else if (peek() is IdentifierToken) {
            val token = consume() as IdentifierToken
            return IdentifierNode(token.value, token.location)
        } else if (peek() is LParenToken) {
            if (peekn(2) is QuoteToken) {
                return parseQuote()
            } else if (peekn(2) is BeginToken) {
                return parseBegin()
            } else if (peekn(2) is DoToken) {
                return parseDo()
            } else if (peekn(2) is IfToken) {
                return parseIf()
            } else if (peekn(2) is LambdaToken) {
                return parseLambda()
            }
            return parseApplication()
        }
        throw SchemeError("Unexpected token", "I got confused with ${peek()} token", peek().location, null)
    }

    private fun parseExpressions(): List<ExpressionNode> {
        val expressionNodes = mutableListOf<ExpressionNode>()
        while (peek() !is RParenToken) {
            expressionNodes.addLast(parseExpression())
        }
        return expressionNodes
    }

    /**
     * Parse a lambda expression.
     *
     * Spec: R7R, chapter 4.1.4
     * (lambda <formals> <body>)
     *
     * FIXME: Incomplete, varargs missing.
     */
    private fun parseLambda(): LambdaNode {
        val lparen = consume()
        consume()

        val args = mutableListOf<IdentifierNode>()

        if (peek() is IdentifierToken) {
            val token = consume() as IdentifierToken
            args.addLast(IdentifierNode(token.value, token.location))
        } else if (peek() is LParenToken) {
            consume()
            while (peek() !is RParenToken) {
                val token = must<IdentifierToken>("I expected an identifier here")
                args.addLast(IdentifierNode(token.value, token.location))
            }
            consume()
        }

        val body = parseBody()
        val rparen = consume()

        return LambdaNode(
            args,
            body,
            Location.merge(lparen.location, rparen.location),
        )
    }

    private fun parseBody(): BodyNode {
        val definitions = parseDefinitions()
        val expressions = parseExpressions()
        if (expressions.isEmpty()) {
            throw SchemeError("Invalid token", "I expected at least one expression here.", peek().location, null)
        }

        val location =
            if (definitions.isEmpty()) {
                Location.merge(
                    expressions.first().location,
                    expressions.last().location,
                )
            } else {
                Location.merge(definitions.first().location, expressions.last().location)
            }
        return BodyNode(definitions, expressions, location)
    }

    /**
     * Parse an if expression.
     *
     * Spec: R7R, chapter 4.1.5
     * (if <test> <consequent> <alternate>)
     * (if <test> <consequent>)
     */
    private fun parseIf(): IfNode {
        val lparen = consume()
        consume()

        val condition = parseExpression()
        val thenExpression = parseExpression()

        if (peek() is RParenToken) {
            // TODO: how to implement empty expression
            val rparen = consume()
            return IfNode(condition, thenExpression, null, Location.merge(lparen.location, rparen.location))
        }

        val elseExpression = parseExpression()
        val rparen = must<RParenToken>("Expected a right parenthesis here")
        return IfNode(condition, thenExpression, elseExpression, Location.merge(lparen.location, rparen.location))
    }

    /**
     * A quote expression (with a single quote).
     *
     * Spec: R7R, chapter 4.1.2
     * '<datum>
     *
     * FIXME: The parser doesn't match the spec
     */
    private fun parseSingleQuote(): ListNode {
        val token = consume() as SingleQuoteToken
        must<LParenToken>("Quoted List must be followed my left parenthesis")
        val expressionNodes = parseExpressions()
        val rparen = consume()
        return ListNode(expressionNodes, Location.merge(token.location, rparen.location))
    }

    /**
     * A quote expression (with the keyword).
     *
     * Spec: R7R, chapter 4.1.2
     * (quote <datum>)
     *
     * FIXME: The parser doesn't match the spec
     */
    private fun parseQuote(): ListNode {
        val lparen = consume()
        consume()
        must<LParenToken>("Quoted List must be followed my left parenthesis")
        val expressionNodes = parseExpressions()
        consume()
        val rparen = must<RParenToken>("Expected a right parenthesis here")
        return ListNode(expressionNodes, Location.merge(lparen.location, rparen.location))
    }

    /**
     * Parse a begin expression.
     *
     * Spec: R7R, chapter 4.2.3
     * (begin <expression or definition> ...)
     * (begin <expression1> <expression2> ...)
     *
     * FIXME: We only attempt to parse the second form here but even there I am not sure, if we do it correctly..
     */
    private fun parseBegin(): BodyNode {
        val lparen = consume()
        consume()

        val expressions = parseExpressions()
        val rparen = must<RParenToken>("Expected a right parenthesis here")

        return BodyNode(
            listOf(),
            expressions,
            Location.merge(lparen.location, rparen.location),
        )
    }

    /**
     * Parse a do loop.
     *
     * Spec: R7R, chapter 4.2.4
     * (do ((<variable1> <init1> <step1>) ...)
     *      (<test> <expression> ...)
     * <command> ...)
     */
    private fun parseDo(): DoNode {
        val start = consume()
        consume()

        // parse variables part
        val variableInitSteps = mutableListOf<VariableInitStep>()
        must<LParenToken>("I expected a left parenthesis here.")
        while (peek() is LParenToken) {
            consume()
            val name = must<IdentifierToken>("I expected an identifier here.")
            val init = parseExpression()
            val step = if (peek() !is RParenToken) parseExpression() else null
            must<RParenToken>("I expected a closing right parenthesis here.")
            variableInitSteps.addLast(VariableInitStep(IdentifierNode(name.value, name.location), init, step))
        }
        must<RParenToken>("I expected a closing right parenthesis here.")

        // parse the test
        must<LParenToken>("I expected a opening left parenthesis here.")
        val test = parseExpression()
        val body = if (peek() !is RParenToken) {
            val bodyExpressions = parseExpressions()
            BodyNode(listOf(), bodyExpressions, Location.merge(bodyExpressions.first().location, bodyExpressions.last().location))
        } else {
            null
        }
        must<RParenToken>("I expected a closing right parenthesis here.")

        // parse the command
        val command = if (peek() !is RParenToken) {
            val commandExpressions = parseExpressions()
            BodyNode(listOf(), commandExpressions, Location.merge(commandExpressions.first().location, commandExpressions.last().location))
        } else {
            null }

        val end = must<RParenToken>("I expected a closing right parenthesis here.")
        return DoNode(variableInitSteps, test, body, command, Location.merge(start.location, end.location))
    }

    /**
     * Parse a definition.
     *
     * Spec: R7R, chapter 5.3
     * (define <variable> <expression>)
     * (define (<variable> <formals>) <body>)
     * (define (<variable> . <formal>) <body>)
     *
     * Since the last two forms are just syntactic sugar for a define of the first form with a nested lambda this parser
     * de-sugars it.
     *
     * FIXME: Implement the varargs form.
     */
    private fun parseDefine(): DefineNode {
        val lparen = consume()
        consume()

        if (peek() is IdentifierToken) {
            val variableName = consume() as IdentifierToken
            val body = parseExpression()
            val rparen = must<RParenToken>("Expected a right parenthesis here")

            return DefineNode(
                IdentifierNode(variableName.value, variableName.location),
                body,
                Location.merge(lparen.location, rparen.location),
            )
        } else if (peek() is LParenToken) {
            consume()
            val functionName = must<IdentifierToken>("Expected an identifier here")

            val args = mutableListOf<IdentifierNode>()
            while (peek() !is RParenToken) {
                val arg = must<IdentifierToken>("Expected an identifier here")
                args.addLast(IdentifierNode(arg.value, arg.location))
            }
            consume()

            val body = parseBody()
            val rparen = must<RParenToken>("Expected a right parenthesis here")

            return DefineNode(
                IdentifierNode(functionName.value, functionName.location),
                LambdaNode(args, body, body.location),
                Location.merge(lparen.location, rparen.location),
            )
        } else {
            throw SchemeError("Unexpected Token", "I expected either an identifier or a left parenthesis here.", peek().location, null)
        }
    }

    /**
     * Parse a application of a procedure (aka function call).
     *
     * Spec: R7R, chapter 4.1.3
     * (<operator> <operator1> ...)
     */
    private fun parseApplication(): ApplicationNode {
        val lparen = consume()
        val expressions: List<ExpressionNode> = mutableListOf()

        while (peek() !is RParenToken) {
            expressions.addLast(parseExpression())
        }

        val rparen = must<RParenToken>("Expected a right parenthesis here")
        return ApplicationNode(expressions, Location.merge(lparen.location, rparen.location))
    }

    private inline fun <reified T : Token> must(
        error: String,
    ): T {
        if (peek() !is T) {
            throw SchemeError("Unexpected symbol", error, peek().location, null)
        }
        return consume() as T
    }

    private fun peek(): Token {
        if (index >= tokens.size) {
            throw SchemeError("Unexpected end of file", "I was in the middle of reading something but the file just ended.", null, null)
        }
        return tokens[index]
    }

    private fun peekn(n: Int): Token {
        if (index + n - 1 >= tokens.size) {
            throw SchemeError("Unexpected end of file", "I was in the middle of reading something but the file just ended.", null, null)
        }
        return tokens[index + n - 1]
    }

    private fun consume(): Token {
        if (index >= tokens.size) {
            throw SchemeError("Unexpected end of file", "I was in the middle of reading something but the file just ended.", null, null)
        }
        index++
        return tokens[index - 1]
    }
}
