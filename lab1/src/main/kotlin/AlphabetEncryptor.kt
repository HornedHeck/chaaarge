private val alphabet = arrayOf(
	'a',
	'b',
	'c',
	'd',
	'e',
	'f',
	'g',
	'h',
	'i',
	'j',
	'k',
	'l',
	'm',
	'n',
	'o',
	'p',
	'q',
	'r',
	's',
	't',
	'u',
	'v',
	'w',
	'x',
	'y',
	'z'
)

fun encrypt(character: Char, shift: Int): Char {
	val isUpperCase = character.isUpperCase()
	
	val res = alphabet[(character.toLowerCase() - 'a' + shift).rem(alphabet.size)]
	
	return if (isUpperCase) {
		res.toUpperCase()
	} else {
		res
	}
}

val availableItems by lazy { ('a'..'z').toSet() + ('A'..'Z').toSet() }