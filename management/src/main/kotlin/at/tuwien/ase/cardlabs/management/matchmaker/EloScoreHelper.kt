package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import kotlin.math.pow

/**
 * A helper class that contains useful methods to aid the calculation of elo scores
 */
class EloScoreHelper {

    companion object {

        /**
         * Calculate the new elo score for a bot
         * <p>
         * Note: it does not update the bot value in the database
         */
        @JvmStatic
        fun calculateScore(bot: Bot, wonGame: Boolean, allBots: List<Bot>): Int {
            val powOperation: (Int) -> Int = { score -> 10.0.pow(score / 400.0).toInt() }
            var denominator = 0
            allBots.forEach { botDAO -> denominator += powOperation(botDAO.eloScore) }
            val numerator = powOperation(bot.eloScore)
            val expected = numerator.toDouble() / denominator.toDouble()
            val modification = 32 * ((if (wonGame) 1 else 0) - expected)
            val newScore = bot.eloScore + modification
            return newScore.toInt()
        }
    }
}
