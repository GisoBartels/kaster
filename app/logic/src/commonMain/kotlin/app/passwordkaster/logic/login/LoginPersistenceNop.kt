package app.passwordkaster.logic.login

object LoginPersistenceNop : LoginPersistence {
    override fun saveCredentials(credentials: LoginInteractor.Credentials) {}

    override fun loadCredentials(): LoginInteractor.Credentials? = null

    override var userAuthenticationRequired: Boolean = false

    override fun clear() {}
}