package at.tuwien.ase.cardlabs.management.database.model.match.log

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SystemLogMessage::class, name = "system"),
    JsonSubTypes.Type(value = DebugLogMessage::class, name = "debug"),
)
open class LogMessage(
    val round: Long,
    val message: String,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogMessage

        if (round != other.round) return false
        return message == other.message
    }

    override fun hashCode(): Int {
        var result = round.hashCode()
        result = 31 * result + message.hashCode()
        return result
    }
}
