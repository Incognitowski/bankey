package parameter

import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

object ParameterListeners {

    private val mListOfListeners = ConcurrentHashMap<Int, (ParameterEntity) -> Unit>()

    fun register(aListener: (ParameterEntity) -> Unit) : Int {
        val lIdentifier = Random.nextInt()
        mListOfListeners[lIdentifier] = aListener
        return lIdentifier
    }

    fun unregister(aListenerId: Int) {
        mListOfListeners.remove(aListenerId)
    }

    fun notify(aParameterEntity: ParameterEntity) {
        mListOfListeners.onEach { it.value.invoke(aParameterEntity) }
    }

    fun clearListeners() {
        mListOfListeners.clear()
    }

    fun getListenerById(aListenerId: Int) : (ParameterEntity) -> Unit {
        return mListOfListeners[aListenerId]
            ?: throw IllegalStateException("Listener with ID $aListenerId not found.")
    }

    fun getAllListeners() : List<(ParameterEntity) -> Unit> {
        return mListOfListeners.elements().toList()
    }

}