package simulation

enum class Color {
    RED,
    YELLOW,
    BLUE,
    GREEN,
}

abstract class Card(open val color: Color) {
    abstract fun match(next: Card): Boolean
}

data class NumberCard(override val color: Color, val number: Int) : Card(color) {
    override fun match(next: Card): Boolean {
        if (color == next.color) {
            return true
        }

        if (next is NumberCard && number == next.number) {
            return true
        }

        return false
    }
}

data class SwitchCard(override val color: Color) : Card(color) {
    override fun match(next: Card): Boolean {
        if (color == next.color) {
            return true
        }

        if (next is SwitchCard) {
            return true
        }

        return false
    }
}

data class SkipCard(override val color: Color) : Card(color) {
    override fun match(next: Card): Boolean {
        if (color == next.color) {
            return true
        }

        if (next is SkipCard) {
            return true
        }

        return false
    }
}

data class PlusTwoCard(override val color: Color) : Card(color) {
    override fun match(next: Card): Boolean {
        if (color == next.color) {
            return true
        }

        if (next is PlusTwoCard) {
            return true
        }

        return false
    }
}

// FIXME: implement Choosecard
