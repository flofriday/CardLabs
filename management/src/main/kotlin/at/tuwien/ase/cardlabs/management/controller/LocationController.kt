package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.service.LocationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LocationController(val locationService: LocationService) {

    @GetMapping("/locations")
    fun locations(): ResponseEntity<List<String>> {
        val result = locationService.getAll()
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }
}
