package crypt

import crypt.CustomDesEncryptor.Companion.BLOCK_SIZE
import crypt.CustomDesEncryptor.Companion.MEGA_BLOCK_SIZE
import java.util.*

fun splitText(string: String, decode: Boolean = false): List<List<Boolean>> {
	
	var bytes = if (decode) {
		Base64.getDecoder().decode(string)
	} else {
		string.toByteArray()
	}
	return splitText(bytes)
}

fun splitText(src: ByteArray): List<List<Boolean>> {
	var bytes = src
	val diff = MEGA_BLOCK_SIZE - bytes.size.rem(MEGA_BLOCK_SIZE)
	if (diff in 1 until MEGA_BLOCK_SIZE) {
		bytes += ByteArray(diff) { 32 }
	}
	val bits = bytes.map { it.toBits() }.flatten()
	
	return (bits.indices step BLOCK_SIZE).map { bits.slice(it until it + BLOCK_SIZE) }
}

fun Byte.toBits(len: Int = 8) = this.toUByte().toString(2).padStart(len, '0').map { it == '1' }
