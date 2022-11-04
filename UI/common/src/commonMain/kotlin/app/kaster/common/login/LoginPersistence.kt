package app.kaster.common.login

interface LoginPersistence {

    fun storeCredentials(username: String, password: String)

    fun loadUsername(): String

    fun loadMasterPassword(): String

}