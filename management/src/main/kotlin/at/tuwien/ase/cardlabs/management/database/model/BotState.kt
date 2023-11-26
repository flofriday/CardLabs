package at.tuwien.ase.cardlabs.management.database.model

enum class BotState {

    /**
     * The state while a bot is being created
     */
    CREATED,

    /**
     * The state while the bot is finished but awaiting the code to be verified
     */
    AWAITING_VERIFICATION,

    /**
     * The state when the code has been varied
     */
    READY,

    /**
     * The state while awaiting a match
     */
    QUEUED,

    /**
     * The state while participating in a match
     */
    PLAYING,

    /**
     * The state while there is an error such as that the code validation didn't succeed
     */
    ERROR
}
