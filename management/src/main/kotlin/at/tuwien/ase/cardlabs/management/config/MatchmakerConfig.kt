package at.tuwien.ase.cardlabs.management.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cardlabs.matchmaker")
data class MatchmakerConfig(
    val matchSize: MatchSize,
    val matchSkillDifference: MatchSkillDifference,
)

data class MatchSize(
    val min: Int,
    val max: Int,
)

data class MatchSkillDifference(
    val relativeEloDifference: Double
)
