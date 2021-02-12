import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import tcp_ip.Section
import tcp_reset.InfinityTcpConnection
import tsp_search.TcpServer

fun main() {
	scan(false)
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
				)
			)
			sn += 16
			delay(100)
		}
		
		j1.cancel()
		j2.cancel()
	}
}