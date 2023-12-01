package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class ContainSpecialCharacterRule(private var inputName: String?) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
    }

    override fun validate(field: String) {
        if (field.none { !it.isLetterOrDigit() }) {
            throw ValidationException("The input $inputName needs to contain at least one special character")
        }
    }
}
