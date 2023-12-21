package at.tuwien.ase.cardlabs.management.database.model.game.result

import java.io.Serializable

data class Result(
    val botId: Long,
    val botCodeId: Long,
    val oldEloScore: Int,
    val newEloScore: Int,
    val position: Int,
) : Serializable
