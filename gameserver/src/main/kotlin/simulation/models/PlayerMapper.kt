package simulation.models

import cardscheme.IntegerValue
import cardscheme.SchemeValue
import cardscheme.StringValue
import cardscheme.VectorValue

fun encodePlayer(player: Player): SchemeValue {
    return VectorValue(
        mutableListOf(
            StringValue(player.bot.name, null),
            IntegerValue(player.hand.size, null),
        ),
        null,
    )
}
