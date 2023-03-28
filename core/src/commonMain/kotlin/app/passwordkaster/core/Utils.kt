package app.passwordkaster.core

@Suppress("MagicNumber")
internal fun Int.toBigEndianBytes() = ByteArray(4) { i -> (this shr ((3 - i) * 8)).toByte() }

@OptIn(ExperimentalUnsignedTypes::class)
internal fun ByteArray.toHexString(): String =
    asUByteArray().joinToString("") { it.toString(radix = 16).padStart(2, '0') }

internal fun concat(vararg byteArrays: ByteArray): ByteArray = ByteArray(byteArrays.sumOf { it.size }).apply {
    var offset = 0
    byteArrays.forEach {
        it.copyInto(this, offset)
        offset += it.size
    }
}
