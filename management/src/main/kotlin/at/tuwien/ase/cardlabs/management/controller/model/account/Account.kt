package at.tuwien.ase.cardlabs.management.controller.model.account

data class Account(
    val id: Long?,
    val username: String,
    val email: String,
    val location: String?,
)
