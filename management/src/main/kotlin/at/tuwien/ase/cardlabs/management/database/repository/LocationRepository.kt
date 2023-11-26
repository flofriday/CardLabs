package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.database.model.LocationDAO
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : CrudRepository<LocationDAO?, String?> {
    fun findByName(location: String): LocationDAO?
}
