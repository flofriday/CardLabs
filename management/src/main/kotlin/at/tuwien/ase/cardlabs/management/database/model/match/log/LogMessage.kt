package at.tuwien.ase.cardlabs.management.database.model.match.log

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SystemLogMessage::class, name = "system"),
    JsonSubTypes.Type(value = DebugLogMessage::class, name = "debug")
)
open class LogMessage(
    val round: Long,
    val message: String
) : Serializable
