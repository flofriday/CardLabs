package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface AccountRepository : CrudRepository<AccountDAO?, Long?> {

    fun findByUsername(username: String): AccountDAO?

}

