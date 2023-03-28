package app.passwordkaster.core

import kotlin.test.Test
import kotlin.test.assertEquals

class MasterKeyTest {

    @Test
    fun testMasterKeySalt() {
        val salt = MasterKey.salt("Bender")
        assertEquals(
            "2ade71770ad37827976b76932da344280fa81de6da5256fb8e6310520bffa48a",
            sha256(salt).toHexString()
        )
    }

    @Test
    fun verifyMasterKeyAlgorithm() {
        val masterKey = MasterKey("Bender", "BiteMyShinyMetalAss!")
        assertEquals(
            "13826b521b744f9488d1ff6433a96d687c52e2ac50dda296a66ca87bc2a1ef09",
            sha256(masterKey.key).toHexString()
        )
    }
}
