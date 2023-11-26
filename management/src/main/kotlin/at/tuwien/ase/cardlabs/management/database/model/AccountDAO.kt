package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "account")
class AccountDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(unique = true, nullable = false)
    lateinit var username: String

    @Column(unique = true, nullable = false)
    lateinit var email: String

    @Column(nullable = false)
    lateinit var password: String

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location", nullable = true)
    var location: LocationDAO? = null

    @Column(nullable = false)
    var sendScoreUpdates: Boolean = false

    @Column(nullable = false)
    var sendChangeUpdates: Boolean = false

    @Column(nullable = false)
    var sendNewsletter: Boolean = false
}
