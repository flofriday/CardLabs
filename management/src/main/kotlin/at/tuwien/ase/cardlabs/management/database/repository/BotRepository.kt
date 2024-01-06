package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
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

    fun findByIdAndDeletedIsNull(id: Long): BotDAO?

    @Query(
        """
        SELECT b
            FROM BotDAO b
            WHERE b.owner.id = :ownerId AND b.deleted = NULL 
        """,
    )
    fun findByOwnerIdAndDeletedIsNull(@Param("ownerId") ownerId: Long, pageable: Pageable): Page<BotDAO>

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b 
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId)
        """,
    )
    fun findBotRankPosition(@Param("botId") botId: Long): Long

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b LEFT JOIN b.owner a
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId) AND a.location = (SELECT a3.location FROM BotDAO b3 LEFT JOIN b3.owner a3 WHERE b3.id = :botId)
        """,
    )
    fun findBotRankPositionCountry(@Param("botId") botId: Long): Long

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b LEFT JOIN b.owner a LEFT JOIN a.location l
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId) AND l.continent = (SELECT l3.continent FROM BotDAO b3 LEFT JOIN b3.owner a3 LEFT JOIN a3.location l3 WHERE b3.id = :botId)
        """,
    )
    fun findBotRankPositionContinent(@Param("botId") botId: Long): Long
}
