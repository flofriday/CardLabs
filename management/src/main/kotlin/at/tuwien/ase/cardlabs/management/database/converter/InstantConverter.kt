package at.tuwien.ase.cardlabs.management.database.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Converter(autoApply = true)
class InstantConverter : AttributeConverter<Instant, LocalDateTime> {

    override fun convertToDatabaseColumn(instant: Instant?): LocalDateTime? {
        return instant?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    }

    override fun convertToEntityAttribute(localDateTime: LocalDateTime?): Instant? {
        return localDateTime?.toInstant(ZoneOffset.UTC)
    }
}
