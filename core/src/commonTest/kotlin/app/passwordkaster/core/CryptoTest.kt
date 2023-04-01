package app.passwordkaster.core

import kotlin.test.Test
import kotlin.test.assertEquals

class CryptoTest {

    @Test
    fun testSha256() {
        val hash = sha256("test123".encodeToByteArray())
        assertEquals(
            expected = "ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae",
            actual = hash.toHexString()
        )
    }

    @Test
    fun testHmacSha256() {
        val hash = hmacSha256("secret".encodeToByteArray(), "test123".encodeToByteArray())
        assertEquals(
            expected = "512c2c40c9a430e207dd720ce07ddd870a2094670f0f0049062d9712b1e38d2b",
            actual = hash.toHexString()
        )
    }

    @Test
    fun testHmacSha256WithLongKey() {
        val hash = hmacSha256(
            "0".repeat(66).encodeToByteArray(),
            "test123".encodeToByteArray()
        )
        assertEquals(
            expected = "d547570f76f53853d95b96eeef42b58b1ff3c4466960f3022a112ba3b228891b",
            actual = hash.toHexString()
        )
    }
}
