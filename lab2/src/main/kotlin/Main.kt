import crypt.CustomDesEncryptor
import kerberous.*
import java.util.*

fun main() {
	val encryptor = CustomDesEncryptor()
	val c = UUID.randomUUID().toString()
	println("c: $c")
	val cKey = UUID.randomUUID().toString().take(7)
	println("Kc: $cKey")
	
	val client = Client(c, "1234567", encryptor)
	
	val tgs = UUID.randomUUID().toString()
	println("tgs(id): $tgs")
	val tgsKey = UUID.randomUUID().toString().take(7)
	println("Kas_tgs: $tgsKey")
	val aServer = AServer(tgs, tgsKey, encryptor, mutableMapOf(c to cKey))
	
	val ss = UUID.randomUUID().toString()
	println("ss: $ss")
	val tgssKey = UUID.randomUUID().toString().take(7)
	println("Ktgs_ss: $tgssKey")
	val tgServer = TGServer(tgs , ss , encryptor , tgsKey , tgssKey)
	
	val sServer = SServer(ss , tgssKey , encryptor)
	
	runChain(client , aServer, sServer, tgServer)
}