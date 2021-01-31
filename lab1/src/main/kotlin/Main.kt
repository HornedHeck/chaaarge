import java.io.File
import kotlin.system.exitProcess

fun usage() {
	println(
		"""
		Encryptor\Decryptor with support of Caesar and Visiner shifts
		Usage: java lab1 [Mode] [Arg] [File]
		
		 Modes:
		 1. c - Caesar Encryptor
		 2. v - Visisner Encryptor
		 3. dc - Caesar Decryptor
		 4. dv - Visiner Decryptor
		 
		 Arg:
		 1. Shift - decimal number - for Caesar
		 2. Word - any string - for Visiner
		 
		 File - file with data to encrypt\decrypt
	""".trimIndent()
	)
}

fun main(args: Array<String>) {
	
	if (args.size != 3) {
		usage()
		exitProcess(1)
	}
	
	val src = File(args[2]).readText()
	
	val res: String = when (args[0]) {
		"c" -> {
			caesar(src, args[1].toInt())
		}
		"v" -> {
			visiner(src, args[1])
		}
		"dc" -> {
			decryptCaesar(src, args[1].toInt())
		}
		"dv" -> {
			decryptVisiner(src, args[1])
		}
		else -> {
			throw IllegalArgumentException()
		}
	}
	
	println(res)
}