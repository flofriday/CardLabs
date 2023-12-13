package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import org.springframework.stereotype.Service

@Service
class LocationService(private val locationRepository: LocationRepository) {
    fun getAll(): List<String> {
        return locationRepository.findAll().map { s -> s!!.name }.toList()
    }
}
