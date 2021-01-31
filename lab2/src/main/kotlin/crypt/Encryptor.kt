package crypt

interface Encryptor {
	
	fun encrypt(data: String, key: String): String
	
	fun decrypt(data: String, key: String): String
	
}