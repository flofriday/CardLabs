package at.tuwien.ase.cardlabs.management.database.converter

import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

class LogMessageListConverter(private val objectMapper: ObjectMapper) : AttributeConverter<List<LogMessage>, String> {

    override fun convertToDatabaseColumn(t: List<LogMessage>): String {
        return objectMapper
            // This is required due to type erasure definition in the Java specification
            .writerFor(object : TypeReference<List<LogMessage>>() {})
            .writeValueAsString(t)
    }

    override fun convertToEntityAttribute(json: String): List<LogMessage> {
        return objectMapper.readValue(json, object : TypeReference<List<LogMessage>>() {})
    }
}
