package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.bot.BotCodeDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Bot entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface BotCodeRepository : CrudRepository<BotCodeDAO?, Long?> {

    fun findByIdAndDeletedIsNull(id: Long): BotCodeDAO?
}
