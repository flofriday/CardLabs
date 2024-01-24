package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.config.BotConfig
import at.tuwien.ase.cardlabs.management.config.MatchmakerConfig
import at.tuwien.ase.cardlabs.management.security.config.OauthConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableConfigurationProperties(BotConfig::class, MatchmakerConfig::class, OauthConfig::class)
@EnableScheduling
class ManagementApplication

fun main(args: Array<String>) {
    runApplication<ManagementApplication>(*args)
}
