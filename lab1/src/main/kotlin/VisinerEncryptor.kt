fun visiner(data: String, word: String): String {
	val offsets = word.toCharArray().map { it.toLowerCase() - 'a' }.toIntArray()
	var offsetPos = 0
	
	return String(
		data.toCharArray()
			.map {
				if (it in availableItems) {
					encrypt(it, offsets[offsetPos]).also {
						offsetPos = (offsetPos + 1).rem(offsets.size)
					}
				} else {
					it
				}
			}
			.toCharArray()
	)
}