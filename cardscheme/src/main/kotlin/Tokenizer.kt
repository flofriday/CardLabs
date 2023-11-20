import java.lang.Exception

// Tokens are defined here:
// https://www.scheme.com/tspl2d/grammar.html
class Tokenizer() {
    var line = 1
    var col = 1
    var index = 0
    var program: String = ""

    // The line, col and index always point to the next token we are consuming
    fun tokenize(program: String): List<Token> {
        this.program = program
        val tokens = mutableListOf<Token>()

        while (index < program.length) {
            val c = program[index]

            if (c == '(') {
                tokens.addLast(LParenToken(Location(line, line, col, col)))
                consume()
            } else if (c == ')') {
                tokens.addLast(RParenToken(Location(line, line, col, col)))
                consume()
            } else if (c == '\'') {
                tokens.add(SingleQuoteToken(Location(line, line, col, col)))
                consume()
            } else if (c == '\n') {
                col = 1
                line++
                index++
            } else if (c == '#') {
                tokens.addLast(scanPoundSign())
            } else if (c.isDigit()) {
                tokens.addLast(scanNumber())
            } else if (consumeMatch("define")) {
                tokens.add(DefineToken(Location(line, line, col - 1 - "define".length, col - 1)))
            } else if (consumeMatch("quote")) {
                tokens.add(QuoteToken(Location(line, line, col - 1 - "quote".length, col - 1)))
            } else if (consumeMatch("lambda")) {
                tokens.add(LambdaToken(Location(line, line, col - 1 - "lambda".length, col - 1)))
            } else if (consumeMatch("if")) {
                tokens.add(IfToken(Location(line, line, col - 1 - "if".length, col - 1)))
            } else if (isIdentifierInitial(c)) {
                tokens.addLast(scanIdentifier())
            } else if (c.isWhitespace()) {
                consume()
            } else {
                throw SchemeError(
                    "Parsing Error",
                    "I got confused during parsing, with an unexpected character",
                    Location(line, line, col, col),
                    null
                )
            }
        }

        return tokens
    }

    private fun isIdentifierInitial(c: Char): Boolean {
        // FIXME: add ... support
        return c == '+' || c == '-' || c.isLetter() || c == '!' || c == '$' || c == '%' || c == '&' || c == '*' || c == '/' || c == ':' || c == '<' || c == '=' || c == '>' || c == '?' || c == '~' || c == '_' || c == '^'
    }

    //<initial> | <digit> | . | + | -
    private fun isIdentifierSubsequent(c: Char): Boolean {
        return isIdentifierInitial(c) || c == '.' || c.isDigit()
    }

    private fun scanIdentifier(): IdentifierToken {
        val initial = consume()
        var literal: String = initial.toString()

        // FIXME: add support for '...'
        if (initial == '+' || initial == '-') {
            return IdentifierToken(literal, Location(line, line, col - 1 - literal.length, col - 1))
        }

        while (isIdentifierSubsequent(peek())) {
            literal += consume()
        }

        return IdentifierToken(literal, Location(line, line, col - 1 - literal.length, col - 1))
    }

    private fun scanNumber(): Token {
        var literal = ""

        while (peek().isDigit()) {
            val digit = consume()
            literal += digit
        }

        // Return here if it is just an integer
        if (peek() != '.') {
            return IntegerToken(literal.toInt(), Location(line, line, col - 1 - literal.length, col - 1))
        }

        // Eat the point
        consume()
        literal += "."

        while (peek().isDigit()) {
            val digit = consume()
            literal += digit
        }

        return FloatToken(literal.toFloat(), Location(line, line, col - 1 - literal.length, col - 1))
    }

    private fun scanPoundSign(): BooleanToken {
        consume()
        if (peek().equals('t', ignoreCase = true)){
            consume()
            return BooleanToken(true, Location(line, line, col - 1, col - 1))
        }
        else if (peek().equals('f', ignoreCase = true)){
            consume()
            return BooleanToken(false, Location(line, line, col - 1, col - 1))
        }

        // FIXME: add support for chars
        throw Exception("No Chars are implemented yet")
    }

    private fun consumeMatch(expected: String): Boolean {
        if (expected.length + index > program.length) {
            return false
        }

        for (i in 0..<expected.length) {
            if (expected[i] != program[index + i]) return false
        }

        col += expected.length
        index += expected.length

        return true
    }

    private fun peek(): Char {
        if (index >= program.length) return ' '
        return program[index]
    }

    private fun consume(): Char {
        if (index >= program.length) return ' '
        col++
        index++
        return program[index - 1]
    }
}