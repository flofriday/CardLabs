package simulation.models

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
    val message: String,
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogMessage

        return message == other.message
    }

    override fun hashCode(): Int {
        return message.hashCode()
    }
}
