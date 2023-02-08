package app.kaster.common.login

interface Biometrics {

    suspend fun promptUserAuth(): AuthResult

    enum class AuthResult {
        Success, Failed, Canceled
    }
}