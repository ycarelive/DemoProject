package book.five

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * 对应《深入理解kotlin协程》
 *
 * 1： kotlin调度器的实现
 */

/**
 *
 */
interface Dispatcher{
    fun dispatcher(block : () -> Unit)
}

open class DispatcherContext(private val dispatcher: Dispatcher)
    : AbstractCoroutineContextElement(ContinuationInterceptor),ContinuationInterceptor{
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>
            = DispatcherContinuation(continuation,dispatcher)

}

/**
 * 协程调取器：
 * 调度的具体过程其实就是在delegate（也就是真正协程恢复时要执行的逻辑）的恢复调用之前，
 * 通过dispatch将其调度到指定的调度器上。
 */
private class DispatcherContinuation<T>(
    val delegate: Continuation<T>,
    val dispatcher: Dispatcher
): Continuation<T>{
    override val context = delegate.context
    override fun resumeWith(result: Result<T>) {
        dispatcher.dispatcher {
            delegate.resumeWith(result)
        }
    }
}