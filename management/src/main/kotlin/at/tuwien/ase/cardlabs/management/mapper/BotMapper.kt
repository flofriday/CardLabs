package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCode
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import org.springframework.stereotype.Component

@Component
class BotMapper(private val botCodeMapper: BotCodeMapper) {

    fun map(bot: Bot): BotDAO {
        val botDAO = BotDAO()
        botDAO.id = bot.id
        botDAO.name = bot.name
        botDAO.ownerId = bot.ownerId
        botDAO.currentCode = bot.currentCode
        botDAO.eloScore = bot.eloScore
        botDAO.currentState = bot.currentState
        botDAO.defaultState = bot.currentState
        return botDAO
    }

    fun map(botDAO: BotDAO): Bot {
        val codeHistory = mutableListOf<BotCode>()
        for (code in botDAO.codeHistory) {
            codeHistory.add(botCodeMapper.map(code))
        }

        return Bot(
            id = botDAO.id,
            name = botDAO.name,
            // Normally the case where owner.id is not set never occurs as the id is automatically generated when
            // inserting into the database
            ownerId = botDAO.owner.id ?: -1,
            currentCode = botDAO.currentCode,
            codeHistory = codeHistory,
            eloScore = botDAO.eloScore,
            currentState = botDAO.currentState,
            defaultState = botDAO.defaultState,
            created = botDAO.created,
            updated = botDAO.codeUpdated,
            errorStateMessage = botDAO.errorStateMessage,
        )
    }
}
