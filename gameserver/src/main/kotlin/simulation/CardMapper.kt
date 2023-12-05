package simulation

import cardscheme.SchemeValue
import cardscheme.StringValue
import cardscheme.VectorValue

fun encodeCard(card: Card): SchemeValue {
    val type =
        when (card) {
            is NumberCard -> card.number.toString()
            is SwitchCard -> "switch"
            is SkipCard -> "skip"
            is DrawCard -> "draw"
            is ChooseCard -> "choose"
            is ChooseDrawCard -> "chooseDraw"
            else -> throw Exception("Invalid case")
        }
    return VectorValue(mutableListOf<SchemeValue>(StringValue(card.color.toString().lowercase()), StringValue(type)))
}

fun decodeCard(value: SchemeValue): Card {
    if (value !is VectorValue) {
        throw DecodeError("The card is not a vector")
    }

    if (value.values.size != 2) {
        throw DecodeError("The cards only have 2 members but the value has ${value.values.size}")
    }

    if (!value.values.all { m -> m is StringValue }) {
        throw DecodeError("Both fields of a card must be strings")
    }

    val color = (value.values[0] as StringValue).value.uppercase()
    val type = (value.values[1] as StringValue).value
    return when {
        type == "switch" -> SwitchCard(Color.valueOf(color))
        type == "skip" -> SkipCard(Color.valueOf(color))
        type == "draw" -> DrawCard(Color.valueOf(color))
        type == "choose" -> ChooseCard(Color.valueOf(color))
        type == "chooseDraw" -> ChooseDrawCard(Color.valueOf(color))
        (type.toIntOrNull() != null) -> NumberCard(Color.valueOf(color), type.toInt())
        else -> throw DecodeError("Invalid type $type")
    }
}

class DecodeError(val reason: String) : Throwable()
