package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.Bot
import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import org.springframework.stereotype.Component

@Component
class BotMapper {

    fun map(bot: Bot): BotDAO {
        val botDAO = BotDAO()
        botDAO.botId = bot.id
        botDAO.name = bot.name
        // botDAO.owner
        botDAO.code = bot.code
        botDAO.eloScore = bot.eloScore
        botDAO.currentState = bot.currentState
        botDAO.defaultState = bot.currentState
        return botDAO
    }

    fun map(botDAO: BotDAO): Bot {
        return Bot(
            id = botDAO.botId,
            name = botDAO.name,
            // Normally the case where owner.id is not set never occurs as the id is automatically generated when
            // inserting into the database
            ownerId = botDAO.owner.accountId ?: -1,
            code = botDAO.code,
            eloScore = botDAO.eloScore,
            currentState = botDAO.currentState,
            defaultState = botDAO.defaultState,
            errorStateMessage = botDAO.errorStateMessage
        )
    }
}
