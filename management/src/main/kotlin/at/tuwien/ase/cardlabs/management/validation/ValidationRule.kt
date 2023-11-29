package at.tuwien.ase.cardlabs.management.validation

import at.tuwien.ase.cardlabs.management.error.ValidationException

interface ValidationRule<T> {

    /**
     * Checks if the provided data does not violate the validation rule
     *
     * @throws ValidationException if the rule is violated
     * @return if the validation rule is fulfilled or not
     */
    fun validate(field: T)
}
