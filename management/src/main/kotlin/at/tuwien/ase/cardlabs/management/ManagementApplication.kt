package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.config.BotConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BotConfig::class)
class ManagementApplication

fun main(args: Array<String>) {
    runApplication<ManagementApplication>(*args)
}
