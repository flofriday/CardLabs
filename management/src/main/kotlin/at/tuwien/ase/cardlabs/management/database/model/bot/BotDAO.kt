package at.tuwien.ase.cardlabs.management.database.model.bot

import at.tuwien.ase.cardlabs.management.database.model.AccountDAO
import at.tuwien.ase.cardlabs.management.database.model.AuditedEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import kotlin.jvm.Transient

/**
 * A database entry representing a bot. When the deleted field from the parent class AuditedEntity is set, then the
 * bot counts as being deleted
 */
@Entity
@Table(name = "bot")
class BotDAO : AuditedEntity() {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(nullable = false)
    lateinit var name: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", nullable = false)
    lateinit var owner: AccountDAO

    @Transient
    var ownerId: Long = -1

    // the bot program code
    @Column(nullable = false, length = 32768)
    lateinit var currentCode: String

    @CreationTimestamp
    @Column(nullable = false)
    var codeUpdated: Instant? = null

    @OneToMany(mappedBy = "bot", fetch = FetchType.LAZY)
    var codeHistory: MutableList<BotCodeDAO> = mutableListOf()

    @Column(nullable = false)
    var eloScore: Int = -1

    // == States ==
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    lateinit var currentState: BotState

    // The state to which a bot should return after a match has been completed
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    lateinit var defaultState: BotState

    // The state error message if the current bot state is ERROR
    @Column
    var errorStateMessage: String? = null

    fun getLatestCodeVersion(): BotCodeDAO? {
        return codeHistory.maxByOrNull { it.id!! }
    }
}
