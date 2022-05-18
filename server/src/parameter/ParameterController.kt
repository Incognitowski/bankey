package parameter

import framework.injectFromKoin
import framework.runInTransaction
import io.javalin.http.Context
import io.javalin.http.sse.SseClient

object ParameterController {

    private val mParameterAPI: ParameterAPI = injectFromKoin()

    fun getOrCreate(ctx: Context) {
        val lParameterEntity = mParameterAPI.findLatest()
            ?: ParameterEntity(processOperations = true).also { mParameterAPI.create(it) }
        ctx.json(lParameterEntity)
    }

    fun update(ctx: Context) {
        val lParameterEntity = ctx.bodyAsClass<ParameterEntity>()
        runInTransaction {
            mParameterAPI.update(lParameterEntity)
        }
        ParameterListeners.notify(lParameterEntity)
        ctx.json(lParameterEntity)
    }

    fun listenToParameterChanges(sse: SseClient) {
        val lParameterEntity = mParameterAPI.findLatest()
            ?: ParameterEntity(processOperations = true).also { mParameterAPI.create(it) }
        val lListener: (ParameterEntity) -> Unit = { aParameterEntity ->
            sse.sendEvent(ParameterConstants.Sse.parameter, aParameterEntity)
        }
        ParameterListeners.register(lListener).also {
            sse.onClose { ParameterListeners.unregister(it) }
        }
        sse.sendEvent(ParameterConstants.Sse.parameter, lParameterEntity)
    }

}