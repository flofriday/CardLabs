package at.tuwien.ase.cardlabs.management.controller

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.service.LeaderBoardService
import at.tuwien.ase.cardlabs.management.util.Region
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LeaderBoardController(val leaderBoardService: LeaderBoardService) {

    @GetMapping("/leaderboard/public")
    fun getLeaderBoardPage(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") entriesPerPage: Int,
        @RequestParam(defaultValue = "GLOBAL") regionType: Region,
        @RequestParam(defaultValue = "",  required = false) filter: String
    ):
            ResponseEntity<Page<LeaderBoardEntry>> {
        val resultPage = leaderBoardService.getLeaderBoardPage(entriesPerPage, page, regionType, filter)

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resultPage)
    }
}