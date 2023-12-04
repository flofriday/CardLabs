package cardscheme

abstract class Token(val location: Location)

class SingleQuoteToken(location: Location) : Token(location)

class PoundToken(location: Location) : Token(location)

class QuoteToken(location: Location) : Token(location)

class LParenToken(location: Location) : Token(location)

class RParenToken(location: Location) : Token(location)

class BooleanToken(val value: Boolean, location: Location) : Token(location)

class IntegerToken(val value: Int, location: Location) : Token(location)

class FloatToken(val value: Float, location: Location) : Token(location)

class CharToken(val value: Char, location: Location) : Token(location)

class StringToken(val value: String, location: Location) : Token(location)

class IdentifierToken(val value: String, location: Location) : Token(location)

class BeginToken(location: Location) : Token(location)

class DefineToken(location: Location) : Token(location)

class SetToken(location: Location) : Token(location)

class DoToken(location: Location) : Token(location)

class IfToken(location: Location) : Token(location)

class LambdaToken(location: Location) : Token(location)
