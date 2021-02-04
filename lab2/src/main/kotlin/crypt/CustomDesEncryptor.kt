package crypt

import java.util.*

class CustomDesEncryptor : Encryptor {
	
	companion object {
		
		const val BLOCK_SIZE = 64
		const val MEGA_BLOCK_SIZE = 8
		const val B_SIZE = 6
		const val B_COUNT = 8
		
	}
	
	
	override fun encrypt(data: String, key: String): String {
		
		val eKey = splitText(key).first().take(56).expandKey()
		
		val bytes = splitText(data).map { block ->
			val lr0 = block.applyIp().let {
				it.slice(0 until BLOCK_SIZE / 2) to it.slice(BLOCK_SIZE / 2 until BLOCK_SIZE)
			}
			var lr = emptyList<Boolean>() to emptyList<Boolean>()
			for (i in 0 until 16) {
				val keyI = eKey.applyCDL(i).applyKeySelect()
				lr = round(lr0.first, lr0.second, keyI)
			}
			(lr.first + lr.second)
				.applyIpR()
				.toQuadBytes()
		}
			.flatten()
			.toByteArray()
		
		return Base64.getEncoder().encodeToString(bytes)
	}
	
	private fun round(l: List<Boolean>, r: List<Boolean>, key: List<Boolean>): Pair<List<Boolean>, List<Boolean>> {
		
		val nr = r.applyF(key).mapIndexed { i, v -> v != l[i] }
		
		return r to nr
	}
	
	override fun decrypt(data: String, key: String): String {
		val eKey = splitText(key).first().take(56).expandKey()
		val bytes = splitText(data, true).map { block ->
			val lr0 = block.applyIp().let {
				it.slice(0 until BLOCK_SIZE / 2) to it.slice(BLOCK_SIZE / 2 until BLOCK_SIZE)
			}
			var lr = emptyList<Boolean>() to emptyList<Boolean>()
			for (i in 0 until 16) {
				val keyI = eKey.applyCDR(i).applyKeySelect()
				lr = round(lr0.second , lr0.first , keyI)
			}
			(lr.second + lr.first)
				.applyIpR()
				.toQuadBytes()
		}
			.flatten()
			.toByteArray()
		
		return String(bytes).trim()
	}
}