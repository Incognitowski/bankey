package operation

import framework.exception.EntityNotFoundException
import framework.extensionFunctions.parseFromJson
import framework.extensionFunctions.toJSON
import framework.injectFromKoin
import framework.runInTransaction
import framework.runSuspended
import io.javalin.http.Context
import io.javalin.http.sse.SseClient
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration

object OperationController {

    private val mOperationAPI: OperationAPI = injectFromKoin()

    fun create(ctx: Context) {
        val lOperationEntity: OperationEntity = ctx.bodyAsClass()
        runInTransaction {
            mOperationAPI.create(lOperationEntity)
        }
        ctx.json(lOperationEntity)
        notifyNewOperation(lOperationEntity)
    }

    fun listenToOperationChanges(sse: SseClient) {
        val lOperationProtocol = sse.ctx.pathParam("protocol")
        val lOperationEntity = mOperationAPI.findByProtocol(lOperationProtocol)
            ?: throw EntityNotFoundException("No operation found with protocol $lOperationProtocol")
        var lShouldStream = true
        sse.sendEvent("operation", lOperationEntity)
        OperationStreamingFactory.getConsumer().use {lConsumer ->
            lConsumer.subscribe(listOf("operation"))
            sse.onClose {
                lShouldStream = false
            }
            while (lShouldStream) {
                lConsumer.poll(Duration.ofMillis(1_000)).map {
                   sse.sendEvent("operation", it.value().parseFromJson<OperationEntity>())
                }
            }
        }
    }

    private fun notifyNewOperation(aOperationEntity: OperationEntity) {
        runSuspended {
            OperationStreamingFactory.getProducer().use {
                it.send(ProducerRecord("operation", aOperationEntity.operationId, aOperationEntity.toJSON()))
            }
        }
    }

}