package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.repository.LeaderBoardRepository
import at.tuwien.ase.cardlabs.management.database.repository.LocationRepository
import at.tuwien.ase.cardlabs.management.security.CardLabUser
import at.tuwien.ase.cardlabs.management.util.Region
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Service
class LeaderBoardService(
    private val leaderBoardRepository: LeaderBoardRepository,
    private val locationRepository: LocationRepository
) {

    @Transactional
    fun getLeaderBoardPage(entriesPerPage: Int, page: Int, region: Region, filter: String): Page<LeaderBoardEntry> {
        val pageable = PageRequest.of(page, entriesPerPage)

        if (region == Region.GLOBAL) {
            return leaderBoardRepository.getLeaderBoardEntriesGlobal(pageable)
        } else if (region == Region.CONTINENT) {
            val continent = locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getLeaderBoardEntriesContinent(continent.continent, pageable)
        } else {
            locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getLeaderBoardEntriesCountry(filter, pageable)
        }
    }

    @Transactional
    @Throws(IllegalArgumentException::class)
    fun getPrivateLeaderBoardPage(
        entriesPerPage: Int,
        page: Int,
        region: Region,
        filter: String,
        user: CardLabUser
    ): Page<LeaderBoardEntry> {
        val pageable = PageRequest.of(page, entriesPerPage)

        if (region == Region.GLOBAL) {
            return leaderBoardRepository.getPrivateLeaderBoardEntriesGlobal(user.id, pageable)
        } else if (region == Region.CONTINENT) {
            val continent = locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getPrivateLeaderBoardEntriesContinent(user.id, continent.continent, pageable)
        } else {
            locationRepository.findByName(filter) ?: throw IllegalArgumentException()
            return leaderBoardRepository.getPrivateLeaderBoardEntriesCountry(user.id, filter, pageable)
        }
    }

    fun getScoreOfGlobalFirstPlace(): Long? {
        return leaderBoardRepository.getScoreOfGlobalFirstPlace()
    }
}
