import crypt.BundledDesEncryptor
import crypt.CustomDesEncryptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class EncryptorTest {
	
	private val custom = CustomDesEncryptor()
	private val bundled = BundledDesEncryptor()
	
	
	@Test
	fun `custom encryptors encrypts FINE`() {
		
		
		val message = "asjafkjakjfkjafjas"
		val code = "11111111"
		
		assertEquals(bundled.encrypt(message, code), custom.encrypt(message, code))
		
	}
	
//	@Test
//	fun `getAB works FINE`() {
//		val set = BitSet(6)
//		set.set(0, 6, true)
//		set.set(1, false)
//
//		val (a, b) = custom.getAB(set)
//
//		assertEquals(7, b)
//		assertEquals(3, a)
//	}
//
}