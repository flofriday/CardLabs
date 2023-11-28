package at.tuwien.ase.cardlabs.management.service.bot

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotUpdate
import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.BotState
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.error.AccountDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.BotDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.mapper.BotMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class BotService(
    private val botRepository: BotRepository,
    private val botMapper: BotMapper,
    private val accountService: AccountService,
    private val botNameGenerator: BotNameGenerator
) {

    companion object {

        const val INITIAL_ELO_VALUE: Int = 0
    }

    fun generateBotName(): String {
        return botNameGenerator.generateBotName()
    }

    @Transactional
    fun create(user: CardLabUser, botCreate: BotCreate): Bot {
        val owner = accountService.findById(user.id)
            ?: throw AccountDoesNotExistException("An account with the id ${user.id} doesn't exist")

        val bot = BotDAO()
        bot.name = botCreate.name
        bot.owner = owner
        bot.currentCode = botCreate.currentCode
        bot.codeHistory = mutableListOf()
        bot.eloScore = INITIAL_ELO_VALUE
        bot.currentState = BotState.CREATED
        bot.defaultState = BotState.CREATED

        return botMapper.map(botRepository.save(bot))
    }

    @Transactional
    fun patch(user: CardLabUser, botId: Long, botUpdate: BotUpdate): Bot {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        botUpdate.currentCode?.let { bot.currentCode = it }

        return botMapper.map(bot)
    }

    @Transactional
    fun fetch(user: CardLabUser, botId: Long): Bot {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        if (bot.owner.accountId != user.id) {
            throw UnauthorizedException("You are not authorized to view the bot $botId")
        }

        return botMapper.map(bot)
    }

    @Transactional
    fun delete(user: CardLabUser, botId: Long) {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        if (bot.owner.accountId != user.id) {
            throw UnauthorizedException("Can't delete a bot not belonging to you")
        }

        bot.deleted = Instant.now()
    }

    private fun findById(botId: Long): BotDAO? {
        return botRepository.findByBotIdAndDeletedIsNull(botId)
    }
}
