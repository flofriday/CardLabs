package at.tuwien.ase.cardlabs.management.database.model

import at.tuwien.ase.cardlabs.management.util.Continent
import jakarta.persistence.*

@Entity
@Table(name = "location")
class LocationDAO {

    @Id
    @Column(unique = true, nullable = false)
    lateinit var name: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var continent: Continent
}
