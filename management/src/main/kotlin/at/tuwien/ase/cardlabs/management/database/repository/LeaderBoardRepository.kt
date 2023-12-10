package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

/**
 *
 */
@Repository
interface LeaderBoardRepository : PagingAndSortingRepository<BotDAO?, Long> {

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry(-1L, b.eloScore, b.name, b.owner.username)
            FROM BotDAO b ORDER BY b.eloScore DESC""")
    fun getLeaderBoardEntries(pageable: Pageable): Page<LeaderBoardEntry>

}