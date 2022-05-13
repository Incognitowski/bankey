package parameter

import framework.coroutineOnAThread
import framework.injectFromKoin
import io.javalin.http.Context
import io.javalin.http.sse.SseClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object ParameterController {

    private val mParameterAPI: ParameterAPI = injectFromKoin()

    fun getOrCreate(ctx: Context) {
        val lParameterEntity = mParameterAPI.findLatest()
            ?: ParameterEntity(processOperations = true).also { mParameterAPI.create(it) }
        ctx.json(lParameterEntity)
    }

    fun listenToParameterChanges(sse: SseClient) {
        val lShouldRespond = true
        coroutineOnAThread {
            while (true) {
                delay(5_000)
                val lParameterEntity = mParameterAPI.findLatest()!!
                lParameterEntity.processOperations = !lParameterEntity.processOperations
                mParameterAPI.update(lParameterEntity)
            }
        }
        runBlocking {
            while (lShouldRespond) {
                delay(4_000)
                sse.sendEvent("testEvent", mParameterAPI.findLatest() ?: ParameterEntity(processOperations = true).also { mParameterAPI.create(it) })
            }
        }
    }

}