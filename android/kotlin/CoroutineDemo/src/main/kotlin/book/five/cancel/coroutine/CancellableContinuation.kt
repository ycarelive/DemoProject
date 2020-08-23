package book.five.cancel.coroutine

import book.five.CancellationException
import book.five.Job
import book.five.OnCancel
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException

/**
 * CancellableContinuation的实现
 */


class CancellableContinuation <T>(private val continuation: CancellableContinuation<T>)
    : Continuation<T> by continuation{

    private val state = AtomicReference<CancelState>(CancelState.InComplete)
    private val decision = AtomicReference(CancelDecision.UNDECIDED)
    val isCompleted : Boolean
    get() = when(state.get()){
        is CancelState.InComplete,
        is CancelState.CancelHandler -> false
        is CancelState.Complete<*>,
        is CancelState.Cancelled -> true
    }

    /**
     * 支持注册取消的回调
     */
    fun invokeOnCancellation(onCancel: OnCancel){
        val newState = state.updateAndGet{
            prev ->
            when(prev){
                CancelState.InComplete -> CancelState.CancelHandler(onCancel)
                is CancelState.CancelHandler -> throw IllegalStateException("Prohibited!")
                is CancelState.Complete<*>,CancelState.Cancelled -> prev
            }
        }

        if (newState is CancelState.Cancelled){
            onCancel()
        }
    }

    private fun installCancelHandler() {
        if (isCompleted) return
        val parent = continuation.context[Job] ?: return
        parent.invokeOnCancel {
            doCancel()
        }
    }

    /**
     * 协程取消时调用的doCancel函数的实现
     */
    private fun doCancel() {
        val prevState = state.getAndUpdate { prev ->
            when (prev) {
                is CancelState.CancelHandler,
                CancelState.InComplete -> {
                    CancelState.Cancelled
                }
                CancelState.Cancelled,
                is CancelState.Complete<*> -> {
                    prev
                }
            }
        }
        if (prevState is CancelState.CancelHandler) {
            prevState.onCancel()
            resumeWithException(CancellationException("Cancelled."))
        }
    }
}


sealed class CancelState{
    object InComplete : CancelState()
    class CancelHandler(val onCancel: OnCancel) : CancelState()
    class Complete<T>(val value : T? = null, val exception: Throwable? = null) : CancelState()
    object Cancelled : CancelState()
}


enum class CancelDecision{
    UNDECIDED , SUSPENDED , RESUMED
}