package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.Account
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface AccountRepository : CrudRepository<Account?, Long?> {

    fun findByOauthId(id: String): Optional<Account?>

    fun findByUsername(username: String): Optional<Account?>

}

