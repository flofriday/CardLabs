package at.tuwien.ase.cardlabs.management

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest
@ActiveProfiles("test")
annotation class ApplicationTest
