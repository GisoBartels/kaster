package app.passwordkaster.core

class MasterKey(username: String, masterPassword: String) {

    val key: ByteArray = scrypt(masterPassword, salt(username))

    companion object {
        internal fun salt(username: String): ByteArray =
            "com.lyndir.masterpassword".encodeToByteArray() +
                    username.length.toBigEndianBytes() +
                    username.encodeToByteArray()
    }
}
