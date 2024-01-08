package at.tuwien.ase.cardlabs.management.mapper

import at.tuwien.ase.cardlabs.management.matchmaker.DebugLogMessage
import at.tuwien.ase.cardlabs.management.matchmaker.LogMessage
import at.tuwien.ase.cardlabs.management.matchmaker.SystemLogMessage
import org.springframework.stereotype.Component

@Component
class LogMessageMapper {

    fun mapLogMessage(logMessage: LogMessage): at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage {
        return when (logMessage) {
            is DebugLogMessage -> mapToDebugLogMessage(logMessage)
            is SystemLogMessage -> mapToSystemLogMessage(logMessage)
            else -> at.tuwien.ase.cardlabs.management.database.model.game.log.LogMessage(logMessage.message)
        }
    }

    fun mapToDebugLogMessage(debugLogMessage: DebugLogMessage): at.tuwien.ase.cardlabs.management.database.model.game.log.DebugLogMessage {
        return at.tuwien.ase.cardlabs.management.database.model.game.log.DebugLogMessage(
            message = debugLogMessage.message,
            botId = debugLogMessage.botId
        )
    }

    fun mapToSystemLogMessage(systemLogMessage: SystemLogMessage): at.tuwien.ase.cardlabs.management.database.model.game.log.SystemLogMessage {
        return at.tuwien.ase.cardlabs.management.database.model.game.log.SystemLogMessage(systemLogMessage.message)
    }
}
