package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.LocationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationController(val locationService: LocationService) {

    private final val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/locations")
    fun locations(): ResponseEntity<List<String>> {
        logger.info("Attempt to fetch all locations")
        val result = locationService.getAll()
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }
}
