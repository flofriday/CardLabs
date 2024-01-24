package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import at.tuwien.ase.cardlabs.management.database.model.bot.BotState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream

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
            ORDER BY b.codeUpdated DESC, b.name
        """,
    )
    fun findByOwnerIdAndDeletedIsNull(
        @Param("ownerId") ownerId: Long,
        pageable: Pageable,
    ): Page<BotDAO>

    @Query(
        """
        SELECT b.id
            FROM BotDAO b
            WHERE b.owner.id = :ownerId AND b.deleted = NULL
        """,
    )
    fun findBotIdsByOwnerIdAndDeletedIsNull(
        @Param("ownerId") ownerId: Long,
    ): Stream<Long>

    fun findAllByIdInAndDeletedIsNull(botsIds: List<Long>): Stream<BotDAO>

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId)
        """,
    )
    fun findBotRankPosition(
        @Param("botId") botId: Long,
    ): Long

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b LEFT JOIN b.owner a
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId) AND a.location = (SELECT a3.location FROM BotDAO b3 LEFT JOIN b3.owner a3 WHERE b3.id = :botId)
        """,
    )
    fun findBotRankPositionCountry(
        @Param("botId") botId: Long,
    ): Long

    @Query(
        """
        SELECT COUNT(b) + 1
            FROM BotDAO b LEFT JOIN b.owner a LEFT JOIN a.location l
            WHERE b.eloScore > (SELECT b2.eloScore FROM BotDAO b2 WHERE b2.id = :botId) AND l.continent = (SELECT l3.continent FROM BotDAO b3 LEFT JOIN b3.owner a3 LEFT JOIN a3.location l3 WHERE b3.id = :botId)
        """,
    )
    fun findBotRankPositionContinent(
        @Param("botId") botId: Long,
    ): Long

    fun findByCurrentStateAndDeletedIsNullAndBannedIsFalse(botState: BotState): Stream<BotDAO>

    @Modifying
    @Transactional
    @Query(
        """
            UPDATE BotDAO b
                SET b.currentState = :newState
                WHERE b.id IN :botIds
        """,
    )
    fun updateMultipleBotState(
        botIds: List<Long>,
        newState: BotState,
    ): Int

    @Query(
        """
            SELECT bot
                FROM BotDAO bot
                    JOIN BotCodeDAO botCode ON bot.id = botCode.bot.id
                WHERE bot.deleted IS NULL AND botCode.deleted IS NULL
                GROUP BY bot.id
                HAVING COUNT(botCode.id) >= 1
        """,
    )
    fun findAllBotsWithAtLeastOneBotCode(): Stream<BotDAO>

    @Transactional
    fun findByIdInAndDeletedIsNull(botIds: List<Long>): Stream<BotDAO>

    @Modifying
    @Transactional
    @Query(
        """
            UPDATE BotDAO b 
                SET b.eloScore = :eloScore 
                WHERE b.id = :id
        """,
    )
    fun updateEloScore(
        id: Long,
        eloScore: Int,
    ): Int

    @Modifying
    @Transactional
    @Query(
        """
            UPDATE BotDAO b 
                SET b.banned = :banned 
                WHERE b.id = :id
        """,
    )
    fun updateBotBannedStatus(
        id: Long,
        banned: Boolean,
    ): Int
}
