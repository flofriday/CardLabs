package simulation.models

import java.io.Serializable

enum class Color {
    CYAN,
    ORANGE,
    GREEN,
    PURPLE,
    ANY,
}

enum class CardType {
    NUMBER_CARD,
    SWITCH,
    SKIP,
    DRAW_TWO,
    CHOOSE,
    CHOOSE_DRAW,
}

data class Card(
    val type: CardType,
    val color: Color,
    val number: Int?,
) : Serializable {
    fun match(next: Card): Boolean {
        // Match if both colors are the same
        if (color == next.color) {
            return true
        }

        // Choose cards can always be played
        if (next.type == CardType.CHOOSE || next.type == CardType.CHOOSE_DRAW) {
            return true
        }

        // Anything can be played on a choosecard with the color any
        if (color == Color.ANY) {
            return true
        }

        // Number cards only match if the numbers are the same
        if (type == CardType.NUMBER_CARD && next.type == CardType.NUMBER_CARD) {
            return number == next.number
        }

        // All other types of cards can mach only in their types
        return type == next.type
    }

    fun name(): String {
        var colorName = color.toString().lowercase()
        val typeName =
            when (type) {
                CardType.NUMBER_CARD -> number.toString()
                CardType.SWITCH -> "switch"
                CardType.SKIP -> "skip"
                CardType.DRAW_TWO -> "draw(+2)"
                CardType.CHOOSE -> "choose"
                CardType.CHOOSE_DRAW -> "choose-draw(+4)"
                else -> throw Exception("Invalid case")
            }

        if (typeName.startsWith("choose")) {
            if (color == Color.ANY) {
                colorName = "no specific color)"
            }
            return "$typeName card (wishing $colorName)"
        }

        return "$colorName $typeName card"
    }
}
