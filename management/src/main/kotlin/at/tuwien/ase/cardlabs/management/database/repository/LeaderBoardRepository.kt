package at.tuwien.ase.cardlabs.management.database.repository

import at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry
import at.tuwien.ase.cardlabs.management.database.model.bot.BotDAO
import at.tuwien.ase.cardlabs.management.util.Continent
import at.tuwien.ase.cardlabs.management.util.Region
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 *
 */
@Repository
interface LeaderBoardRepository : PagingAndSortingRepository<BotDAO?, Long> {

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c WHERE c.eloScore > b.eloScore ), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b ORDER BY b.eloScore DESC""")
    fun getLeaderBoardEntriesGlobal(pageable: Pageable): Page<LeaderBoardEntry>

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c LEFT JOIN c.owner o1 LEFT JOIN o1.location l1 WHERE c.eloScore > b.eloScore AND  l1.continent = :continent), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b LEFT JOIN b.owner o2 LEFT JOIN o2.location l2 WHERE l2.continent = :continent ORDER BY b.eloScore DESC""")
    fun getLeaderBoardEntriesContinent(@Param("continent") continent: Continent, pageable: Pageable): Page<LeaderBoardEntry>

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c LEFT JOIN c.owner o1 LEFT JOIN o1.location l1 WHERE c.eloScore > b.eloScore AND  l1.name = :country), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b LEFT JOIN b.owner o2 LEFT JOIN o2.location l2 WHERE l2.name = :country ORDER BY b.eloScore DESC""")
    fun getLeaderBoardEntriesCountry(@Param("country") country: String, pageable: Pageable): Page<LeaderBoardEntry>

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c WHERE c.eloScore > b.eloScore ), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b WHERE b.owner.id = :userId ORDER BY b.eloScore DESC""")
    fun getPrivateLeaderBoardEntriesGlobal(@Param("userId") userId: Long, pageable: Pageable): Page<LeaderBoardEntry>

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c LEFT JOIN c.owner o1 LEFT JOIN o1.location l1 WHERE c.eloScore > b.eloScore AND  l1.continent = :continent), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b LEFT JOIN b.owner o2 LEFT JOIN o2.location l2 WHERE l2.continent = :continent and b.owner.id = :userId ORDER BY b.eloScore DESC""")
    fun getPrivateLeaderBoardEntriesContinent(@Param("userId") userId: Long, @Param("continent") continent: Continent, pageable: Pageable): Page<LeaderBoardEntry>

    @Query("""SELECT new at.tuwien.ase.cardlabs.management.controller.model.LeaderBoardEntry((SELECT count(*) + 1 FROM BotDAO c LEFT JOIN c.owner o1 LEFT JOIN o1.location l1 WHERE c.eloScore > b.eloScore AND  l1.name = :country), b.eloScore, b.name, b.owner.username)
            FROM BotDAO b LEFT JOIN b.owner o2 LEFT JOIN o2.location l2 WHERE l2.name = :country and b.owner.id = :userId ORDER BY b.eloScore DESC""")
    fun getPrivateLeaderBoardEntriesCountry(@Param("userId") userId: Long, @Param("country") country: String, pageable: Pageable): Page<LeaderBoardEntry>
}