package at.tuwien.ase.cardlabs.management.database.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Converts an Instant into a LocalDateTime and vice versa. This is used when saving data to the database (it is automatically applied to all Instant fields).
 * <p>
 * Note: this is a workaround for a time conversion bug where despite Instant being UTC, hibernate doesn't handle it appropriately and nonetheless still converts it
 */
@Converter(autoApply = true)
class InstantConverter : AttributeConverter<Instant, LocalDateTime> {

    override fun convertToDatabaseColumn(instant: Instant?): LocalDateTime? {
        return instant?.atZone(ZoneId.systemDefault())?.toLocalDateTime()
    }

    override fun convertToEntityAttribute(localDateTime: LocalDateTime?): Instant? {
        return localDateTime?.toInstant(ZoneOffset.UTC)
    }
}
