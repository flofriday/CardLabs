package cardscheme

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
        } else if (peek() is PoundToken) {
            if (peekn(2) is LParenToken) {
                return parseVector()
            }
        } else if (peek() is BooleanToken) {
            val token = consume() as BooleanToken
            return BoolNode(token.value, token.location)
        } else if (peek() is IntegerToken) {
            val token = consume() as IntegerToken
            return IntNode(token.value, token.location)
        } else if (peek() is FloatToken) {
            val token = consume() as FloatToken
            return FloatNode(token.value, token.location)
        } else if (peek() is CharToken) {
            val token = consume() as CharToken
            return CharNode(token.value, token.location)
        } else if (peek() is StringToken) {
            val token = consume() as StringToken
            return StringNode(token.value, token.location)
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
            } else if (peekn(2) is CondToken) {
                return parseCond()
            } else if (peekn(2) is LambdaToken) {
                return parseLambda()
            } else if (peekn(2) is LetToken) {
                return parseLet()
            } else if (peekn(2) is SetToken) {
                return parseSet()
            } else if (peekn(2) is DefineToken) {
                throw SchemeError("Misplaced Define", "Define cannot occur here.", peekn(2).location, null)
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
     * Spec: R7RS, chapter 4.1.4
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

        verifyUniqueNames(args)
        return LambdaNode(
            args,
            body,
            Location.merge(lparen.location, rparen.location),
        )
    }

    /**
     * Parse let expression.
     *
     * Spec: R7RS, chapter 4.2.2
     * (let ((<variable> <init>) ...) <body>)
     * (let* ((<variable> <init>) ...) <body>)
     * (letrec ((<variable> <init>) ...) <body>)
     * (letrec* ((<variable> <init>) ...) <body>)
     *
     * FIXME: At the moment it doesn't handle the named-let syntax (let <variable> ((<variable> <init>) ...) <body>)
     */
    private fun parseLet(): LetNode {
        val lparen = consume()
        val letToken = consume() as LetToken

        val bindings = mutableListOf<VariableBinding>()
        must<LParenToken>("I expected a opening left parenthesis here.")
        while (peek() !is RParenToken) {
            val bindingStart = must<LParenToken>("I expected a opening left parenthesis here.")
            val name = must<IdentifierToken>("I expected a variable name here.")
            val init = parseExpression()
            val bindingEnd = must<RParenToken>("I expected a closing right parenthesis here.")
            bindings.addLast(
                VariableBinding(
                    IdentifierNode(name.value, name.location),
                    init,
                    Location.merge(bindingStart.location, bindingEnd.location),
                ),
            )
        }
        consume()

        val body = parseBody()
        val rparen = must<RParenToken>("I expected a closing right parenthesis here.")

        // Only `let*` can have duplicates in names.
        if (!(letToken.star && !letToken.rec)) {
            verifyUniqueNames(bindings.map { b -> b.name })
        }
        return LetNode(letToken.rec, letToken.star, bindings, body, Location.merge(lparen.location, rparen.location))
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
     * Spec: R7RS, chapter 4.1.5
     * (if <test> <consequent> <alternate>)
     * (if <test> <consequent>)
     */
    private fun parseIf(): IfNode {
        val lparen = consume()
        consume()

        val condition = parseExpression()
        val thenExpression = parseExpression()

        if (peek() is RParenToken) {
            val rparen = consume()
            return IfNode(condition, thenExpression, null, Location.merge(lparen.location, rparen.location))
        }

        val elseExpression = parseExpression()
        val rparen = must<RParenToken>("Expected a right parenthesis here")
        return IfNode(condition, thenExpression, elseExpression, Location.merge(lparen.location, rparen.location))
    }

    /**
     * Parse an cond expression. It is desugared to nested if expressions
     *
     * Spec: R7RS, chapter 4.2.1
     * (cond <clause1> <clause2> ...)
     * where <clause> is (<test> <expression1> ...)
     */
    private fun parseCond(): IfNode {
        val lparen = consume()
        consume()

        val conditionsList = SchemeList<ExpressionNode>()
        val expressionsList = SchemeList<List<ExpressionNode>>()

        var currentClause: ExpressionNode? = null

        do {
            must<LParenToken>("Expected left parenthesis here")
            if (peek() is ElseToken) {
                consume()
                currentClause = BodyNode(emptyList(), parseExpressions(), lparen.location)
                must<RParenToken>("Expected right parenthesis here")
                break
            }

            val condition = parseExpression()
            val thenExpressions = parseExpressions()
            must<RParenToken>("Expected right parenthesis here")

            conditionsList.addFirst(condition)
            expressionsList.addFirst(thenExpressions)
        } while (peek() !is RParenToken)
        must<RParenToken>("Expected right parenthesis here")

        for ((condition, thenExpressions) in conditionsList.zip(expressionsList)) {
            currentClause =
                IfNode(
                    condition,
                    BodyNode(emptyList(), thenExpressions, lparen.location),
                    currentClause,
                    lparen.location,
                )
        }

        return currentClause as IfNode
    }

    /**
     * The parser behaves differently in a quoted environment
     */
    private fun parseQuotedExpression(): ExpressionNode {
        val token = peek()
        return when (token) {
            is LParenToken -> {
                val lparen = consume()
                val expressionNodes = mutableListOf<ExpressionNode>()
                while (peek() !is RParenToken) {
                    expressionNodes.addLast(parseQuotedExpression())
                }
                val rparen = consume()

                expressionNodes.addFirst(IdentifierNode("list", lparen.location))
                ApplicationNode(expressionNodes, Location.merge(lparen.location, rparen.location))
            }

            is IdentifierToken -> {
                consume()
                SymbolNode(token.value, token.location)
            }

            is BeginToken -> {
                consume()
                SymbolNode("begin", token.location)
            }

            is CondToken -> {
                consume()
                SymbolNode("cond", token.location)
            }

            is DefineToken -> {
                consume()
                SymbolNode("define", token.location)
            }

            is SetToken -> {
                consume()
                SymbolNode("set!", token.location)
            }

            is DoToken -> {
                consume()
                SymbolNode("do", token.location)
            }

            is IfToken -> {
                consume()
                SymbolNode("if", token.location)
            }

            is LambdaToken -> {
                consume()
                SymbolNode("lambda", token.location)
            }

            is LetToken -> {
                consume()
                SymbolNode(token.display(), token.location)
            }

            is ElseToken -> {
                consume()
                SymbolNode("else", token.location)
            }

            is QuoteToken -> {
                consume()
                SymbolNode("quote", token.location)
            }

            is BooleanToken -> {
                consume()
                return BoolNode(token.value, token.location)
            }

            is IntegerToken -> {
                consume()
                return IntNode(token.value, token.location)
            }

            is FloatToken -> {
                consume()
                return FloatNode(token.value, token.location)
            }

            is CharToken -> {
                consume()
                return CharNode(token.value, token.location)
            }

            is StringToken -> {
                consume()
                return StringNode(token.value, token.location)
            }

            else -> {
                throw SchemeError(
                    "Unexpected Token",
                    "While reading the sourcecode I got confused here",
                    token.location,
                    null,
                )
            }
        }
    }

    /**
     * A quote expression (with a single quote).
     *
     * Spec: R7RS, chapter 4.1.2
     * '<datum>
     *
     * This parser automatically desugars the syntax to an application
     *
     * FIXME: The parser doesn't match the spec
     */
    private fun parseSingleQuote(): ExpressionNode {
        val token = consume() as SingleQuoteToken
        return parseQuotedExpression()
    }

    /**
     * A quote expression (with the keyword).
     *
     * Spec: R7RS, chapter 4.1.2
     * (quote <datum>)
     *
     * This parser automatically desugars the syntax to an application
     *
     * FIXME: The parser doesn't match the spec
     */
    private fun parseQuote(): ExpressionNode {
        val lparen = consume()
        val quote = consume()

        val expr = parseQuotedExpression()
        val rparen = must<RParenToken>("Expected a closing right parenthesis here")
        return expr
    }

    /**
     * A vector expression
     *
     * Spec: R7RS, chapter 6.8
     * #(<expression> ...)
     *
     * This parser automatically desugars the syntax to an application.
     *
     * NOTE: This function intentionally deviates from the spec which would only create immutable vectors here but
     * in Cardscheme we won't have the concept of immutable vector values.
     */
    private fun parseVector(): ApplicationNode {
        val pound = consume()
        consume()
        val expressionNodes = parseExpressions()
        val rparen = must<RParenToken>("Expected a right parenthesis here")

        expressionNodes.addFirst(IdentifierNode("vector", pound.location))
        return ApplicationNode(expressionNodes, Location.merge(pound.location, rparen.location))
    }

    /**
     * Parse a begin expression.
     *
     * Spec: R7RS, chapter 4.2.3
     * (begin <expression or definition> ...)
     * (begin <expression1> <expression2> ...)
     *
     * FIXME: We only parse the second form here
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
     * Spec: R7RS, chapter 4.2.4
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
        val body =
            if (peek() !is RParenToken) {
                val bodyExpressions = parseExpressions()
                BodyNode(
                    listOf(),
                    bodyExpressions,
                    Location.merge(bodyExpressions.first().location, bodyExpressions.last().location),
                )
            } else {
                null
            }
        must<RParenToken>("I expected a closing right parenthesis here.")

        // parse the command
        val command =
            if (peek() !is RParenToken) {
                val commandExpressions = parseExpressions()
                BodyNode(
                    listOf(),
                    commandExpressions,
                    Location.merge(commandExpressions.first().location, commandExpressions.last().location),
                )
            } else {
                null
            }

        val end = must<RParenToken>("I expected a closing right parenthesis here.")

        verifyUniqueNames(variableInitSteps.map { v -> v.name })
        return DoNode(variableInitSteps, test, body, command, Location.merge(start.location, end.location))
    }

    /**
     * Parse a definition.
     *
     * Spec: R7RS, chapter 5.3
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

            verifyUniqueNames(args)
            return DefineNode(
                IdentifierNode(functionName.value, functionName.location),
                LambdaNode(args, body, body.location),
                Location.merge(lparen.location, rparen.location),
            )
        } else {
            throw SchemeError(
                "Unexpected Token",
                "I expected either an identifier or a left parenthesis here.",
                peek().location,
                null,
            )
        }
    }

    /**
     * Parse a set! expression.
     *
     * Spec: R7RS, chapter 4.16
     * (set! <variable> <expression>)
     *
     */
    private fun parseSet(): SetNode {
        val lparen = consume()
        consume()

        val name = must<IdentifierToken>("The first argument of a `set!` must be a variable name.")
        val expression = parseExpression()
        val rparen = must<RParenToken>("I expected a closing right parenthesis here.")

        return SetNode(
            IdentifierNode(name.value, name.location),
            expression,
            Location.merge(lparen.location, rparen.location),
        )
    }

    /**
     * Parse a application of a procedure (aka function call).
     *
     * Spec: R7RS, chapter 4.1.3
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

    private fun verifyUniqueNames(names: List<IdentifierNode>) {
        val nameSet = HashSet<String>()

        for (name in names) {
            if (nameSet.contains(name.identifier)) {
                throw SchemeError(
                    "Name Error",
                    "All names must be unique, however this one was already used.",
                    name.location,
                    null,
                )
            }
            nameSet.add(name.identifier)
        }
    }

    private inline fun <reified T : Token> must(error: String): T {
        if (peek() !is T) {
            throw SchemeError("Unexpected symbol", error, peek().location, null)
        }
        return consume() as T
    }

    private fun peek(): Token {
        if (index >= tokens.size) {
            throw SchemeError(
                "Unexpected end of file",
                "I was in the middle of reading something but the file just ended.",
                null,
                null,
            )
        }
        return tokens[index]
    }

    private fun peekn(n: Int): Token {
        if (index + n - 1 >= tokens.size) {
            throw SchemeError(
                "Unexpected end of file",
                "I was in the middle of reading something but the file just ended.",
                null,
                null,
            )
        }
        return tokens[index + n - 1]
    }

    private fun consume(): Token {
        if (index >= tokens.size) {
            throw SchemeError(
                "Unexpected end of file",
                "I was in the middle of reading something but the file just ended.",
                null,
                null,
            )
        }
        index++
        return tokens[index - 1]
    }
}
