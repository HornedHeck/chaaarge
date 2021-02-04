package crypt

import crypt.CustomDesEncryptor.Companion.BLOCK_SIZE
import crypt.CustomDesEncryptor.Companion.B_COUNT
import crypt.CustomDesEncryptor.Companion.B_SIZE

private val ip = arrayOf(
	57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
	61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7,
	56, 48, 40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2,
	60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6
)

fun List<Boolean>.applyIp(): List<Boolean> {
	require(size == BLOCK_SIZE)
	return ip.map { this[it] }
}


private val ipr = arrayOf(
	39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30,
	37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28,
	35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26,
	33, 1, 41, 9, 49, 17, 57, 25, 32, 0, 40, 8, 48, 16, 56, 24
)

fun List<Boolean>.applyIpR(): List<Boolean> {
	require(size == BLOCK_SIZE)
	return ipr.map { this[it] }
}

private val e = arrayOf(
	31, 0, 1, 2, 3, 4,
	3, 4, 5, 6, 7, 8,
	7, 8, 9, 10, 11, 12,
	11, 12, 13, 14, 15, 16,
	15, 16, 17, 18, 19, 20,
	19, 20, 21, 22, 23, 24,
	23, 24, 25, 26, 27, 28,
	27, 28, 29, 30, 31, 0
)

fun List<Boolean>.applyE(): List<Boolean> {
	return e.map { this[it] }
}

val c0 = listOf(
	56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17,
	9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35
)

val d0 = listOf(
	62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21,
	13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19, 11, 3
)

private val cdl = arrayOf(
	c0.leftShift(1) + d0.leftShift(1),
	c0.leftShift(2) + d0.leftShift(2),
	c0.leftShift(4) + d0.leftShift(4),
	c0.leftShift(6) + d0.leftShift(6),
	c0.leftShift(8) + d0.leftShift(8),
	c0.leftShift(10) + d0.leftShift(10),
	c0.leftShift(12) + d0.leftShift(12),
	c0.leftShift(14) + d0.leftShift(14),
	c0.leftShift(15) + d0.leftShift(15),
	c0.leftShift(17) + d0.leftShift(17),
	c0.leftShift(19) + d0.leftShift(19),
	c0.leftShift(21) + d0.leftShift(21),
	c0.leftShift(23) + d0.leftShift(23),
	c0.leftShift(25) + d0.leftShift(25),
	c0.leftShift(27) + d0.leftShift(27),
	c0.leftShift(28) + d0.leftShift(28),
)

private val cdr = arrayOf(
	c0.rightShift(1) + d0.rightShift(1),
	c0.rightShift(2) + d0.rightShift(2),
	c0.rightShift(4) + d0.rightShift(4),
	c0.rightShift(6) + d0.rightShift(6),
	c0.rightShift(8) + d0.rightShift(8),
	c0.rightShift(10) + d0.rightShift(10),
	c0.rightShift(12) + d0.rightShift(12),
	c0.rightShift(14) + d0.rightShift(14),
	c0.rightShift(15) + d0.rightShift(15),
	c0.rightShift(17) + d0.rightShift(17),
	c0.rightShift(19) + d0.rightShift(19),
	c0.rightShift(21) + d0.rightShift(21),
	c0.rightShift(23) + d0.rightShift(23),
	c0.rightShift(25) + d0.rightShift(25),
	c0.rightShift(27) + d0.rightShift(27),
	c0.rightShift(28) + d0.rightShift(28),
)

fun List<Boolean>.applyCDL(i: Int): List<Boolean> {
	require(size == BLOCK_SIZE)
	return cdl[i].map { this[it] }
}


fun List<Boolean>.applyCDR(i: Int): List<Boolean> {
	require(size == BLOCK_SIZE)
	return cdr[i].map { this[it] }
}

private val keySelect = arrayOf(
	13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 20, 9, 22, 18, 11, 3,
	25, 7, 15, 6, 26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 29, 39,
	50, 44, 32, 47, 43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31
)

fun List<Boolean>.applyKeySelect(): List<Boolean> {
	require(size == 56)
	return keySelect.map { this[it] }
}

private val p = arrayOf(
	15, 6, 19, 20, 28, 11, 27, 16,
	0, 14, 22, 25, 4, 17, 30, 9,
	1, 7, 23, 13, 31, 26, 2, 8,
	18, 12, 29, 5, 21, 10, 3, 24
)

fun List<Boolean>.applyP(): List<Boolean> {
	require(size == BLOCK_SIZE / 2)
	return p.map { this[it] }
}

private val s: Array<Array<Array<Byte>>> = arrayOf(
	arrayOf(
		arrayOf(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
		arrayOf(0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8),
		arrayOf(4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
		arrayOf(15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13)
	),
	arrayOf(
		arrayOf(15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10),
		arrayOf(3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5),
		arrayOf(0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15),
		arrayOf(13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9)
	),
	arrayOf(
		arrayOf(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8),
		arrayOf(13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1),
		arrayOf(13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7),
		arrayOf(1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12)
	),
	arrayOf(
		arrayOf(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15),
		arrayOf(13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9),
		arrayOf(10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4),
		arrayOf(3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14)
	),
	arrayOf(
		arrayOf(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9),
		arrayOf(14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6),
		arrayOf(4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14),
		arrayOf(11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3)
	),
	arrayOf(
		arrayOf(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11),
		arrayOf(10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8),
		arrayOf(9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6),
		arrayOf(4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13)
	),
	arrayOf(
		arrayOf(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1),
		arrayOf(13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6),
		arrayOf(1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2),
		arrayOf(6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12)
	),
	arrayOf(
		arrayOf(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7),
		arrayOf(1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2),
		arrayOf(7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8),
		arrayOf(2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11)
	)
)

fun applyS(i: Int, a: Int, b: Int): List<Boolean> {
	return s[i][a][b].toBits(4)
}

fun List<Boolean>.applyF(key: List<Boolean>): List<Boolean> {
	require(size == BLOCK_SIZE / 2)
	require(key.size == 48)
	
	val preS = applyE()
		.mapIndexed { i, v -> v != key[i] }
	val res = (0 until B_COUNT).map {
		val (a, b) = preS.slice(it * B_SIZE until (it + 1) * B_SIZE).getAB()
		applyS(it, a, b)
	}.flatten()
	
	return res.applyP()
	
}

fun List<Boolean>.expandKey(): List<Boolean> {
	require(size == 56)
	return (0 until 56 step 7).map {
		val slice = this.slice(it until it + 7)
		val add = slice.count { b -> b }.rem(2) == 0
		listOf(add) + slice
	}.flatten()
}