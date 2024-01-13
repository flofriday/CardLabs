package simulation

/**
 * A config for the gameserver that has defaults and loads them from the environment.
 */
class Config {
    var rmqHost = "localhost"
    var rmqUser: String? = null
    var rmqPassword: String? = null
    var rmqVirtualHost: String? = null

    fun load() {
        if (System.getenv("RMQ_HOST") != null) {
            rmqHost = System.getenv("RMQ_HOST")
        }
        if (System.getenv("RMQ_USER") != null) {
            rmqUser = System.getenv("RMQ_USER")
        }
        if (System.getenv("RMQ_PASSWORD") != null) {
            rmqPassword = System.getenv("RMQ_PASSWORD")
        }
        if (System.getenv("RMQ_VIRTUALHOST") != null) {
            rmqVirtualHost = System.getenv("RMQ_VIRTUALHOST")
        }
    }
}
