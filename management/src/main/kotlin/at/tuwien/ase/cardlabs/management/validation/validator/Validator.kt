package at.tuwien.ase.cardlabs.management.validation.validator

import at.tuwien.ase.cardlabs.management.validation.ValidationRule

/**
 * Helper methods for performing validations
 */
class Validator {

    companion object {

        /**
         * Validates all given rules against a given input
         */
        @JvmStatic
        fun <T> validate(input: T, rules: List<ValidationRule<T>>) {
            for (rule in rules) {
                rule.validate(input)
            }
        }

        /**
         * Validates all given rules against a given input
         */
        @JvmStatic
        fun <T> validate(input: T, vararg rules: ValidationRule<T>) {
            for (rule in rules) {
                rule.validate(input)
            }
        }
    }
}
