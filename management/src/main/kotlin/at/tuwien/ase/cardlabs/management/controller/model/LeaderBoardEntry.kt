package at.tuwien.ase.cardlabs.management.controller.model

/**
 * A model used to represent the data required for displaying a leaderboard entry
 */
data class LeaderBoardEntry (
    val place: Long,
    val score: Long,
    val botName: String,
    val userName: String
)