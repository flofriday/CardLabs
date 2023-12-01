package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.BotDAO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Bot entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface BotRepository : CrudRepository<BotDAO?, Long?> {

    fun findByBotIdAndDeletedIsNull(id: Long): BotDAO?

    @Query(
        """
        SELECT b
            FROM BotDAO b
            WHERE b.owner.accountId = :ownerId
        """
    )
    fun findByOwnerIdAndDeletedIsNull(@Param("ownerId") ownerId: Long, pageable: Pageable): Page<BotDAO>

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b 
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.botId = :botId)
        """
    )
    fun findBotRankPosition(@Param("botId") botId: Long): Long
}
