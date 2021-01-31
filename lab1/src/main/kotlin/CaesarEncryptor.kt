fun caesar(data: String, shift: Int): String {
	return String(
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
}