package scan

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import common.Section
import kotlin.random.Random

class TcpServer(val isFirewallEnabled: Boolean, val ip: String) {
	
	private val ports = listOf(
		//FTP
		Port(20), Port(21),
		//SSH (required)
		Port(22, false),
		//Telnet
		Port(23),
		//Production SMTP server
		Port(25),
		//MySQL DB
		Port(118), Port(3306),
		//Sites
		Port(8008, false), Port(8080, false),
		//Smb plays Doom 1,2
		Port(666)
	)
	
	fun setupChannels(chIn: ReceiveChannel<Section>, chOut: Channel<Section>) =
		CoroutineScope(Dispatchers.Default).launch {
			chIn.consumeEach {
				if (it.rIp != ip) return@consumeEach
				val port = ports.firstOrNull { p -> p.num == it.rPort }
				if (port == null || (port.isCloseable && isFirewallEnabled)) {
					chOut.send(
						Section(
							sIp = ip,
							sPort = it.rPort,
							rIp = it.sIp,
							rPort = it.sPort,
							rst = true
						)
					)
					return@consumeEach
				}
				chOut.send(
					Section(
						sIp = ip,
						sPort = it.rPort,
						rIp = it.sIp,
						rPort = it.sPort,
						ack = true,
						`as` = it.sn + 1,
						syn = true,
						sn = Random.nextInt()
					)
				)
			}
			println("Closing")
		}
	
}