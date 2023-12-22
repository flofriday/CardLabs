package at.tuwien.ase.cardlabs.management.database.model.bot

import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * A database entry representing a bot code. When the deleted field from the parent class AuditedEntity is set, then
 * the bot code counts as being deleted
 */
@Entity
@Table(name = "bot_code")
class BotCodeDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var botCodeId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    lateinit var bot: BotDAO

    @Transient
    var botId: Long = -1

    // the bot program code
    @Column(nullable = false, length = 32768)
    lateinit var code: String
}
