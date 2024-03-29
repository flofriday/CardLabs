package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

/**
 * A rule that checks if the given string contains at least one digit
 */
class ContainDigitRule(private var inputName: String?) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
    }

    override fun validate(field: String) {
        if (field.none { it.isDigit() }) {
            throw ValidationException("The $inputName needs to contain at least one digit")
        }
    }
}
