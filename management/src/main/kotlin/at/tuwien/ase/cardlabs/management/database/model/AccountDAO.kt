package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
class AccountDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true, nullable = false)
    lateinit var username: String

    @Column(nullable = true)
    lateinit var password: String

}
