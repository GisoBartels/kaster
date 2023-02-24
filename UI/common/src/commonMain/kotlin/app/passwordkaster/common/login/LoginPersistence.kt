package app.passwordkaster.common.login

interface LoginPersistence {

    fun saveCredentials(credentials: LoginInteractor.Credentials)

    fun loadCredentials(): LoginInteractor.Credentials?

    var userAuthenticationRequired: Boolean

    fun clear()
}
