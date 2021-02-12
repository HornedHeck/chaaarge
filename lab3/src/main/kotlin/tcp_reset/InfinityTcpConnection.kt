package tcp_reset

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tcp_ip.Section
import java.io.PrintStream
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

class InfinityTcpConnection(
	val ip: String,
	val port: Int
) : CoroutineScope {
	
	override val coroutineContext = context
	
	fun start(chOut: SendChannel<Section>, chIn: ReceiveChannel<Section>, rIp: String, rPort: Int) = launch {
		var lastSn = 1
		chOut.send(
			Section(
				sIp = ip,
				sPort = port,
				rIp = rIp,
				rPort = rPort,
				sn = 1,
				data = Random.nextBytes(16)
			).log(false)
		)
		while (true) {
			val res = chIn.receive().log(true)
			if (res.rIp != ip || res.rPort != port) {
				println("Skipping wrong package to ${res.rIp}:${res.rPort}")
				continue
			}
			if (res.rst) {
				if (res.sn !in lastSn until lastSn + 16) {
					continue
				}
				println("Connection $ip:$port <-> ${res.sIp}:${res.sPort} closing")
				delay(TIMEOUT)
				chOut.send(
					Section(
						sIp = res.rIp,
						sPort = res.rPort,
						rIp = res.sIp,
						rPort = res.sPort,
						rst = true
					).log(false)
				)
			} else if (res.ack) {
				delay(TIMEOUT)
				lastSn = res.`as`
				chOut.send(
					Section(
						sIp = res.rIp,
						sPort = res.rPort,
						rIp = res.sIp,
						rPort = res.sPort,
						sn = res.`as`,
						data = Random.nextBytes(16)
					).log(false)
				)
			}
		}
	}
	
	fun receive(chOut: SendChannel<Section>, chIn: ReceiveChannel<Section>) = launch {
		while (true) {
			val res = chIn.receive().log(true)
			if (res.rIp != ip || res.rPort != port) {
//					Skip wrong packages
				continue
			}
			if (res.rst) {
				println("Connection $ip:$port <-> ${res.sIp}:${res.sPort} closed")
				break
			} else {
				delay(TIMEOUT)
				chOut.send(
					Section(
						sIp = res.rIp,
						sPort = res.rPort,
						rIp = res.sIp,
						rPort = res.sPort,
						ack = true,
						`as` = res.sn + res.data.size
					).log(false)
				)
			}
		}
	}
	
	private fun Section.log(receiving: Boolean) = also {
		if (receiving) {
			writer.println("Host $ip:$port receiving $it")
		} else {
			writer.println("Host $ip:$port sending $it")
		}
		writer.flush()
	}
	
	companion object {
		val context = EmptyCoroutineContext
		const val TIMEOUT = 500L
		val writer = PrintStream("/home/hornedheck/IdeaProjects/Security/lab3/src/test/resources/tcp_reset_logs.log")
	}
	
}