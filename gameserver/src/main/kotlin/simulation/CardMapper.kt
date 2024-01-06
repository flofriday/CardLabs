package simulation

import cardscheme.*
import simulation.models.*

fun encodeCard(card: Card): SchemeValue {
    val type =
        when (card.type) {
            CardType.NUMBER_CARD -> IntegerValue(card.number!!, null)
            CardType.SWITCH -> StringValue("switch", null)
            CardType.SKIP -> StringValue("skip", null)
            CardType.DRAW_TWO -> StringValue("draw", null)
            CardType.CHOOSE -> StringValue("choose", null)
            CardType.CHOOSE_DRAW -> StringValue("choose-draw", null)
            else -> throw Exception("Invalid case")
        }
    return VectorValue(
        mutableListOf<SchemeValue>(
            SymbolValue(card.color.toString().lowercase(), null),
            type,
        ),
        null,
    )
}

fun decodeCard(value: SchemeValue): Card {
    if (value !is VectorValue) {
        throw DecodeError("The card is not a vector")
    }

    if (value.values.size != 2) {
        throw DecodeError("The cards only have 2 members but the value has ${value.values.size}")
    }

    if (value.values[0] !is SymbolValue) {
        throw DecodeError("The first field of a card must be a symbol")
    }

    if (value.values[1] !is StringValue && value.values[1] !is IntegerValue) {
        throw DecodeError("The second field of a card must be a string or integer")
    }

    val color = Color.valueOf((value.values[0] as SymbolValue).value.uppercase())

    if (value.values[1] is IntegerValue) {
        val num = (value.values[1] as IntegerValue).value
        if (!(0..9).contains(num)) {
            throw DecodeError("Number cards can only hold numbers between 0 and 9, but it was: $num")
        }
        return Card(CardType.NUMBER_CARD, color, num)
    }

    val typeValue = (value.values[1] as StringValue).value
    val type: CardType =
        when (typeValue) {
            "switch" -> CardType.SWITCH
            "skip" -> CardType.SKIP
            "draw" -> CardType.DRAW_TWO
            "choose" -> CardType.CHOOSE
            "choose-draw" -> CardType.CHOOSE_DRAW
            else -> throw DecodeError("Invalid type $typeValue")
        }

    return Card(type, color, null)
}

class DecodeError(val reason: String) : Throwable()
