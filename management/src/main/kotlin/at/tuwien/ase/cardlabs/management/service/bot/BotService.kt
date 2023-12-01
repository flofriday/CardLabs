package at.tuwien.ase.cardlabs.management.service.bot

import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.database.model.BotCodeDAO
import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.BotState
import at.tuwien.ase.cardlabs.management.database.repository.BotCodeRepository
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.error.AccountDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.BotDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.BotStateException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.mapper.BotMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.validation.validator.BotValidator
import at.tuwien.ase.cardlabs.management.validation.validator.Validator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class BotService(
    private val botRepository: BotRepository,
    private val botMapper: BotMapper,
    private val accountService: AccountService,
    private val botNameGenerator: BotNameGenerator,
    private val botCodeRepository: BotCodeRepository
) {

    companion object {

        const val INITIAL_ELO_VALUE: Int = 1000
    }

    fun generateBotName(): String {
        return botNameGenerator.generateBotName()
    }

    @Transactional
    fun create(user: CardLabUser, botCreate: BotCreate): Bot {
        val owner = accountService.findById(user.id)
            ?: throw AccountDoesNotExistException("An account with the id ${user.id} doesn't exist")

        BotValidator.validate(botCreate)

        val bot = BotDAO()
        bot.name = botCreate.name
        bot.owner = owner
        bot.currentCode = botCreate.currentCode ?: ""
        bot.codeHistory = mutableListOf()
        bot.eloScore = INITIAL_ELO_VALUE
        bot.currentState = BotState.CREATED
        bot.defaultState = BotState.READY

        return botMapper.map(botRepository.save(bot))
    }

    @Transactional
    fun patch(user: CardLabUser, botId: Long, botPatch: BotPatch): Bot {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")
        if (bot.owner.accountId != user.id) {
            throw UnauthorizedException("Can't update a bot not belonging to you")
        }

        BotValidator.validate(botPatch)

        botPatch.currentCode?.let { bot.currentCode = it }

        return botMapper.map(bot)
    }

    @Transactional
    fun rank(user: CardLabUser, botId: Long) {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")
        if (bot.owner.accountId != user.id) {
            throw UnauthorizedException("Can't rank a bot not belonging to you")
        }

        if (!(bot.currentState == BotState.CREATED || bot.currentState == BotState.READY)) {
            throw BotStateException("In order to rank a bot it must either be in the state CREATED or READY")
        }

        Validator.validate(bot.currentCode, BotValidator.codeValidationRules())

        val latest = getLatestCodeVersion(bot.codeHistory)
        latest?.let {
            if (bot.currentCode == it.code) {
                throw ValidationException("Can't re-rank a bot with the same code as it is currently ranked with")
            }
        }

        var botCodeDAO = BotCodeDAO()
        botCodeDAO.bot = bot
        botCodeDAO.code = bot.currentCode
        botCodeDAO = botCodeRepository.save(botCodeDAO)

        bot.currentState = BotState.READY
        bot.codeHistory.add(botCodeDAO)
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
    fun fetchAll(user: CardLabUser, pageable: Pageable): Page<Bot> {
        return botRepository.findByOwnerIdAndDeletedIsNull(user.id, pageable)
            .map(botMapper::map)
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

    @Transactional
    fun rankPosition(user: CardLabUser, botId: Long): Long {
        val bot = findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        return botRepository.findBotRankPosition(botId)
    }

    private fun findById(botId: Long): BotDAO? {
        return botRepository.findByBotIdAndDeletedIsNull(botId)
    }

    // Assume that botCodeId is set for every item
    private fun getLatestCodeVersion(codeHistory: MutableList<BotCodeDAO>): BotCodeDAO? {
        return codeHistory.maxByOrNull { it.botCodeId!! }
    }
}
