package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Game entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface GameRepository : CrudRepository<GameDAO?, Long?> {

    fun findByIdAndDeletedIsNull(id: Long): GameDAO?

    @Query(
        /*"""
        SELECT *
            FROM game
            WHERE :botId = ANY(participating_bots)
        """,*/
        """
            SELECT *
                FROM game g
                WHERE (ARRAY_CONTAINS(g.participating_bots_id, :botId))
        """,
        nativeQuery = true
    )
    fun findAllGamesWithBot(@Param("botId") botId: Long, pageable: Pageable): Page<GameDAO>
}
