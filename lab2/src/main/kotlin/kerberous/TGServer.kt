package kerberous

import crypt.Encryptor
import java.util.*

class TGServer(
	private val id: String,
	private val ssId: String,
	private val encryptor: Encryptor,
	private val asKey: String,
	private val tgsssKey: String
) {
	
	fun step4(s3: String): String? {
		
		val (tgtE, authE, idExp) = s3.split("|")
		val (cExp, tgsId, t1S, p1S, ctgsKey) = encryptor.decrypt(tgtE, asKey).split("|")
		val (cAuth, t2S) = encryptor.decrypt(authE, ctgsKey).split("|")
		
		if (cAuth != cExp) return null
		if (idExp != ssId) return null
		if (tgsId != id) return null
		
		val t1 = t1S.toLong()
		val t2 = t2S.toLong()
		val p = p1S.toLong()
		
		if (t1 + p < t2) return null
		
		val cssKey = UUID.randomUUID().toString()
		
		val tgs = arrayOf(cExp, idExp, System.currentTimeMillis().toString(), "1000000", cssKey).joinToString("|")
		val tgsE = encryptor.encrypt(tgs, tgsssKey)
		
		return encryptor.encrypt("$tgsE|$cssKey", ctgsKey)
	}
	
}