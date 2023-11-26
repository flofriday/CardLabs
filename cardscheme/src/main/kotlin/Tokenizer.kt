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
            } else if (c == '"') {
                tokens.addLast(scanString())
            } else if (c == '\n') {
                col = 1
                line++
                index++
            } else if (c == ';') {
                col = 1
                line++
                while (index < program.length && program[index++] != '\n') {}
            } else if (c == '#') {
                tokens.addLast(scanPoundSign())
            } else if (c.isDigit()) {
                tokens.addLast(scanNumber())
            } else if (consumeMatch("begin")) {
                tokens.add(BeginToken(Location(line, line, col - "begin".length, col - 1)))
            } else if (consumeMatch("define")) {
                tokens.add(DefineToken(Location(line, line, col - "define".length, col - 1)))
            } else if (consumeMatch("do")) {
                tokens.add(DoToken(Location(line, line, col - "do".length, col - 1)))
            } else if (consumeMatch("if")) {
                tokens.add(IfToken(Location(line, line, col - "if".length, col - 1)))
            } else if (consumeMatch("lambda")) {
                tokens.add(LambdaToken(Location(line, line, col - "lambda".length, col - 1)))
            } else if (consumeMatch("quote")) {
                tokens.add(QuoteToken(Location(line, line, col - "quote".length, col - 1)))
            } else if (isIdentifierInitial(c)) {
                tokens.addLast(scanIdentifier())
            } else if (c.isWhitespace()) {
                consume()
            } else {
                throw SchemeError(
                    "Parsing Error",
                    "I got confused during parsing, with an unexpected character",
                    Location(line, line, col, col),
                    null,
                )
            }
        }

        return tokens
    }

    private fun isIdentifierInitial(c: Char): Boolean {
        // FIXME: add ... support
        return c == '+' ||
            c == '-' ||
            c.isLetter() ||
            c == '!' ||
            c == '$' ||
            c == '%' ||
            c == '&' ||
            c == '*' ||
            c == '/' ||
            c == ':' ||
            c == '<' ||
            c == '=' ||
            c == '>' ||
            c == '?' ||
            c == '~' ||
            c == '_' ||
            c == '^'
    }

    // <initial> | <digit> | . | + | -
    private fun isIdentifierSubsequent(c: Char): Boolean {
        return isIdentifierInitial(c) || c == '.' || c.isDigit()
    }

    private fun scanIdentifier(): IdentifierToken {
        val initial = consume()
        var literal: String = initial.toString()

        // FIXME: add support for '...'
        if (initial == '+' || initial == '-') {
            return IdentifierToken(literal, Location(line, line, col - literal.length, col - 1))
        }

        while (isIdentifierSubsequent(peek())) {
            literal += consume()
        }

        return IdentifierToken(literal, Location(line, line, col - literal.length, col - 1))
    }

    private fun scanNumber(): Token {
        var literal = ""

        while (peek().isDigit()) {
            val digit = consume()
            literal += digit
        }

        // Return here if it is just an integer
        if (peek() != '.') {
            return IntegerToken(
                literal.toInt(),
                Location(line, line, col - literal.length, col - 1),
            )
        }

        // Eat the point
        consume()
        literal += "."

        while (peek().isDigit()) {
            val digit = consume()
            literal += digit
        }

        return FloatToken(literal.toFloat(), Location(line, line, col - literal.length, col - 1))
    }

    private fun scanPoundSign(): Token {
        consume()
        if (peek().equals('t', ignoreCase = true)) {
            consume()
            return BooleanToken(true, Location(line, line, col - 2, col - 1))
        } else if (peek().equals('f', ignoreCase = true)) {
            consume()
            return BooleanToken(false, Location(line, line, col - 2, col - 1))
        } else {
            return PoundToken(Location(line, line, col - 1, col - 1))
        }

        throw Exception("No Chars are implemented yet")
    }

    private fun scanCharacter(): CharToken {
        consume()
        return when (consume()) {
            'b' -> CharToken('\b', Location(line, line, col - 2, col))
            't' -> CharToken('\t', Location(line, line, col - 2, col))
            'n' -> CharToken('\n', Location(line, line, col - 2, col))
            'r' -> CharToken('\r', Location(line, line, col - 2, col))
            '"' -> CharToken('\"', Location(line, line, col - 2, col))
            '\\' -> CharToken('\\', Location(line, line, col - 2, col))
            else -> {
                throw SchemeError(
                    "Unknown character",
                    "Did not expect this character",
                    Location(line, line, col, col),
                    null,
                )
            }
        }
    }

    private fun scanString(): StringToken {
        val startLine = line
        val startCol = col
        consume()

        var stringValue = ""
        while (peek() != '"') {
            if (peek() == '\\') {
                val char = scanCharacter()
                stringValue += char.value
                continue
            }
            stringValue += consume()
        }

        consume()
        return StringToken(stringValue, Location(startLine, line, startCol, col))
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
