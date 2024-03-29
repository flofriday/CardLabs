package at.tuwien.ase.cardlabs.management.amqp

import org.springframework.beans.factory.annotation.Qualifier

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MatchResultQueue
