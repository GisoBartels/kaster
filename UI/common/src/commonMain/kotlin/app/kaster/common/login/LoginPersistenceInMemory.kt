package app.kaster.common.login

import kotlinx.coroutines.flow.MutableStateFlow

class LoginPersistenceInMemory(initialCredentials: LoginPersistence.Credentials? = null) : LoginPersistence {

    constructor(username: String, masterPassword: String) : this(LoginPersistence.Credentials(username, masterPassword))

    override val credentials = MutableStateFlow(initialCredentials)

}