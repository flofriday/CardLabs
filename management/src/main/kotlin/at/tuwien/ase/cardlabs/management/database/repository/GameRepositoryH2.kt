package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
@Profile("local")
interface GameRepositoryH2 : GameRepository {

    @Query(
        """
            SELECT *
                FROM game 
                WHERE ARRAY_CONTAINS(participating_bot_ids, :botId)
                ORDER BY start_time
        """,
        nativeQuery = true,
    )
    override fun findAllGamesWithBot(@Param("botId") botId: Long, pageable: Pageable): Page<GameDAO>
}
