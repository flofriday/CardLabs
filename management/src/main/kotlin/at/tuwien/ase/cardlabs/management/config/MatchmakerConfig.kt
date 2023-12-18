package at.tuwien.ase.cardlabs.management.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cardlabs.matchmaker")
data class MatchmakerConfig(
    val matchSize: MatchSize,
//    val matchSkillDifference: MatchSkillDifference,
)

data class MatchSize(
    val min: Int,
    val max: Int,
)

data class MatchSkillDifference(
    val optimalMaxAbsolute: Int, // The optimal max elo skill difference between the best and worst player
    val optimalMaxRelative: Int,
    val hardMaxAbsolute: Int,
    val hardMaxRelative: Int,
)
