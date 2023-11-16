package at.tuwien.ase.cardlabs.management.security.authentication

data class JwtAuthenticationResponse(val username: String, val jwt: String)