package kerberous

import crypt.Encryptor
import java.util.*

class AServer(
	private val tgs: String,
	private val tgsKey: String,
	private val encryptor: Encryptor,
	private val keyC: MutableMap<String, String> = mutableMapOf()
) {
	
	fun step2(c: String): String? {
		
		val ctgsKey = UUID.randomUUID().toString().take(7)
		
		val tgt = arrayOf(c, this.tgs, System.currentTimeMillis().toString(), "10000000", ctgsKey).joinToString("|")
		val tgtE = encryptor.encrypt(tgt, tgsKey)
		
		return keyC[c]?.let { encryptor.encrypt("$tgtE|$ctgsKey", it) }
	}
	
}