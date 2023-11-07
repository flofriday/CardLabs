package at.tuwien.ase.cardlabs.management.security

import org.springframework.security.oauth2.core.user.OAuth2User

class OAuthHelper {

    fun generateOAuthId(user: OAuth2User, oAuthProvider: OAuth): String {
        if (oAuthProvider == OAuth.GITHUB) {
            return oAuthProvider.name + "-" + user.getAttribute("id")
        }
        throw UnsupportedOperationException(String.format("%s is not supported as OAuth provider", oAuthProvider.name))
    }

    fun getUsername(user: OAuth2User, oAuthProvider: OAuth): String? {
        if (oAuthProvider == OAuth.GITHUB) {
            return user.getAttribute("login") as? String
        }
        throw UnsupportedOperationException(String.format("%s is not supported as OAuth provider", oAuthProvider.name))
    }

}