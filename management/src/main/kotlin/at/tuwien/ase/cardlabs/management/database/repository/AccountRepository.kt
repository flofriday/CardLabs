package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Account entities for which the deleted field is not null are viewed as deleted entities
 */
@Repository
interface AccountRepository : CrudRepository<AccountDAO?, Long?> {

    fun findByAccountIdAndDeletedIsNull(id: Long): AccountDAO?

    fun findByUsernameAndDeletedIsNull(username: String): AccountDAO?

    fun findByEmailAndDeletedIsNull(email: String): AccountDAO?
}
