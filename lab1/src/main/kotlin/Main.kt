import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
	
	if (args.size != 3) {
		exitProcess(1)
	}
	
	val src = File(args[2]).readText()
	
	val res: String = when {
		args[0] == "c" -> {
			caesar(src, args[1].toInt())
		}
		args[0] == "v" -> {
			visiner(src, args[1])
		}
		else -> {
			throw IllegalArgumentException()
		}
	}
	
	println(res)
}