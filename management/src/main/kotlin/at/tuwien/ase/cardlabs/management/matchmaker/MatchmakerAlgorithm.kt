package at.tuwien.ase.cardlabs.management.matchmaker

import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.controller.model.bot.Bot
import org.springframework.stereotype.Component

@Component
class MatchmakerAlgorithm(private val matchmakerConfig: MatchmakerConfig) {

    data class MatchmakerAlgorithmResult(
        val matches: List<List<Bot>>,
        val unassignedBots: List<Bot>,
    )

    fun createMatches(bots: List<Bot>): MatchmakerAlgorithmResult {
        val sortedBots = bots.sortedByDescending { it.eloScore }
        val clusters = mutableListOf<List<Bot>>()
        val unassignedBots = mutableListOf<Bot>()
        var tempCluster = mutableListOf<Bot>()

        for (bot in sortedBots) {
            if (tempCluster.isEmpty()) {
                tempCluster.add(bot)
                if (tempCluster.size == matchmakerConfig.matchSize.max) {
                    clusters.add(tempCluster)
                    tempCluster = mutableListOf()
                }
            } else {
                if (isWithinEloRange(tempCluster, bot)) {
                    tempCluster.add(bot)
                    if (tempCluster.size == matchmakerConfig.matchSize.max) {
                        clusters.add(tempCluster)
                        tempCluster = mutableListOf()
                    }
                } else {
                    if (tempCluster.size >= matchmakerConfig.matchSize.min) {
                        clusters.add(tempCluster)
                        tempCluster = mutableListOf()
                        tempCluster.add(bot)
                    } else {
                        unassignedBots.addAll(tempCluster)
                        tempCluster = mutableListOf()
                        tempCluster.add(bot)
                    }
                }
            }
        }

        if (tempCluster.isNotEmpty()) {
            if (tempCluster.size >= matchmakerConfig.matchSize.min) {
                clusters.add(tempCluster)
            } else {
                unassignedBots.addAll(tempCluster)
            }
        }

        return MatchmakerAlgorithmResult(
            matches = clusters,
            unassignedBots = unassignedBots,
        )
    }

    private fun isWithinEloRange(cluster: List<Bot>, bot: Bot): Boolean {
        if (cluster.isEmpty()) return true
        val maxElo = cluster.maxOf { it.eloScore }
        val minElo = maxElo - matchmakerConfig.matchSkillDifference.relativeEloDifference * maxElo
        return !(bot.eloScore < minElo || bot.eloScore > maxElo)
    }
}
