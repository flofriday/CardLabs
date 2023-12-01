package at.tuwien.ase.cardlabs.management.ut

import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.validation.validator.BotValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class BotValidatorTests {

    @Test
    fun whenValidateBotCreate_withNameSet_expectSuccess() {
        assertDoesNotThrow {
            val botCreate = BotCreate(
                name = "Xennixos"
            )
            BotValidator.validate(botCreate)
        }
    }

    @Test
    fun whenValidateBotCreate_withEmptyName_expectValidationError() {
        val exception = assertThrows<ValidationException> {
            val botCreate = BotCreate(
                name = ""
            )
            BotValidator.validate(botCreate)
        }

        assertEquals("The name can not be empty or blank", exception.message)
    }

    @Test
    fun whenValidateBotCreate_withToShortName_expectValidationError() {
        val exception = assertThrows<ValidationException> {
            val botCreate = BotCreate(
                name = "Xenn"
            )
            BotValidator.validate(botCreate)
        }

        assertEquals("The name must have length [5, 30]", exception.message)
    }

    @Test
    fun whenValidateBotUpdate_withNoDataSet_expectSuccess() {
        assertDoesNotThrow {
            BotValidator.validate(BotPatch())
        }
    }

    @Test
    fun whenValidateBotUpdate_withCodeSetEmpty_expectValidationError() {
        val exception = assertThrows<ValidationException> {
            val botPatch = BotPatch(
                currentCode = ""
            )
            BotValidator.validate(botPatch)
        }

        assertEquals("The code can not be empty or blank", exception.message)
    }

    @Test
    fun whenValidateBotUpdate_withCodeSet_expectSuccess() {
        assertDoesNotThrow {
            val botPatch = BotPatch(
                currentCode = "a"
            )
            BotValidator.validate(botPatch)
        }
    }
}
