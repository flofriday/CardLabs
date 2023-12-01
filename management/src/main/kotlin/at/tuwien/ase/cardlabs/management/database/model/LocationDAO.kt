package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "location")
class LocationDAO {

    @Id
    @Column(unique = true, nullable = false)
    lateinit var name: String
}
