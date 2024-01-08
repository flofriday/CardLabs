package at.tuwien.ase.cardlabs.management.database.model.game

enum class GameState {

    /**
     * The state when a game has been created but has not yet been executed
     */
    CREATED,

    /**
     * The state when a game has been completed
     */
    COMPLETED,
}
