package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class LengthRule(private val min: Int, private val max: Int) : ValidationRule<String> {

    override fun validate(field: String) {
        if (field.length !in min..max) {
            throw ValidationException("The input must have length [$min, $max]")
        }
    }
}
