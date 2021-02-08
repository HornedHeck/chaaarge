package kerberous

import crypt.Encryptor

class Client(
	private val c: String,
	private val key: String,
	private val encryptor: Encryptor
) {
	
	private lateinit var ctgsKey: String
	private lateinit var cssKey: String
	private var t4 = 0L
	fun step1() = c
	
	fun step3(s2: String, id: String) = try {
		val data = encryptor.decrypt(s2, key).split("|")
		val tgtE = data.first()
		ctgsKey = data[1]
//		Check data to prevent hack
		require(ctgsKey.length == 7)
		val auth = encryptor.encrypt("$c|${System.currentTimeMillis()}", ctgsKey)
		
		arrayOf(tgtE, auth, id).joinToString("|")
	} catch (e: Exception) {
		null
	}
	
	
	fun step5(s4: String): String {
		val data = encryptor.decrypt(s4, ctgsKey).split("|")
		val tgsE = data[0]
		cssKey = data[1]
		t4 = System.currentTimeMillis()
		val authE = encryptor.encrypt("$c|$t4", cssKey)
		return "$tgsE|$authE"
	}
	
	fun step6Check(s6: String): Boolean {
		val t4R = encryptor.decrypt(s6, cssKey).toLong()
		return t4R == t4 + 1
	}
	
}