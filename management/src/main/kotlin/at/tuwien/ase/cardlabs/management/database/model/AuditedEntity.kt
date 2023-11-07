package at.tuwien.ase.cardlabs.management.database.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant


@MappedSuperclass
open class AuditedEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    var created: Instant? = null

    @UpdateTimestamp
    var updated: Instant? = null

    var deleted: Instant? = null

}

