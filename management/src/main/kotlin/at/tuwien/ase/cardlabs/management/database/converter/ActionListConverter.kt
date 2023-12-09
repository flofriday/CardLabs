package at.tuwien.ase.cardlabs.management.database.converter

import at.tuwien.ase.cardlabs.management.database.model.match.action.Action
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

class ActionListConverter(private val objectMapper: ObjectMapper) : AttributeConverter<List<Action>, String> {

    override fun convertToDatabaseColumn(t: List<Action>): String {
        return objectMapper
            // This is required due to type erasure definition in the Java specification
            .writerFor(object : TypeReference<List<Action>>() {})
            .writeValueAsString(t)
    }

    override fun convertToEntityAttribute(json: String): List<Action> {
        return objectMapper.readValue(json, object : TypeReference<List<Action>>() {})
    }
}
