package simulation.models

class SystemLogMessage(
    message: String,
) : LogMessage(message) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
