package at.tuwien.ase.cardlabs.management.database.model.game.log

class DebugLogMessage(
    round: Long,
    message: String,
    val botId: Long,
) : LogMessage(round, message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DebugLogMessage

        return botId == other.botId
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + botId.hashCode()
        return result
    }
}
