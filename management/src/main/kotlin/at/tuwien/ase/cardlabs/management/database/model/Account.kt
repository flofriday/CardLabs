package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id


@Entity
class Account : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true)
    var oauthId: String? = null

    @Column(unique = true, nullable = false)
    lateinit var username: String

}
