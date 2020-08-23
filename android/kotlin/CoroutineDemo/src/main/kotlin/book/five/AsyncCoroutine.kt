package book.five

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine


/**
 * 对应《深入理解协程》 await函数声明
 */

interface Deferred<T> : Job{
    suspend fun await() : T
}

@Suppress("UNCHECKED_CAST")
class DeferredCoroutine<T>(context: CoroutineContext) : AbstractCoroutine<T>(context) , Deferred<T> {
    override suspend fun await(): T {
        return when(val currentSate  = state.get()){
            is CoroutineState.Incomplete,
            is CoroutineState.Cancelling -> {
                awaitSuspend()
            }
            is CoroutineState.Complete<*> ->{
                currentSate.exception?.let {
                    throw it
                }?:(currentSate.value as T)
            }
        }
    }

    private suspend fun awaitSuspend() = suspendCoroutine<T> { continuation ->
        doOnCompleted { result ->
            continuation.resumeWith(result)
        }
    }
}

//fun <T> async(
//    context: CoroutineContext = EmptyCoroutineContext,
//    block : suspend () -> Unit
//) : Deferred<T>{
//    val completion = DeferredCoroutine<T>(context)
//    block.startCoroutine(completion,completion)
//}
