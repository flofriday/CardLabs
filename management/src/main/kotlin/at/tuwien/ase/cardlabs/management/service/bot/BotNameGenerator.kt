package at.tuwien.ase.cardlabs.management.service.bot

import at.tuwien.ase.cardlabs.management.config.BotConfig
import org.springframework.stereotype.Component

@Component
class BotNameGenerator(
    private val botConfig: BotConfig,
) {

    /**
     * Generates a random bot name by combining random syllables
     *
     * @return a random bot name
     */
    fun generateBotName(): String {
        val first = botConfig.name.generator.syllables.first
        val middle = botConfig.name.generator.syllables.middle
        val last = botConfig.name.generator.syllables.last

        return first.random() + middle.random() + last.random()
    }
}
