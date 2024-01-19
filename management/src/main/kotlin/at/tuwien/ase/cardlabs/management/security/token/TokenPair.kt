package at.tuwien.ase.cardlabs.management.security.token

/**
 * A token pair consisting of a refresh and access token
 */
data class TokenPair(
    val refreshToken: Token,
    val accessToken: Token
)
