package app.passwordkaster.core

internal expect fun scrypt(password: String, salt: ByteArray): ByteArray

internal object ScryptParameters {
    const val WORK_FACTOR = 32768
    const val RESOURCES = 8
    const val PARALLELIZATION = 2
    const val DERIVED_KEY_LENGTH = 64
}

internal expect fun sha256(message: ByteArray): ByteArray

internal expect fun hmacSha256(key: ByteArray, message: ByteArray): ByteArray
