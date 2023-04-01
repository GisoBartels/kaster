package app.passwordkaster.core

import app.passwordkaster.core.Kaster.Scope

class SiteKey(masterKey: MasterKey, domain: String, counter: Int, scope: Scope) {
    val key: ByteArray = hmacSha256(masterKey.key, salt(domain, counter, scope))

    companion object {
        internal fun salt(domain: String, counter: Int, scope: Scope): ByteArray = concat(
            scope.id.encodeToByteArray(),
            domain.length.toBigEndianBytes(),
            domain.encodeToByteArray(),
            counter.toBigEndianBytes()
        )

        private val Scope.id: String
            get() = when (this) {
                Scope.Authentication -> "com.lyndir.masterpassword"
                Scope.Identification -> "com.lyndir.masterpassword.login"
                Scope.Recovery -> "com.lyndir.masterpassword.answer"
            }
    }
}
