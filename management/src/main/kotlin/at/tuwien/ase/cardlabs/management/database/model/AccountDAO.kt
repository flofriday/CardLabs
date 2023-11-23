package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.*

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
