package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.config.BotConfig
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class BotNameRule(private var inputName: String?, private val botConfig: BotConfig) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
        botConfig.name.generator.syllables.first
    }

    override fun validate(field: String) {
        val errorMessage = "The $inputName needs to be a bot name"

        val prefixLength =
            botConfig.name.generator.syllables.first.find { field.startsWith(it) }?.length
                ?: throw ValidationException(errorMessage)
        val suffixLength =
            botConfig.name.generator.syllables.last.find { field.endsWith(it) }?.length
                ?: throw ValidationException(errorMessage)

        // Extract the middle part
        val middlePart = field.substring(prefixLength, field.length - suffixLength)

        // Check if the middle part is valid
        if (!botConfig.name.generator.syllables.middle.contains(middlePart)) {
            throw ValidationException(errorMessage)
        }
    }
}
