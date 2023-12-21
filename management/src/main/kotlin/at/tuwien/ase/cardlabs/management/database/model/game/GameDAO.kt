package at.tuwien.ase.cardlabs.management.database.model.game

import at.tuwien.ase.cardlabs.management.database.converter.ActionListConverter
import at.tuwien.ase.cardlabs.management.database.converter.LogMessageListConverter
import at.tuwien.ase.cardlabs.management.database.converter.ResultListConverter
import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import at.tuwien.ase.cardlabs.management.database.model.game.action.Action
import at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.model.game.result.Result
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

    @Column(nullable = false)
    @Convert(converter = ActionListConverter::class)
    lateinit var actions: List<Action>

    @Column(nullable = false)
    @Convert(converter = ResultListConverter::class)
    lateinit var results: List<Result>

    @Column(nullable = false)
    @Convert(converter = LogMessageListConverter::class)
    lateinit var logMessages: List<LogMessage>

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    lateinit var gameState: GameState
}
