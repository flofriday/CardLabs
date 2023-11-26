package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.Helper
import at.tuwien.ase.cardlabs.management.controller.model.Bot
import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.BotState
import at.tuwien.ase.cardlabs.management.database.repository.BotRepository
import at.tuwien.ase.cardlabs.management.error.AccountDoesNotExistsException
import at.tuwien.ase.cardlabs.management.error.UnauthorizedException
import at.tuwien.ase.cardlabs.management.mapper.BotMapper
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class BotService(
    private val botRepository: BotRepository,
    private val botMapper: BotMapper,
    private val accountService: AccountService,
) {

    companion object {

        const val INITIAL_ELO_VALUE: Int = 0
    }

    @Transactional
    fun create(bot: Bot): Bot {
        Helper.requireNull(bot.id, "The id must not be set")
        Helper.requireNonNull(bot.name, "The name must be set")
        Helper.requireNonNull(bot.ownerId, "The owner must be set")
        val owner = accountService.findById(bot.ownerId)
        if (owner != null) {
            throw AccountDoesNotExistsException("An account with the id ${bot.ownerId} doesn't exist")
        }

        val b = BotDAO()
        b.name = bot.name
        b.owner = owner!!
        b.code = bot.code
        b.eloScore = INITIAL_ELO_VALUE
        b.currentState = BotState.CREATED
        b.defaultState = BotState.CREATED
        return botMapper.map(botRepository.save(b))
    }

    @Transactional
    fun delete(user: CardLabUser, id: Long) {
        Helper.requireNonNull(user, "No authentication provided")
        Helper.requireNonNull(id, "Cannot delete a bot with the id null")
        var botDAO: BotDAO? = findById(id)
        // TODO: check ir owner is present and if id is present
        if (user.id != botDAO!!.owner.accountId) {
            throw UnauthorizedException("Can't delete someone elses bot")
        }

        botDAO.deleted = Instant.now()
    }

    fun findById(id: Long): BotDAO? {
        return botRepository.findByBotIdAndDeletedIsNull(id)
    }
}
