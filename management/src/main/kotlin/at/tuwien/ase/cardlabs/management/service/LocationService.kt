package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LocationService(private val locationRepository: LocationRepository) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    fun getAll(user: CardLabUser): List<String> {
        logger.debug("User ${user.id} attempts to fetch all locations")
        return locationRepository.findAll().map { s -> s!!.name }.toList()
    }
}
