import common.Section
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import reset.InfinityTcpConnection
import scan.TcpServer
import synflood.TcpSynServer
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

fun main() {
	flood()
}

suspend fun testCollector(it: Int) {
	println("Collecting $it")
	delay(1000)
	println("Collected $it")
}

fun scan(verbose: Boolean) {
	val ip1 = "192.168.0.1"
	val ip2 = "192.168.0.2"
	
	val server = TcpServer(true, ip1)
	val ch1 = Channel<Section>(10)
	val ch2 = Channel<Section>(10)
	
	val job = server.setupChannels(ch1, ch2)
	job.invokeOnCompletion { println("Completed") }
	
	runBlocking {
		for (i in 20 until 9000) {
			ch1.send(
				Section(
					sIp = ip2,
					sPort = i,
					rIp = ip1,
					rPort = i,
					syn = true,
					sn = 100
				).also {
					if (verbose) println("Sending: $it")
				}
			)
			
			val response = ch2.receive()
			if (verbose) println("Receiving: $response")
			if (response.rst) {
				if (verbose) println("Port $i closed")
			} else {
				println("Port $i opened")
			}
		}
		
		ch1.close()
	}
}


fun tcpReset() {
	val ip1 = "192.168.0.1"
	val p1 = 3036
	val ip2 = "192.168.0.2"
	val p2 = 3036
	
	val c1 = InfinityTcpConnection(ip1, p1)
	val c2 = InfinityTcpConnection(ip2, p2)
	
	val channel12 = Channel<Section>(Int.MAX_VALUE)
	val channel21 = Channel<Section>(Int.MAX_VALUE)
	
	val j1 = c2.receive(channel21, channel12)
	val j2 = c1.start(channel12, channel21, ip2, p2)
	
	println("Press Enter to start TCP reset attack")
	readLine()
	
	runBlocking {
		var sn = 1
		while (j1.isActive && j2.isActive) {
			channel21.send(
				Section(
					sIp = ip2,
					sPort = p2,
					rIp = ip1,
					rPort = p1,
					rst = true,
					sn = sn
				).also {
					println("Attack attempt: $it")
				}
			)
			sn += 16
			delay(100)
		}
		
		j1.cancel()
		j2.cancel()
	}
}

fun flood() {
	
	val timeout = 500L
	val bufferSize = 32
	val toServerChannel = Channel<Section>(bufferSize * 20)
	val fromServerChannel = Channel<Section>(bufferSize * 20)
	
	val p = 20
	val serverIp = "192.168.0.1"
	val clientIp = "192.168.0.2"
	val attackerIpTemplate = "192.168.0.1"
	
	TcpSynServer(
		timeout,
		bufferSize,
		toServerChannel,
		fromServerChannel
	)
	
	@OptIn(ExperimentalTime::class)
	fun CoroutineScope.client(chIn: ReceiveChannel<Section>, chOut: Channel<Section>) = launch {
		chOut.send(
			Section(
				sIp = clientIp, sPort = p,
				rIp = serverIp, rPort = p,
				syn = true, sn = Random.nextInt()
			)
		)
		println("C sending SYN")
		val synAck = measureTimedValue { chIn.receive() }
		println("Client waited for SYN+ACK ${synAck.duration.inMilliseconds}ms")
		
		
		chOut.send(
			Section(
				sIp = clientIp, sPort = p,
				rIp = serverIp, rPort = p,
				ack = true, `as` = synAck.value.sn + 1
			)
		)
		println("C sending ACK")
		
	}
	
	runBlocking {
		repeat(100) {
			launch {
				toServerChannel.send(
					Section(
						sIp = attackerIpTemplate + it, sPort = p,
						rIp = serverIp, rPort = p,
						syn = true, sn = Random.nextInt()
					).apply {
						println("Attacker $sIp started attack")
					}
				)
			}
		}
		delay(100)
		client(
			produce {
				for (s in fromServerChannel) {
					if (s.rIp == clientIp) send(s)
				}
			},
			toServerChannel
		)
	}
}

