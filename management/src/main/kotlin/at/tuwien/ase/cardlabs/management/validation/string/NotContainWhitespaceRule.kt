package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

/**
 * A rule that checks if the given string does not contain a whitespace character
 */
class NotContainWhitespaceRule(private var inputName: String?) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
    }

    override fun validate(field: String) {
        if (field.any { it.isWhitespace() }) {
            throw ValidationException("The $inputName can not contain a whitespace")
        }
    }
}
