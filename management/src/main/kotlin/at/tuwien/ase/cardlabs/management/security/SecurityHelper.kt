package at.tuwien.ase.cardlabs.management.security

import org.springframework.security.core.context.SecurityContextHolder

class SecurityHelper {

    companion object {

        fun getIdentity() {
            SecurityContextHolder.getContext().authentication.principal
        }
    }
}
