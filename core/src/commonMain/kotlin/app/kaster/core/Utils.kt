package app.kaster.core

internal fun Int.toBigEndianBytes() = ByteArray(4) { i -> (this shr ((3 - i) * 8)).toByte() }

internal fun ByteArray.toHexString(): String = joinToString(separator = "") { byte -> "%02X".format(byte) }

internal fun Int.toHexString() = toBigEndianBytes().toHexString()