package at.tuwien.ase.cardlabs.management.controller.model.account

data class AccountUpdate(
    val location: String?,
    val sendScoreUpdates: Boolean,
    val sendChangeUpdates: Boolean,
    val sendNewsletter: Boolean,
)
