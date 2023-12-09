package at.tuwien.ase.cardlabs.management.database.model.match.log

class DebugLogMessage(
    round: Long,
    message: String,
    val botId: Long
) : LogMessage(round, message)
