package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

/**
 * A class from which database entities can inherit inorder to have database entry modification details.
 * All database entities must inherit from this entity.
 */
@MappedSuperclass
open class AuditedEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    var created: Instant? = null

    @UpdateTimestamp
    var updated: Instant? = null

    var deleted: Instant? = null
}
