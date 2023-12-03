package at.tuwien.ase.cardlabs.management.ut.validator

import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.validator.BotValidator
import at.tuwien.ase.cardlabs.management.validation.validator.Validator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BotValidatorTests {

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/bot_validator_create_test_parameter.csv"], numLinesToSkip = 1)
    fun testBotCreateValidator(
        name: String,
        currentCode: String,
        success: Boolean,
        errorMessage: String,
        description: String
    ) {
        val botCreate = BotCreate(
            name = name,
            currentCode = currentCode
        )
        if (success) {
            assertDoesNotThrow {
                BotValidator.validate(botCreate)
            }
        } else {
            val exception = assertThrows<ValidationException> {
                BotValidator.validate(botCreate)
            }
            Assertions.assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/bot_validator_patch_test_parameter.csv"], numLinesToSkip = 1)
    fun testBotPatchValidator(currentCode: String, success: Boolean, errorMessage: String, description: String) {
        val botPatch = BotPatch(
            currentCode = currentCode
        )
        if (success) {
            assertDoesNotThrow {
                BotValidator.validate(botPatch)
            }
        } else {
            val exception = assertThrows<ValidationException> {
                BotValidator.validate(botPatch)
            }
            Assertions.assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/bot_validator_rules_name_test_parameter.csv"], numLinesToSkip = 1)
    fun testNameValidator(name: String, success: Boolean, errorMessage: String, description: String) {
        if (success) {
            assertDoesNotThrow {
                Validator.validate(name, BotValidator.nameValidationRules())
            }
        } else {
            val exception = assertThrows<ValidationException> {
                Validator.validate(name, BotValidator.nameValidationRules())
            }
            Assertions.assertEquals(errorMessage, exception.message)
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/validator/bot_validator_rules_code_test_parameter.csv"], numLinesToSkip = 1)
    fun testCodeValidator(code: String, success: Boolean, errorMessage: String, description: String) {
        if (success) {
            assertDoesNotThrow {
                Validator.validate(code, BotValidator.codeValidationRules())
            }
        } else {
            val exception = assertThrows<ValidationException> {
                Validator.validate(code, BotValidator.codeValidationRules())
            }
            Assertions.assertEquals(errorMessage, exception.message)
        }
    }
}
