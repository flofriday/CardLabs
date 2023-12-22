package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.match.GameDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Game entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface GameRepository : CrudRepository<GameDAO?, Long?> {

    fun findByIdAndDeletedIsNull(id: Long): GameDAO?
}
