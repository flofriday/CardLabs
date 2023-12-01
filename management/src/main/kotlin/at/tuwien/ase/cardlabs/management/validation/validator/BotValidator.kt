package at.tuwien.ase.cardlabs.management.validation.validator

import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule
import at.tuwien.ase.cardlabs.management.validation.string.LengthRule
import at.tuwien.ase.cardlabs.management.validation.string.NotEmptyRule

class BotValidator {

    companion object {

        /**
         * Checks if a BotCreate instance violates any rule
         *
         * @throws ValidationException if any rule is violated
         */
        fun validate(botCreate: BotCreate) {
            Validator.validate(
                botCreate.name,
                NotEmptyRule("name"),
                LengthRule("name", 5, 30)
            )
            botCreate.currentCode?.let {
                Validator.validate(
                    it,
                    codeValidationRules()
                )
            }
        }

        /**
         * Checks if a BotUpdate instance violates any rule
         *
         * @throws ValidationException if any rule is violated
         */
        fun validate(botPatch: BotPatch) {
            botPatch.currentCode?.let {
                Validator.validate(
                    it,
                    codeValidationRules()
                )
            }
        }

        fun codeValidationRules(): List<ValidationRule<String>> {
            val inputName = "code"
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 1, 32768)
            )
        }
    }
}
