package at.tuwien.ase.cardlabs.management.database.model.game.log

class SystemLogMessage(
    round: Long,
    message: String,
) : LogMessage(round, message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
