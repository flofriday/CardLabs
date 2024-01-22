package at.tuwien.ase.cardlabs.management.validation.validator

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.ValidationRule
import at.tuwien.ase.cardlabs.management.validation.string.ContainDigitRule
import at.tuwien.ase.cardlabs.management.validation.string.ContainLowerAndUppercaseRule
import at.tuwien.ase.cardlabs.management.validation.string.ContainSpecialCharacterRule
import at.tuwien.ase.cardlabs.management.validation.string.IsEmailRule
import at.tuwien.ase.cardlabs.management.validation.string.LengthRule
import at.tuwien.ase.cardlabs.management.validation.string.NotContainWhitespaceRule
import at.tuwien.ase.cardlabs.management.validation.string.NotEmptyRule

/**
 * Data validation methods for account operations
 */
class AccountValidator {

    companion object {

        /**
         * Checks if a Account instance violates any rule in the context of account creation
         *
         * @throws ValidationException if any rule is violated
         */
        @JvmStatic
        fun validateAccountCreate(account: Account) {
            Validator.validate(
                account.username,
                usernameValidationRules()
            )
            Validator.validate(
                account.email,
                emailValidationRules()
            )
        }

        /**
         * The validation rules that are used to validate a username
         *
         * @return the validation rules
         */
        @JvmStatic
        fun usernameValidationRules(): List<ValidationRule<String>> {
            val inputName = "username"
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 4, 32),
                NotContainWhitespaceRule(inputName)
            )
        }

        /**
         * The validation rules that are used to validate an email
         *
         * @return the validation rules
         */
        @JvmStatic
        fun emailValidationRules(): List<ValidationRule<String>> {
            val inputName = "email"
            return mutableListOf(
                IsEmailRule(inputName)
            )
        }

        /**
         * The validation rules that are used to validate a password
         *
         * @return the validation rules
         */
        @JvmStatic
        fun passwordValidationRules(): List<ValidationRule<String>> {
            val inputName = "password"
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 8, 64),
                NotContainWhitespaceRule(inputName),
                ContainDigitRule(inputName),
                ContainLowerAndUppercaseRule(inputName),
                ContainSpecialCharacterRule(inputName)
            )
        }
    }
}
