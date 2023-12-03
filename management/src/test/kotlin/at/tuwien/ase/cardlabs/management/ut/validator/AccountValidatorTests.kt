package at.tuwien.ase.cardlabs.management.ut.validator

import at.tuwien.ase.cardlabs.management.controller.model.account.Account
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.validator.AccountValidator
import at.tuwien.ase.cardlabs.management.validation.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class AccountValidatorTests {

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/account_validator_test_parameter.csv"], numLinesToSkip = 1)
    fun testAccountValidator(
        username: String,
        email: String,
        password: String,
        success: Boolean,
        errorMessage: String,
        description: String
    ) {
        val account = Account(
            id = null,
            username = username,
            email = email,
            password = password,
            location = null,
            sendScoreUpdates = false,
            sendChangeUpdates = false,
            sendNewsletter = false
        )
        if (success) {
            assertDoesNotThrow {
                AccountValidator.validateAccountCreate(account)
            }
        } else {
            val exception = assertThrows<ValidationException> {
                AccountValidator.validateAccountCreate(account)
            }
            assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/account_validator_rules_username_test_parameter.csv"], numLinesToSkip = 1)
    fun testUsernameValidator(username: String, success: Boolean, errorMessage: String, description: String) {
        if (success) {
            assertDoesNotThrow {
                Validator.validate(username, AccountValidator.usernameValidationRules())
            }
        } else {
            val exception = assertThrows<ValidationException> {
                Validator.validate(username, AccountValidator.usernameValidationRules())
            }
            assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/account_validator_rules_email_test_parameter.csv"], numLinesToSkip = 1)
    fun testEmailValidator(username: String, success: Boolean, errorMessage: String, description: String) {
        if (success) {
            assertDoesNotThrow {
                Validator.validate(username, AccountValidator.emailValidationRules())
            }
        } else {
            val exception = assertThrows<ValidationException> {
                Validator.validate(username, AccountValidator.emailValidationRules())
            }
            assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/account_validator_rules_password_test_parameter.csv"], numLinesToSkip = 1)
    fun testPasswordValidator(username: String, success: Boolean, errorMessage: String, description: String) {
        if (success) {
            assertDoesNotThrow {
                Validator.validate(username, AccountValidator.passwordValidationRules())
            }
        } else {
            val exception = assertThrows<ValidationException> {
                Validator.validate(username, AccountValidator.passwordValidationRules())
            }
            assertEquals(errorMessage, exception.message)
        }
    }
}
