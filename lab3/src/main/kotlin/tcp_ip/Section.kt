package tcp_ip

data class Section(
	val sIp: String,
	val sPort: Int,
	val rIp: String,
	val rPort: Int,
	val sn: Int = 0,
	val `as`: Int = 0,
	val urg: Boolean = false,
	val ack: Boolean = false,
	val psh: Boolean = false,
	val rst: Boolean = false,
	val syn: Boolean = false,
	val fin: Boolean = false,
	val winSize: Int = 0,
	val checksum: Int = 0,
	val impPointer: Int = 0,
	val data: ByteArray = byteArrayOf()
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as Section
		
		if (sIp != other.sIp) return false
		if (sPort != other.sPort) return false
		if (rIp != other.rIp) return false
		if (rPort != other.rPort) return false
		if (sn != other.sn) return false
		if (`as` != other.`as`) return false
		if (urg != other.urg) return false
		if (ack != other.ack) return false
		if (psh != other.psh) return false
		if (rst != other.rst) return false
		if (syn != other.syn) return false
		if (fin != other.fin) return false
		if (winSize != other.winSize) return false
		if (checksum != other.checksum) return false
		if (impPointer != other.impPointer) return false
		if (!data.contentEquals(other.data)) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = sIp.hashCode()
		result = 31 * result + sPort
		result = 31 * result + rIp.hashCode()
		result = 31 * result + rPort
		result = 31 * result + sn
		result = 31 * result + `as`
		result = 31 * result + urg.hashCode()
		result = 31 * result + ack.hashCode()
		result = 31 * result + psh.hashCode()
		result = 31 * result + rst.hashCode()
		result = 31 * result + syn.hashCode()
		result = 31 * result + fin.hashCode()
		result = 31 * result + winSize
		result = 31 * result + checksum
		result = 31 * result + impPointer
		result = 31 * result + data.contentHashCode()
		return result
	}
	
	override fun toString(): String {
		return "Section(sIp='$sIp', sPort=$sPort, rIp='$rIp', rPort=$rPort, sn=$sn, `as`=$`as`, urg=$urg, ack=$ack, psh=$psh, rst=$rst, syn=$syn, fin=$fin, winSize=$winSize, checksum=$checksum, impPointer=$impPointer)"
	}
	
	
}

