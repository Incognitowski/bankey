package parameter

import framework.injectFromKoin
import io.javalin.http.Context

object ParameterController {

    private val mParameterAPI: ParameterAPI = injectFromKoin()

    fun getOrCreate(aContext: Context) {
        val lParameterEntity = mParameterAPI.findLatest()
            ?: ParameterEntity(processOperations = true).also { mParameterAPI.create(it) }
        aContext.json(lParameterEntity)
    }

}