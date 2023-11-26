package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Account entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface BotRepository : CrudRepository<BotDAO?, Long?> {

    fun findByBotIdAndDeletedIsNull(id: Long): BotDAO?
}
