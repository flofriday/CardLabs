package at.tuwien.ase.cardlabs.management.controller.model.account

data class Account(
    val id: Long?,
    val username: String,
    val email: String,
    val password: String,
    val location: String?,
    val sendScoreUpdates: Boolean,
    val sendChangeUpdates: Boolean,
    val sendNewsletter: Boolean,
)
