package framework

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun runSuspended(context: CoroutineContext = Dispatchers.IO, runnable: suspend () -> Unit) {
    CoroutineScope(context).launch {
        runnable()
    }
}