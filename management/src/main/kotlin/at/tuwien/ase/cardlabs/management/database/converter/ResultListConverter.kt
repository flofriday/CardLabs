package at.tuwien.ase.cardlabs.management.database.converter

import at.tuwien.ase.cardlabs.management.database.model.match.result.Result
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter

class ResultListConverter(private val objectMapper: ObjectMapper) : AttributeConverter<List<Result>, String> {

    override fun convertToDatabaseColumn(t: List<Result>): String {
        return objectMapper
            // This is required due to type erasure definition in the Java specification
            .writerFor(object : TypeReference<List<Result>>() {})
            .writeValueAsString(t)
    }

    override fun convertToEntityAttribute(json: String): List<Result> {
        return objectMapper.readValue(json, object : TypeReference<List<Result>>() {})
    }
}
