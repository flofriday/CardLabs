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

class AccountValidator {

    companion object {

        /**
         * Checks if a BotCreate instance violates any rule
         *
         * @throws ValidationException if any rule is violated
         */
        fun validateAccountCreate(account: Account) {
            Validator.validate(
                account.username,
                usernameValidationRules()
            )
            Validator.validate(
                account.email,
                IsEmailRule("email")
            )
            Validator.validate(
                account.password,
                passwordValidationRules()
            )
        }

        fun usernameValidationRules(): List<ValidationRule<String>> {
            val inputName = "username"
            return mutableListOf(
                NotEmptyRule(inputName),
                LengthRule(inputName, 4, 32),
                NotContainWhitespaceRule(inputName)
            )
        }

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
