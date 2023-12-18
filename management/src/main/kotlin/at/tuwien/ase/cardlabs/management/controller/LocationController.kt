package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.service.LocationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationController(val locationService: LocationService) {

    @GetMapping("/locations")
    fun locations(user: CardLabUser): ResponseEntity<List<String>> {
        val result = locationService.getAll(user)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }
}
