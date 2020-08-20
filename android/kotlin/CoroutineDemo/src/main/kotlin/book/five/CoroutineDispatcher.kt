package book.five

import kotlinx.coroutines.delay
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * 对应《深入理解kotlin协程》
 *
 * 1： kotlin调度器的实现
 */


fun main() {
//    launch(Dispatchers.Default) {
//        println(1)
//        delay(1000)
//        println(2)
//    }
}

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

/**
 * 基于线程池的调度器实现
 */
object DefaultDispatcher : Dispatcher{

    private val threadGroup = ThreadGroup("DefaultDispatcher")
    private val threadIndex = AtomicInteger(0)

    private val executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()+1
    ){runnable ->
        Thread(threadGroup,runnable,"${threadGroup.name}+worker+${threadIndex.getAndIncrement()}")
                .apply { isDaemon = true }
    }

    override fun dispatcher(block: () -> Unit) {
        executor.submit(block)
    }

}

object Dispatchers{

    val Default by lazy {
        DispatcherContext(DefaultDispatcher)
    }
}