package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.repository.LeaderBoardRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.util.Region
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LeaderBoardService(private val leaderBoardRepository: LeaderBoardRepository, private val locationRepository: LocationRepository) {
    fun getLeaderBoardPage(entriesPerPage: Int, page: Int, regionType: Region, filter: String): Page<LeaderBoardEntry> {
        val pageable = PageRequest.of(page, entriesPerPage)

        if (regionType == Region.GLOBAL) {
            return leaderBoardRepository.getLeaderBoardEntriesGlobal(pageable)
        } else if (regionType == Region.CONTINENT) {
            val continent = locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getLeaderBoardEntriesContinent(continent.continent, pageable)
        } else {
            return leaderBoardRepository.getLeaderBoardEntriesCountry(filter, pageable)
        }
    }

    fun getPrivateLeaderBoardPage(entriesPerPage: Int, page: Int, regionType: Region, filter: String, user: CardLabUser): Page<LeaderBoardEntry> {
        val pageable = PageRequest.of(page, entriesPerPage)

        if (regionType == Region.GLOBAL) {
            return leaderBoardRepository.getPrivateLeaderBoardEntriesGlobal(user.id, pageable)
        } else if (regionType == Region.CONTINENT) {
            val continent = locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getPrivateLeaderBoardEntriesContinent(user.id, continent.continent, pageable)
        } else {
            return leaderBoardRepository.getPrivateLeaderBoardEntriesCountry(user.id, filter, pageable)
        }
    }

    fun getScoreOfGlobalFirstPlace(): Long {
        return leaderBoardRepository.getScoreOfGlobalFirstPlace()
    }
}
