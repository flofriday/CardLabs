package at.tuwien.ase.cardlabs.management.service.bot

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.config.BotConfig
import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotCreate
import at.tuwien.ase.cardlabs.management.controller.model.bot.BotPatch
import at.tuwien.ase.cardlabs.management.controller.model.bot.TestBot
import at.tuwien.ase.cardlabs.management.database.model.bot.BotCodeDAO
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import at.tuwien.ase.cardlabs.management.database.repository.BotCodeRepository
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.error.ValidationException
import at.tuwien.ase.cardlabs.management.error.account.AccountDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.bot.BotDoesNotExistException
import at.tuwien.ase.cardlabs.management.error.bot.BotStateException
import at.tuwien.ase.cardlabs.management.error.matchmaking.InsufficientBotExistsException
import at.tuwien.ase.cardlabs.management.mapper.BotMapper
import at.tuwien.ase.cardlabs.management.matchmaker.Matchmaker
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.AccountService
import at.tuwien.ase.cardlabs.management.util.Region
import at.tuwien.ase.cardlabs.management.validation.validator.BotValidator
import at.tuwien.ase.cardlabs.management.validation.validator.Validator
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import kotlin.jvm.Throws
import kotlin.streams.toList

@Service
class BotService(
    private val botRepository: BotRepository,
    private val botMapper: BotMapper,
    private val accountService: AccountService,
    private val botNameGenerator: BotNameGenerator,
    private val botCodeRepository: BotCodeRepository,
    private val botConfig: BotConfig,
    private val matchmakerConfig: MatchmakerConfig,
    @Lazy private val matchmaker: Matchmaker,
) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    private var testsBots: List<TestBot>? = null

    /**
     * Generate a bot name
     */
    fun generateBotName(user: CardLabUser): String {
        logger.debug("User ${user.id} attempts to generate a bot name")
        return botNameGenerator.generateBotName()
    }

    /**
     * Fetch all test bots
     */
    fun fetchAllTestsBots(user: CardLabUser): List<TestBot> {
        logger.debug("User ${user.id} attempts to fetch all tests bots")
        if (testsBots == null) {
            testsBots = Helper.fetchAllTestsBots()
        }
        return testsBots!!
    }

    /**
     * Test a test bot by its id
     */
    @Throws(BotDoesNotExistException::class)
    fun fetchTestBotById(testBotId: Long): TestBot {
        if (testsBots == null) {
            testsBots = Helper.fetchAllTestsBots()
        }
        for (bot in testsBots!!) {
            if (bot.id == testBotId) {
                return bot
            }
        }
        throw BotDoesNotExistException("The test bot $testBotId does not exist")
    }

    /**
     * Create a bot
     */
    @Transactional
    @Throws(AccountDoesNotExistException::class)
    fun create(
        user: CardLabUser,
        botCreate: BotCreate,
    ): Bot {
        logger.debug("User ${user.id} attempts to create a bot with the name ${botCreate.name}")
        val owner =
            accountService.findById(user.id)
                ?: throw AccountDoesNotExistException("An account with the id ${user.id} doesn't exist")

        BotValidator.validate(botCreate)

        val bot = BotDAO()
        bot.name = botCreate.name
        bot.owner = owner
        bot.currentCode = botCreate.currentCode ?: ""
        bot.codeHistory = mutableListOf()
        bot.eloScore = botConfig.elo.initialValue
        bot.currentState = BotState.CREATED
        bot.defaultState = BotState.READY

        return botMapper.map(botRepository.save(bot))
    }

    /**
     * Update a bot
     */
    @Transactional
    @Throws(
        BotDoesNotExistException::class,
        UnauthorizedException::class,
    )
    fun patch(
        user: CardLabUser,
        botId: Long,
        botPatch: BotPatch,
    ): Bot {
        logger.debug("User ${user.id} attempts to patch the bot $botId")
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")
        if (bot.owner.id != user.id) {
            throw UnauthorizedException("Can't update a bot not belonging to you")
        }

        BotValidator.validate(botPatch)

        botPatch.currentCode?.let { bot.currentCode = it }
        bot.codeUpdated = Instant.now()

        return botMapper.map(bot)
    }

    /**
     * Create a new version of the current code which is used when a bot is playing a game
     */
    @Transactional
    @Throws(
        BotDoesNotExistException::class,
        UnauthorizedException::class,
        BotStateException::class,
        ValidationException::class,
    )
    fun createCodeVersion(
        user: CardLabUser,
        botId: Long,
    ) {
        logger.debug("User ${user.id} attempts create code version for bot $botId")
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")
        if (bot.owner.id != user.id) {
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

        bot.codeHistory.add(botCodeDAO)
        bot.currentState = BotState.QUEUED
    }

    @Transactional(noRollbackFor = [InsufficientBotExistsException::class])
    @Throws(InsufficientBotExistsException::class)
    fun rank(
        user: CardLabUser,
        botId: Long,
    ) {
        logger.debug("User ${user.id} attempts to rank the bot $botId")
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        if (bot.owner.id != user.id) {
            throw UnauthorizedException("You are not authorized to view the bot $botId")
        }

        logger.debug("Create new code version for $botId")
        var botCodeDAO = BotCodeDAO()
        botCodeDAO.bot = bot
        botCodeDAO.code = bot.currentCode
        botCodeDAO = botCodeRepository.save(botCodeDAO)
        bot.codeHistory.add(botCodeDAO)

        if (matchmakerConfig.matchOnCode.enabled) {
            logger.debug("Attempting to create ${matchmakerConfig.matchOnCode.generateMatchCount} matches for $botId")
            matchmaker.createMatches(bot, matchmakerConfig.matchOnCode.generateMatchCount)
        }
    }

    /**
     * Fetch a bot by its id
     */
    @Transactional
    @Throws(
        BotDoesNotExistException::class,
        UnauthorizedException::class,
    )
    fun fetch(
        user: CardLabUser,
        botId: Long,
    ): Bot {
        logger.debug("User ${user.id} attempts to fetch the bot $botId")
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        if (bot.owner.id != user.id) {
            throw UnauthorizedException("You are not authorized to view the bot $botId")
        }

        return botMapper.map(bot)
    }

    /**
     * Fetch all bots by user
     */
    @Transactional
    fun fetchAll(
        user: CardLabUser,
        pageable: Pageable,
    ): Page<Bot> {
        logger.debug("User ${user.id} attempts to fetch all its bots (pageNumber=${pageable.pageNumber}, pageSize=${pageable.pageSize})")
        return botRepository.findByOwnerIdAndDeletedIsNull(user.id, pageable)
            .map(botMapper::map)
    }

    /**
     * Fetches all the bot IDs of a user
     */
    fun fetchAllBotIds(user: CardLabUser): List<Long> {
        return botRepository.findBotIdsByOwnerIdAndDeletedIsNull(user.id).toList()
    }

    /**
     * Delete a bot by its id
     */
    @Transactional
    @Throws(
        BotDoesNotExistException::class,
        UnauthorizedException::class,
    )
    fun delete(
        user: CardLabUser,
        botId: Long,
    ) {
        logger.debug("User ${user.id} attempts to delete the bot $botId")
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        if (bot.owner.id != user.id) {
            throw UnauthorizedException("Can't delete a bot not belonging to you")
        }

        bot.deleted = Instant.now()
    }

    /**
     * Fetch the current rank position of a bot
     */
    @Transactional
    @Throws(BotDoesNotExistException::class)
    fun fetchRankPosition(
        user: CardLabUser,
        botId: Long,
        region: Region,
    ): Long {
        logger.debug("User ${user.id} attempts to fetch global bot rank for bot $botId")
        findById(botId)
            ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        val result = when (region) {
            Region.GLOBAL -> botRepository.findBotRankPosition(botId)
            Region.CONTINENT -> botRepository.findBotRankPositionContinent(botId)
            Region.COUNTRY -> botRepository.findBotRankPositionCountry(botId)
            else -> throw UnsupportedOperationException("The fetch rank position operation is currently not supported for the region $region")
        }
        return result
    }

    /**
     * Fetch all bots by a given state
     */
    @Transactional
    fun fetchByState(botState: BotState): List<Bot> {
        logger.debug("Attempting to fetch all bots with the state $botState")
        return botRepository.findByCurrentStateAndDeletedIsNull(botState)
            .map(botMapper::map)
            .toList()
    }

    /**
     * Update the state of multiple bots to a given state
     */
    @Transactional
    fun updateMultipleBotState(
        botIds: List<Long>,
        newState: BotState,
    ): Int {
        logger.debug("Attempting to update the state for the bots $botIds to $newState")
        return botRepository.updateMultipleBotState(botIds, newState)
    }

    /**
     * Set the bots to there default state
     */
    @Transactional
    fun setBotStateToDefaultState(botIds: List<Long>) {
        logger.debug("Attempting to set the bot state to the default state for the bots $botIds")
        for (bot in botRepository.findAllByIdInAndDeletedIsNull(botIds)) {
            bot.currentState = bot.defaultState
        }
    }

    /**
     * Return if a bot is owned by a user
     */
    @Transactional
    @Throws(BotDoesNotExistException::class)
    fun isBotOwnedByUser(
        botId: Long,
        user: CardLabUser,
    ): Boolean {
        val bot =
            findById(botId)
                ?: throw BotDoesNotExistException("A bot with the id $botId doesn't exist")

        return bot.owner.id == user.id
    }

    private fun findById(botId: Long): BotDAO? {
        return botRepository.findByIdAndDeletedIsNull(botId)
    }

    // Assume that botCodeId is set for every item
    private fun getLatestCodeVersion(codeHistory: MutableList<BotCodeDAO>): BotCodeDAO? {
        return codeHistory.maxByOrNull { it.id!! }
    }
}
