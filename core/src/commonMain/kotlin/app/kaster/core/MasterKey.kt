package app.kaster.core

class MasterKey(username: String, masterPassword: String) {

    val key: ByteArray = scrypt(masterPassword, salt(username))

    companion object {
        internal fun salt(username: String): ByteArray =
            "com.lyndir.masterpassword".toByteArray() + username.length.toBigEndianBytes() + username.toByteArray()
    }
}
