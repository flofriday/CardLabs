package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class ContainLowerAndUppercaseRule(private var inputName: String?) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
    }

    override fun validate(field: String) {
        if (field.none { it.isLowerCase() } || field.none { it.isUpperCase() }) {
            throw ValidationException("The $inputName needs to contain at least one lower- and uppercase character")
        }
    }
}
