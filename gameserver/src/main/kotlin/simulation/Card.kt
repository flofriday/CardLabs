package simulation

abstract class Card {
    abstract fun match(next: Card): Boolean
}

enum class Color {
    RED,
    YELLOW,
    BLUE,
    GREEN,
}

abstract class ColorCard(val color: Color) : Card()

class NumberCard(color: Color, val number: Int) : ColorCard(color) {
    override fun match(next: Card): Boolean {
        if (next is ColorCard && color == next.color) {
            return true
        }

        if (next is NumberCard && number == next.number) {
            return true
        }

        return false
    }
}

class SwitchCard(color: Color) : ColorCard(color) {
    override fun match(next: Card): Boolean {
        if (next is ColorCard && color == next.color) {
            return true
        }

        if (next is SwitchCard) {
            return true
        }

        return false
    }
}

class SkipCard(color: Color) : ColorCard(color) {
    override fun match(next: Card): Boolean {
        if (next is ColorCard && color == next.color) {
            return true
        }

        if (next is SkipCard) {
            return true
        }

        return false
    }
}

class PlusTwoCard(color: Color) : ColorCard(color) {
    override fun match(next: Card): Boolean {
        if (next is ColorCard && color == next.color) {
            return true
        }

        if (next is PlusTwoCard) {
            return true
        }

        return false
    }
}

// FIXME: implement Choosecard
