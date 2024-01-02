package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCode
import at.tuwien.ase.cardlabs.management.database.model.bot.BotCodeDAO
import org.springframework.stereotype.Component

@Component
class BotCodeMapper {

    fun map(botCode: BotCode): BotCodeDAO {
        val botCodeDAO = BotCodeDAO()
        botCodeDAO.id = botCode.id
        botCodeDAO.botId = botCode.botId
        botCodeDAO.code = botCode.code
        return botCodeDAO
    }

    fun map(botCodeDAO: BotCodeDAO): BotCode {
        return BotCode(
            id = botCodeDAO.id,
            botId = botCodeDAO.botId,
            code = botCodeDAO.code,
        )
    }
}
