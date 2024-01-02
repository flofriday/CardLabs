package at.tuwien.ase.cardlabs.management.database.model.game

import at.tuwien.ase.cardlabs.management.database.converter.RoundListConverter
import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import at.tuwien.ase.cardlabs.management.database.model.game.round.Turn
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * When the game state is CREATED, then only the properties id and gameState contain correct values
 */
@Entity
@Table(name = "game")
class GameDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    lateinit var startTime: LocalDateTime

    @Column(nullable = false)
    lateinit var endTime: LocalDateTime

    @Column
    var winningBotId: Long? = null

    @Column
    var disqualifiedBotId: Long? = null

    @Column(nullable = false, length = 32768)
    @Convert(converter = RoundListConverter::class)
    lateinit var turns: List<Turn>

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    lateinit var gameState: GameState

    @Column(nullable = false)
    lateinit var participatingBots: List<Long>
}
