package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCode
import at.tuwien.ase.cardlabs.management.database.model.BotCodeDAO
import org.springframework.stereotype.Component

@Component
class BotCodeMapper {

    fun map(botCode: BotCode): BotCodeDAO {
        val botCodeDAO = BotCodeDAO()
        botCodeDAO.botCodeId = botCode.id
        botCodeDAO.botId = botCode.botId
        botCodeDAO.code = botCode.code
        return botCodeDAO
    }

    fun map(botCodeDAO: BotCodeDAO): BotCode {
        return BotCode(
            id = botCodeDAO.botCodeId,
            botId = botCodeDAO.botId,
            code = botCodeDAO.code
        )
    }
}
