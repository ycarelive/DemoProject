package book.five

import book.five.cancel.coroutine.CancellationHandlerDisposable
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * AbstractCoroutine,剖析协程中Job的实现
 */


abstract class AbstractCoroutine <T>(context: CoroutineContext) : Job , Continuation<T> {


    protected val state = AtomicReference<CoroutineState>()

    override val context: CoroutineContext

    init {
        state.set(CoroutineState.Incomplete())
        this.context = context + this
    }

    val isComplete
    get() = state.get() is CoroutineState.Complete<*>

    override val isActive: Boolean
    get() = when (state.get()) {
        is CoroutineState.Complete<*>,
        is CoroutineState.Cancelling -> false
        else -> true
    }

    @Suppress("UNCHECKED_CAST")
    override fun invokeOnCancel(onCancel: OnCancel) : Disposable {
        val disposable = CancellationHandlerDisposable(this, onCancel)
        val newState = state.updateAndGet{
            prev ->
            when(prev){
                is CoroutineState.Incomplete ->{
                    CoroutineState.Incomplete().from(prev).with(disposable)
                }
                is CoroutineState.Cancelling,
                    is CoroutineState.Complete<*> ->{
                    prev
                }
            }
        }
        (newState as? CoroutineState.Complete<T>)?.let {
            onCancel()
        }
        return disposable
    }

    override fun invokeOnComplete(onComplete: OnComplete) : Disposable {
        return doOnCompleted { _ -> onComplete() }
    }

    override fun cancel() {
        val preState = state.getAndUpdate {
            prev ->
            when(prev){
                is CoroutineState.Incomplete -> {
                    CoroutineState.Cancelling()
                }
                is CoroutineState.Cancelling,
                is CoroutineState.Complete<*> -> {
                    prev
                }
            }
        }

        if (preState is CoroutineState.Incomplete){
            preState.notifyCancellation()
            preState.clear()
        }
        //parentCancelDisposable
    }

    override fun remove(disposable: Disposable) {
        state.updateAndGet{prev ->
            when(prev){
                is CoroutineState.Incomplete ->{
                    CoroutineState.Incomplete().from(prev).without(disposable)
                }
                is CoroutineState.Cancelling ->{
                    CoroutineState.Cancelling().from(prev).without(disposable)
                }
                is CoroutineState.Complete<*> -> prev
            }
        }
    }

    /**
     * 模拟协程Job函数的实现，主要是在状态的判断
     */
    override suspend fun join() {
        when(state.get()){
            is CoroutineState.Incomplete,
            is CoroutineState.Cancelling -> {
                return joinSuspend()
            }
            is CoroutineState.Complete<*> -> return
        }
    }

    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        doOnCompleted {result ->
            continuation.resume(Unit)
        }
    }

    override fun resumeWith(result: Result<T>) {
        val newState = state.updateAndGet{prevState ->
            when(prevState){
                is CoroutineState.Incomplete,
                is CoroutineState.Cancelling ->{
                    CoroutineState.Complete(result.getOrNull(),
                            result.exceptionOrNull()).from(prevState)
                }
                is CoroutineState.Complete<*> ->{
                    throw java.lang.IllegalStateException("Already Complete!")
                }
            }

        }
        newState.notifyCompletion(result)
        newState.clear()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun doOnCompleted(block : (Result<T>) -> Unit) : Disposable {
        val disposable  = CompletionHandleDispose(this,block)
        val newState = state.updateAndGet{ prev -> // 这个是一个接口，实际上这里传入的是给接口UnaryOperator的方法apply调用
            when(prev){
                is CoroutineState.Incomplete ->{
                    CoroutineState.Incomplete().from(prev).with(disposable)
                }
                is CoroutineState.Cancelling ->{
                    CoroutineState.Cancelling().from(prev).with(disposable)
                }
                is CoroutineState.Complete<*> -> prev
            }
        }

        (newState as? CoroutineState.Complete<T>)?.let {
            block(
                when{
                    it.value!=null -> Result.success(it.value)
                    it.exception!=null -> Result.failure(it.exception)
                    else -> throw  IllegalStateException("Wont`t Happen")
                }
            )
        }

        return disposable
    }
}