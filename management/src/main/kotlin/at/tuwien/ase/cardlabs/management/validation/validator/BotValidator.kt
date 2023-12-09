package at.tuwien.ase.cardlabs.management.validation.validator

import at.tuwien.ase.cardlabs.management.ApplicationContextProvider
import at.tuwien.ase.cardlabs.management.config.BotConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule
import at.tuwien.ase.cardlabs.management.validation.string.BotNameRule
import at.tuwien.ase.cardlabs.management.validation.string.LengthRule
import at.tuwien.ase.cardlabs.management.validation.string.NotEmptyRule

/**
 * Data validation methods for bot operations
 */
class BotValidator {

    companion object {

        /**
         * Checks if a BotCreate instance violates any rule
         *
         * @throws ValidationException if any rule is violated
         */
        @JvmStatic
        fun validate(botCreate: BotCreate) {
            Validator.validate(
                botCreate.name,
                nameValidationRules(),
            )
            botCreate.currentCode?.let {
                Validator.validate(
                    it,
                    codeValidationRules(),
                )
            }
        }

        /**
         * Checks if a BotUpdate instance violates any rule
         *
         * @throws ValidationException if any rule is violated
         */
        @JvmStatic
        fun validate(botPatch: BotPatch) {
            botPatch.currentCode?.let {
                Validator.validate(
                    it,
                    codeValidationRules(),
                )
            }
        }

        /**
         * The validation rules that are used to validate a name
         *
         * @return the validation rules
         */
        @JvmStatic
        fun nameValidationRules(): List<ValidationRule<String>> {
            val inputName = "name"
            val botConfig = ApplicationContextProvider.getBean(BotConfig::class.java)
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 5, 30),
                BotNameRule(inputName, botConfig),
            )
        }

        /**
         * The validation rules that are used to validate code
         *
         * @return the validation rules
         */
        @JvmStatic
        fun codeValidationRules(): List<ValidationRule<String>> {
            val inputName = "code"
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 1, 32768),
            )
        }
    }
}
