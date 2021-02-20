package synflood

import common.Section
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.random.Random

class TcpSynServer(
	val timeout: Long,
	val bufferSize: Int,
	val chIn: Channel<Section>,
	val chOut: Channel<Section>,
) : CoroutineScope {
	
	override val coroutineContext = Dispatchers.Default
	
	suspend fun join() {
		managerJob.join()
		clientJobs.joinAll()
	}
	
	private val connections = mutableMapOf<String, Channel<Section>>()
	
	private val managerJob = launch {
		chIn.consumeEach {
			while (connections.size >= bufferSize) {
				delay(50)
			}
			val addr = "${it.sIp}:${it.sPort}"
			(connections[addr] ?: run {
				val channel = Channel<Section>(5)
				connections[addr] = channel
				clientJobs.add(launchSequence(channel, addr))
				channel
			}).send(it)
		}
	}
	
	private var clientJobs = mutableListOf<Job>()
	
	
	private fun launchSequence(chIn: Channel<Section>, addr: String) = launch {
		val synRq = chIn.receive()
		chOut.send(
			Section(
				synRq.rIp, synRq.rPort, synRq.sIp, synRq.sPort,
				syn = true, sn = Random.nextInt(),
				ack = true, `as` = synRq.sn
			)
		)
		delay(50)
		try {
			withTimeout(timeout) {
				chIn.receive()
			}
			println("Connection with $addr established")
		} catch (e: Exception) {
			println("Connection with $addr closed because of timeout")
		}
		connections.remove(addr)
	}
}