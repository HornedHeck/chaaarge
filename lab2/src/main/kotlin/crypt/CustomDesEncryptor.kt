package crypt

import java.nio.ByteBuffer
import java.util.*

class CustomDesEncryptor : Encryptor {
	
	companion object {
		
		private val ip = arrayOf(
			57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 24, 19, 11, 3,
			61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7,
			56, 48, 40, 32, 24, 13, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2,
			60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6
		)
		
		private val E = arrayOf(
			31, 0, 1, 2, 3, 4,
			3, 4, 5, 6, 7, 8,
			7, 8, 9, 10, 11, 12,
			11, 12, 13, 14, 15, 16,
			15, 16, 17, 18, 19, 20,
			19, 20, 21, 22, 23, 24,
			23, 24, 25, 26, 27, 28,
			27, 28, 29, 30, 31, 0
		)
		
		const val BLOCK_SIZE = 64
		const val MEGA_BLOCK_SIZE = 8
		const val FEISTEL_SIZE = 48
		const val B_SIZE = 6
		const val B_COUNT = 8
		
		/** S , a , b */
		private val S: Array<Array<Array<Byte>>> =
			arrayOf(
				/** S1 */
				arrayOf(
					arrayOf(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
					arrayOf(0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8),
					arrayOf(4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
					arrayOf(15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13)
				),
				/** S2 */
				arrayOf(
					arrayOf(15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10),
					arrayOf(3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5),
					arrayOf(0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15),
					arrayOf(13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9)
				),
				/** S3 */
				arrayOf(
					arrayOf(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8),
					arrayOf(13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1),
					arrayOf(13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7),
					arrayOf(1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12)
				),
				/** S4 */
				arrayOf(
					arrayOf(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15),
					arrayOf(13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9),
					arrayOf(10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4),
					arrayOf(3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14)
				),
				/** S5 */
				arrayOf(
					arrayOf(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9),
					arrayOf(14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6),
					arrayOf(4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14),
					arrayOf(11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3)
				),
				/** S6 */
				arrayOf(
					arrayOf(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11),
					arrayOf(10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8),
					arrayOf(9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6),
					arrayOf(4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13)
				),
				/** S7 */
				arrayOf(
					arrayOf(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1),
					arrayOf(13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6),
					arrayOf(1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2),
					arrayOf(6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12)
				),
				/** S8 */
				arrayOf(
					arrayOf(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7),
					arrayOf(1, 15, 13, 8, 10, 3, 7, 4, 15, 5, 6, 11, 0, 14, 9, 2),
					arrayOf(7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8),
					arrayOf(2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11)
				)
			)
		
	}
	
	private fun Boolean.toByte() = if (this) {
		1
	} else {
		0
	}
	
	private fun getAB(set: BitSet): Pair<Int, Int> {
		val a = set.get(0).toByte().shl(1) + set.get(5).toByte()
		val b = set.get(1).toByte().shl(3) +
				set.get(2).toByte().shl(2) +
				set.get(3).toByte().shl(1) +
				set.get(4).toByte()
		
		return a to b
	}
	
	/** Represents one application of Feistel function
	 * @param r 32 bits,
	 * @param key 48-bit key, generated from original 56-bit key,
	 * @return 32 bits of result
	 */
	private fun feistel(r: BitSet, key: BitSet) {
		/** Expanded 48-bit version of r*/
		val extendedSet = BitSet(FEISTEL_SIZE)
		E.map { r[it] }.forEachIndexed { i, b ->
			extendedSet.set(i, b)
		}
		extendedSet.xor(key)
		
		(0 until FEISTEL_SIZE step B_SIZE)
			.map {
				extendedSet.get(it, it + B_SIZE)
			}.mapIndexed { i, set ->
				val (a , b) = getAB(set)
				val res = S[i][a][b]
			}
		
	}
	
	private fun des(block: BitSet, key: String): Array<Byte> {
		val postIp = ip.map { block[it] }
		
		
		
		
		return Array(MEGA_BLOCK_SIZE) { 32 }
	}
	
	override fun encrypt(data: String, key: String): String {
		
		var bytes = data.toByteArray()
		
		println(bytes.size)
		
		val diff = MEGA_BLOCK_SIZE - bytes.size.rem(MEGA_BLOCK_SIZE)
		if (diff != 0) {
			bytes += ByteArray(diff) { 32 }
		}
		
		return Base64.getEncoder().encodeToString(
			(0 until bytes.size / MEGA_BLOCK_SIZE)
				.map {
					val slice = bytes.slice(it * MEGA_BLOCK_SIZE until (it + 1) * MEGA_BLOCK_SIZE).toByteArray()
					val block = BitSet.valueOf(ByteBuffer.wrap(slice))
					
					des(block, key)
				}
				.flatMap { it.asIterable() }
				.toByteArray()
		)
	}
	
	override fun decrypt(data: String, key: String): String {
		return ""
	}
}