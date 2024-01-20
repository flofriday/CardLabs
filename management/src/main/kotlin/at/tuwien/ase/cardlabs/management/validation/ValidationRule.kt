package at.tuwien.ase.cardlabs.management.validation

import at.tuwien.ase.cardlabs.management.error.ValidationException
import kotlin.jvm.Throws

/**
 * An interface representing the validation rule definition
 */
interface ValidationRule<T> {

    /**
     * Checks if the provided data does not violate the validation rule. If the rule is violated then an exception is
     * thrown otherwise it terminates error-free
     *
     * @throws ValidationException if the rule is violated
     */
    @Throws(ValidationException::class)
    fun validate(field: T)
}
