package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LocationService(private val locationRepository: LocationRepository) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    fun getAll(): List<String> {
        logger.debug("Attempt to fetch all locations")
        return locationRepository.findAll().map { s -> s!!.name }.toList()
    }
}
