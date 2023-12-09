package at.tuwien.ase.cardlabs.management.database.model.match.log

class SystemLogMessage(
    round: Long,
    message: String
) : LogMessage(round, message)
