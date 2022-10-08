package app.kaster.core

internal fun Int.toBigEndianBytes() = ByteArray(4) { i -> (this shr ((3 - i) * 8)).toByte() }

internal fun ByteArray.toHexString(): String = joinToString(separator = "") { byte -> "%02X".format(byte) }

internal fun concat(vararg byteArrays: ByteArray): ByteArray = ByteArray(byteArrays.sumOf { it.size }).apply {
    var offset = 0
    byteArrays.forEach {
        it.copyInto(this, offset)
        offset += it.size
    }
}