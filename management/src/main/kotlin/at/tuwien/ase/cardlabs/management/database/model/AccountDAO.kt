package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * A database entry representing a user. When the deleted field from the parent class AuditedEntity is set, then the
 * account counts as being deleted
 */
@Entity
@Table(name = "account")
class AccountDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    lateinit var username: String

    @Column(nullable = false)
    lateinit var email: String

    @Column(nullable = false)
    lateinit var password: String
}
