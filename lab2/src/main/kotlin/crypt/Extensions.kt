package crypt

fun <T> List<T>.leftShift(direction: Int): List<T> {
	return slice(direction until size) + slice(0 until direction)
}

fun <T> List<T>.rightShift(direction: Int): List<T> {
	return slice(size - direction until size) + slice(0 until size - direction)
}

fun Boolean.toByte() = if (this) {
	1
} else {
	0
}

fun List<Boolean>.getAB(): Pair<Int, Int> {
	val a = get(0).toByte().shl(1) + get(5).toByte()
	val b = get(1).toByte().shl(3) +
			get(2).toByte().shl(2) +
			get(3).toByte().shl(1) +
			get(4).toByte()
	return a to b
}

fun List<Boolean>.toQuadBytes(): List<Byte> {
	require(size == 64)
	return (indices step 8).map {
		slice(it until it + 8).map { b -> b.toByte().toUInt() }.reduce { a, i -> a * 2u + i }.toByte()
	}
}

fun List<Boolean>.toBitString() = joinToString("") { it.toByte().toString() }