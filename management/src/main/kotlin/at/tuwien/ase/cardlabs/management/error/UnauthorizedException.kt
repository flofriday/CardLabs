package at.tuwien.ase.cardlabs.management.error

// An exception that is thrown when a user is authenticated but not authorized. In general, this results in a
// 401 Unauthorized.
class UnauthorizedException(message: String?) : Exception(message)
