abstract class Token(val location: Location) {
}

class IntegerToken(val value: Int, location: Location) : Token(location) {

}

class FloatToken(val value: Float, location: Location) : Token(location) {

}

class IdentifierToken(val value: String, location: Location) : Token(location) {

}

class DefineToken(location: Location) : Token(location) {

}

class SingleQuoteToken(location: Location) : Token(location) {

}

class QuoteToken(location: Location) : Token(location) {

}

class LParenToken(location: Location) : Token(location) {
}

class RParenToken(location: Location) : Token(location) {
}
