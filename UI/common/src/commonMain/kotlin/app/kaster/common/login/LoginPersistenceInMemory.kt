package app.kaster.common.login

class LoginPersistenceInMemory(initialUsername: String = "", initialPassword: String = "") : LoginPersistence {

    private var username = initialUsername
    private var password = initialPassword

    override fun storeCredentials(username: String, password: String) {
        this.username = username
        this.password = password
    }

    override fun loadUsername(): String = username

    override fun loadMasterPassword(): String = password

}