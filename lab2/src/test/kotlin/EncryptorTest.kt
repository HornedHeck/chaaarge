import crypt.*
import crypt.CustomDesEncryptor.Companion.BLOCK_SIZE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class EncryptorTest {
	
	private val custom = CustomDesEncryptor()
	private val bundled = BundledDesEncryptor()
	
	
	@Test
	fun `custom encryptors encrypts FINE`() {
		
		
		val message = "abcd ab"
		val code = "1111111"

		val cRes = custom.encrypt(message, code)
		val cdRes = custom.decrypt(cRes, code)
	
		assertEquals(message , cdRes)
	}
	
	@Test
	fun `ip tables CORRECT`() {
		val random = Random()
		val src = (0..63).map { random.nextBoolean() }
		
		val res = src.applyIp().applyIpR()
		
		assertEquals(src, res)
	}
	
	@Test
	fun `cd table CORRECT`() {
		val p = arrayOf(
			57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
			10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
			63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
			14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4
		)
		val random = Random()
		val src = (0..63).map { random.nextBoolean() }
		
		val exp = p.map { src[it - 1] }
//		val res = src.applyCD()

//		assertEquals(exp, res)
	}
	
	@Test
	fun `keySelect table CORRECT`() {
		val keySelect = arrayOf(
			14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4,
			26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
			51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
		)
		val random = Random()
		val src = (0..55).map { random.nextBoolean() }
		
		val exp = keySelect.map { src[it - 1] }.mapIndexed { index, b -> index to b }
		val res = src.applyKeySelect().mapIndexed { index, b -> index to b }
		
		assertEquals(exp, res)
	}
	
	@Test
	fun `p table CORRECT`() {
		val p = arrayOf(
			16, 7, 20, 21, 29, 12, 28, 17,
			1, 15, 23, 26, 5, 18, 31, 10,
			2, 8, 24, 14, 32, 27, 3, 9,
			19, 13, 30, 6, 22, 11, 4, 25
		)
		val random = Random()
		val src = (0..31).map { random.nextBoolean() }
		
		val exp = p.map { src[it - 1] }
		val res = src.applyP()
		
		assertEquals(exp, res)
	}
	
	@Test
	fun `splitText works FINE`() {
		
		val text = "akslfjaklsjfklasjflkajlk slkx  ksldflk adkflajklsfjja"
		
		val splitted = splitText(text)
		
		splitted.forEach {
			assertEquals(BLOCK_SIZE, it.size)
		}
		
	}
	
	@Test
	fun `debug`() {
		val data = "akslfjaklsjfklasjflkajlk slkx  ksldflk adkflajklsfjja"
		val key = "12345678"
		
		val eKey = splitText(key).first()
		
		val bits = splitText(data).map { block ->
			var (l, r) = block.applyIp().let {
				it.slice(0 until BLOCK_SIZE / 2) to it.slice(BLOCK_SIZE / 2 until BLOCK_SIZE)
			}
			var pl: List<Boolean>
			for (i in 0 until 16) {
				val keyI = eKey.applyCDL(i).applyKeySelect()
				pl = l
				l = r
				r = r.applyF(keyI).mapIndexed { j, v -> v != pl[j] }
			}
			(l + r)
		}
		
		bits.forEach { println(it.size) }
		
	}
	
	@Test
	fun `rightShift works FINE`() {
		val src = listOf(1, 2, 3, 4, 5)
		
		val exp = listOf(4, 5, 1, 2, 3)
		val res = src.rightShift(2)
		
		assertEquals(exp , res)
	}
	
}