package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.repository.LeaderBoardRepository
import at.tuwien.ase.cardlabs.management.util.Region
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LeaderBoardService(private val leaderBoardRepository: LeaderBoardRepository){
    fun getLeaderBoardPage(entriesPerPage: Int, page: Int, regionType: Region): Page<LeaderBoardEntry> {
        val pageable = PageRequest.of(page, entriesPerPage)
        return leaderBoardRepository.getLeaderBoardEntries(pageable)
    }
}