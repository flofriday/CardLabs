package at.tuwien.ase.cardlabs.management.database.model.match

import at.tuwien.ase.cardlabs.management.database.converter.ActionListConverter
import at.tuwien.ase.cardlabs.management.database.converter.LogMessageListConverter
import at.tuwien.ase.cardlabs.management.database.converter.ResultListConverter
import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import at.tuwien.ase.cardlabs.management.database.model.match.action.Action
import at.tuwien.ase.cardlabs.management.database.model.match.log.LogMessage
import at.tuwien.ase.cardlabs.management.database.model.match.result.Result
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

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
}
