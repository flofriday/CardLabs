package at.tuwien.ase.cardlabs.management.validation.validator

import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class Validator {

    companion object {

        fun <T> validate(t: T, rules: List<ValidationRule<T>>) {
            for (rule in rules) {
                rule.validate(t)
            }
        }

        fun <T> validate(t: T, vararg rules: ValidationRule<T>) {
            for (rule in rules) {
                rule.validate(t)
            }
        }
    }
}
