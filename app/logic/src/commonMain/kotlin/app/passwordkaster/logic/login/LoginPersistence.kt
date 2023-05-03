package app.passwordkaster.logic.login

interface LoginPersistence {

    fun saveCredentials(credentials: LoginInteractor.Credentials)

    fun loadCredentials(): LoginInteractor.Credentials?

    var userAuthenticationRequired: Boolean

    fun clear()
}
