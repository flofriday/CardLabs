package at.tuwien.ase.cardlabs.management.validation.string

import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule

class LengthRule(private var inputName: String?, private val min: Int, private val max: Int) : ValidationRule<String> {

    init {
        if (inputName == null) {
            inputName = "input"
        }
        if (min < 0) {
            throw IllegalArgumentException("The min length for the LengthRule must be > 0")
        }
        if (max < min) {
            throw IllegalArgumentException("The max length for the LengthRule must be >= min")
        }
    }

    override fun validate(field: String) {
        if (field.length !in min..max) {
            throw ValidationException("The $inputName must have length [$min, $max]")
        }
    }
}
