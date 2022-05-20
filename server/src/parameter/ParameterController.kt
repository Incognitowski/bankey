package parameter

import framework.injectFromKoin
import framework.runInTransaction
import io.javalin.http.Context
import io.javalin.http.sse.SseClient

object ParameterController {

    private val mParameterAPI: ParameterAPI = injectFromKoin()

    fun getLatest(ctx: Context) {
        ctx.json(mParameterAPI.findLatest())
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
        val lListener: (ParameterEntity) -> Unit = { aParameterEntity ->
            sse.sendEvent(ParameterConstants.Sse.parameter, aParameterEntity)
        }
        ParameterListeners.register(lListener).also {
            sse.onClose { ParameterListeners.unregister(it) }
        }
        sse.sendEvent(ParameterConstants.Sse.parameter, lParameterEntity)
    }

}