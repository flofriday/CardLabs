package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.game.GameDAO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param

/**
 * Game entities for which the deleted field is not null are viewed as deleted entities
 */
@NoRepositoryBean
interface GameRepository : CrudRepository<GameDAO?, Long?> {

    fun findByIdAndDeletedIsNull(id: Long): GameDAO?

    fun findAllGamesWithBot(@Param("botId") botId: Long, pageable: Pageable): Page<GameDAO>
}
