package app.kaster.common.login

interface Biometrics {

    suspend fun promptUserAuth(): AuthResult

    val isSupported: Boolean

    enum class AuthResult {
        Success, Failed
    }

    object Unsupported : Biometrics {
        override suspend fun promptUserAuth(): AuthResult = AuthResult.Failed
        override val isSupported: Boolean = false
    }
}