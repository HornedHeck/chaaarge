package kerberous

import crypt.Encryptor

class SServer(
	val id : String,
	private val tgsssKey: String,
	private val encryptor: Encryptor
) {
	
	private val cssKeys = mutableMapOf<String, String>()
	
	fun step6(s5: String): String {
		val (tgsE, authE) = s5.split("|")
		val (cExp, ssExp, t3, p2, cssKey) = encryptor.decrypt(tgsE, tgsssKey).split("|")
		
		val (c, t4S) = encryptor.decrypt(authE, cssKey).split("|")
		
		val t4 = t4S.toLong() + 1
		
		return encryptor.encrypt(t4.toString(), cssKey)
	}
	
}