package simulation.models

import cardscheme.IntegerValue
import cardscheme.SchemeValue
import cardscheme.StringValue
import cardscheme.VectorValue

fun encodePlayer(player: Player): SchemeValue {
    return VectorValue(
        mutableListOf(
            // FIXME: this should probably be the bot name
            StringValue(player.bot.botId.toString(), null),
            IntegerValue(player.hand.size, null),
        ),
        null,
    )
}
