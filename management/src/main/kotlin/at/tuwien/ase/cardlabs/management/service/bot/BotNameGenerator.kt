package at.tuwien.ase.cardlabs.management.service.bot

import org.springframework.stereotype.Component

@Component
class BotNameGenerator {

    /**
     * Generates a random bot name by combining random syllables
     *
     * @return a random bot name
     */
    fun generateBotName(): String {
        val first = listOf("Zar", "Xen", "Kry", "Vex", "Neo", "Jen", "Ty", "Rax")
        val middle = listOf("lon", "tar", "nix", "mek", "zor", "dor", "pho", "gi", "")
        val last = listOf("ium", "eon", "ax", "os", "us", "tron", "lar", "phis")

        return first.random() + middle.random() + last.random()
    }
}
