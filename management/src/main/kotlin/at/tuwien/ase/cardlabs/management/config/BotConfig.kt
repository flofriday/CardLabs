package at.tuwien.ase.cardlabs.management.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cardlabs.bot")
data class BotConfig(
    val elo: EloProperties,
    val name: NameGeneratorProperties,
)

data class EloProperties(
    val initialValue: Int,
)

data class NameGeneratorProperties(
    val generator: SyllablesProperties,
)

data class SyllablesProperties(
    val syllables: Syllables,
)

data class Syllables(
    val first: List<String>,
    val middle: List<String>,
    val last: List<String>,
)
