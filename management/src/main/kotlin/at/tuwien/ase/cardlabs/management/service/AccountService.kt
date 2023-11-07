package at.tuwien.ase.cardlabs.management.service

import at.tuwien.ase.cardlabs.management.security.OAuthHelper
import at.tuwien.ase.cardlabs.management.database.model.Account
import at.tuwien.ase.cardlabs.management.database.repository.AccountRepository
import at.tuwien.ase.cardlabs.management.error.AccountExistsException
import at.tuwien.ase.cardlabs.management.security.OAuth
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional
    fun create(user: OAuth2User, oAuthProvider: OAuth): Account {
        val oAuthId = OAuthHelper().generateOAuthId(user, oAuthProvider)
        if (getByOAuthId(oAuthId).isPresent) {
            throw AccountExistsException("the account with the OAuth id %s already exists".format(oAuthId))
        }
        if (OAuthHelper().getUsername(user, oAuthProvider)?.let { getByUsername(it).isPresent } == true) {
            throw AccountExistsException("the account with the username %s already exists".format(oAuthId))
        }

        val account = Account()
        account.oauthId = oAuthId
        account.username = OAuthHelper().getUsername(user, oAuthProvider).toString()
        return accountRepository.save(account)
    }

    fun getById(id: Long): Optional<Account?> {
        return accountRepository.findById(id)
    }

    fun getByOAuthId(id: String): Optional<Account?> {
        return accountRepository.findByOauthId(id)
    }

    fun getByUsername(username: String): Optional<Account?> {
        return accountRepository.findByUsername(username);
    }

}