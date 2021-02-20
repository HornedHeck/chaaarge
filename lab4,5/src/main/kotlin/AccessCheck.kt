import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

fun main() {
	
	runBlocking {
		
		val client = HttpClient(CIO)
		val response = client.post<HttpResponse>("http://localhost:5000/Lang/Lang")
		println(response.status.value)
		client.close()
	}
	
}