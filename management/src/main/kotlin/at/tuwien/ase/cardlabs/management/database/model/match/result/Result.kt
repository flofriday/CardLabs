package at.tuwien.ase.cardlabs.management.database.model.match.result

import java.io.Serializable

data class Result(
    val botId: Long,
    val botCodeId: Long,
    val oldEloScore: Long,
    val newEloScore: Long,
    val position: Int,
) : Serializable
