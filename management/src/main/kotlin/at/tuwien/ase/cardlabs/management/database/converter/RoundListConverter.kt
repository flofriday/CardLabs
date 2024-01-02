package at.tuwien.ase.cardlabs.management.database.converter

import at.tuwien.ase.cardlabs.management.database.model.game.round.Round
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

/**
 * Converts a list of rounds into a JSON or vice versa. This is used when saving data to the database.
 */
class RoundListConverter(private val objectMapper: ObjectMapper) : AttributeConverter<List<Round>, String> {

    override fun convertToDatabaseColumn(t: List<Round>): String {
        return objectMapper
            // This is required due to type erasure definition in the Java specification
            .writerFor(object : TypeReference<List<Round>>() {})
            .writeValueAsString(t)
    }

    override fun convertToEntityAttribute(json: String): List<Round> {
        return objectMapper.readValue(json, object : TypeReference<List<Round>>() {})
    }
}
