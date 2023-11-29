package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class NotEmptyRule : ValidationRule<String> {

    override fun validate(field: String) {
        if (field.isEmpty() || field.isBlank()) {
            throw ValidationException("The input can not be empty or blank")
        }
    }
}
