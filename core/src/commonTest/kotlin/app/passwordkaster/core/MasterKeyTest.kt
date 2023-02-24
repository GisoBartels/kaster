package app.passwordkaster.core

import kotlin.test.Test
import kotlin.test.assertEquals

class MasterKeyTest {

    @Test
    fun testMasterKeySalt() {
        val salt = MasterKey.salt("Bender")
        assertEquals(
            "2ADE71770AD37827976B76932DA344280FA81DE6DA5256FB8E6310520BFFA48A",
            sha256(salt).toHexString()
        )
    }

    @Test
    fun verifyMasterKeyAlgorithm() {
        val masterKey = MasterKey("Bender", "BiteMyShinyMetalAss!")
        assertEquals(
            "13826B521B744F9488D1FF6433A96D687C52E2AC50DDA296A66CA87BC2A1EF09",
            sha256(masterKey.key).toHexString()
        )
    }
}
