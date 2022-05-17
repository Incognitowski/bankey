package framework

import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

fun coroutineOnAThread(context: CoroutineContext = Dispatchers.IO, runnable: suspend () -> Unit) {
    thread {
        runSuspended(context, runnable)
    }
}