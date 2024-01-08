package at.tuwien.ase.cardlabs.management.database.model.game

import at.tuwien.ase.cardlabs.management.database.converter.TurnListConverter
import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import at.tuwien.ase.cardlabs.management.database.model.game.turn.Turn
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

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
    lateinit var startTime: Instant

    @Column(nullable = false)
    lateinit var endTime: Instant

    @Column
    var winningBotId: Long? = null

    @Column
    var disqualifiedBotId: Long? = null

    @Column(nullable = false, length = 16777216)
    @Convert(converter = TurnListConverter::class)
    lateinit var turns: List<Turn>

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    lateinit var gameState: GameState

    @Column(nullable = false)
    lateinit var participatingBotIds: List<Long>
}
