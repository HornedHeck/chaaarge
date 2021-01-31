fun caesar(data: String, shift: Int) = String(
	data.toCharArray()
		.map {
			if (it in availableItems) {
				encrypt(it, shift)
			} else {
				it
			}
		}
		.toCharArray()
)

fun decryptCaesar(data: String, shift: Int) = String(
	data.toCharArray()
		.map {
			if (it in availableItems) {
				decrypt(it, shift)
			} else {
				it
			}
		}
		.toCharArray()
)