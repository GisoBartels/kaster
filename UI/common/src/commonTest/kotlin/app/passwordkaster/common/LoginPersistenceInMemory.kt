package app.passwordkaster.common

import app.passwordkaster.common.login.LoginInteractor
import app.passwordkaster.common.login.LoginPersistence

class LoginPersistenceInMemory(
    initialCredentials: LoginInteractor.Credentials? = null,
    initialUserAuthenticationRequired: Boolean = false
) : LoginPersistence {

    private var credentials = initialCredentials
    override fun saveCredentials(credentials: LoginInteractor.Credentials) {
        this.credentials = credentials
    }

    override fun loadCredentials(): LoginInteractor.Credentials? = credentials

    override var userAuthenticationRequired: Boolean = initialUserAuthenticationRequired

    override fun clear() {
        credentials = null
        userAuthenticationRequired = false
    }
}