package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

/**
 * A rule that checks if the given string is an email
 */
class IsEmailRule(private var inputName: String?) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
    }

    override fun validate(field: String) {
        if (!field.matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])".toRegex())) {
            throw ValidationException("The $inputName must be a valid email address")
        }
    }
}
