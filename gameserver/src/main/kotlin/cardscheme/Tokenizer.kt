package cardscheme

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
            } else if (c.isDigit() || isNegativeNumber(c)) {
                tokens.addLast(scanNumber())
            } else if (isIdentifierInitial(c)) {
                tokens.addLast(scanIdentifierKeyword())
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

    private fun isNegativeNumber(c: Char): Boolean {
        return (index + 1 < program.length) && (peek() == '-') && program[index + 1].isDigit()
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

    private fun scanIdentifierKeyword(): Token {
        val initial = consume()
        var literal: String = initial.toString()

        // FIXME: add support for '...'
        if (initial == '+' || initial == '-') {
            return IdentifierToken(literal, Location(line, line, col - literal.length, col - 1))
        }

        while (isIdentifierSubsequent(peek())) {
            literal += consume()
        }

        val location = Location(line, line, col - literal.length, col - 1)
        return when(literal) {
        "begin" ->  BeginToken(location)
        "define" ->  DefineToken(location)
        "do" ->  DoToken(location)
        "if" ->  IfToken(location)
        "cond" ->  CondToken(location)
        "else" ->  ElseToken(location)
        "lambda" ->  LambdaToken(location)
        "letrec*" ->  LetToken(true, true, location)
        "letrec" ->  LetToken(true, false, location)
        "let*" ->  LetToken(false, true, location)
        "let" ->  LetToken(false, false, location)
        "quote" ->  QuoteToken(location)
        "set!" ->  SetToken(location)
            else ->  IdentifierToken(literal, location)
        }
    }

    private fun scanNumber(): Token {
        var literal = ""

        if (peek() == '-') {
            literal += consume()
        }

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
        return if (peek().equals('t', ignoreCase = true)) {
            consume()
            BooleanToken(true, Location(line, line, col - 2, col - 1))
        } else if (peek().equals('f', ignoreCase = true)) {
            consume()
            BooleanToken(false, Location(line, line, col - 2, col - 1))
        } else if (peek() == '\\') {
            consume()
            scanCharacter()
        } else {
            PoundToken(Location(line, line, col - 1, col - 1))
        }
    }

    private fun scanCharacter(): CharToken {
        if (peek().isLetter()) {
            return CharToken(consume(), Location(line, line, col - 2, col - 1))
        } else {
            throw SchemeError("Unknown Character", "This does not seem to be a character", Location(line, line, col - 2, col - 1), null)
        }
    }

    private fun scanEscapeCharacter(): Char {
        consume()

        return when (consume()) {
            'b' -> '\b'
            't' -> '\t'
            'n' -> '\n'
            'r' -> '\r'
            '"' -> '\"'
            '\\' -> '\\'
            else -> {
                throw SchemeError(
                    "Unknown escape character",
                    "I did not expect this character.",
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
                stringValue += scanEscapeCharacter()
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
