package crypt

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

class BundledDesEncryptor : Encryptor {
	
	companion object{
		private const val algorithm = "DES"
	}
	
	private val keyFactory = SecretKeyFactory.getInstance(algorithm)
	
	override fun encrypt(data: String, key: String): String {
		val cryptKey = keyFactory.generateSecret(DESKeySpec(key.toByteArray()))
		
		val cipher = Cipher.getInstance(algorithm)
		cipher.init(Cipher.ENCRYPT_MODE, cryptKey)
		return Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
	}
	
	override fun decrypt(data: String, key: String): String {
		val cryptKey = keyFactory.generateSecret(DESKeySpec(key.toByteArray()))
		
		val cipher = Cipher.getInstance(algorithm)
		cipher.init(Cipher.DECRYPT_MODE, cryptKey)
		return String(cipher.doFinal(Base64.getDecoder().decode(data)))
	}
}