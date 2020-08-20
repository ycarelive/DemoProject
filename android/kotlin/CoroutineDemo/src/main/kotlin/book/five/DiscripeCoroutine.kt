package book.five

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

sealed class DisposableList {
    object Nil : DisposableList()
    class Cons(
            val head : Disposable,
            val tail : DisposableList
    ): DisposableList()
}

fun DisposableList.remove(disposable: Disposable) : DisposableList{
    return when(this){
        DisposableList.Nil -> this
        is DisposableList.Cons ->{
            if (head == disposable){
                return tail
            }else {
                DisposableList.Cons(head,tail.remove(disposable))
            }
        }
    }
}

tailrec fun DisposableList.forEach(action: (Disposable) -> Unit): Unit =
        when (this) {
            DisposableList.Nil -> Unit
            is DisposableList.Cons -> {
                action(this.head)
                this.tail.forEach(action)
            }
        }

inline fun <reified T : Disposable> DisposableList.loopOn(
        crossinline action: (T) -> Unit
) = forEach {
    when(it){
        is T ->{
            action(it)
        }
    }
}

interface Job : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<Job>

    override val key: CoroutineContext.Key<*>
        get() = Job

    val isActive : Boolean

    fun invokeOnCancel(onCancel : OnCancel) : Disposable

    fun invokeOnComplete(onComplete: OnComplete) : Disposable

    fun cancel()

    fun remove(disposable : Disposable)

    suspend fun join()

}

sealed class CoroutineState{

    private var disposableList : DisposableList = DisposableList.Nil

    fun from(state: CoroutineState) : CoroutineState {
        this.disposableList = state.disposableList
        return this
    }

    fun with(disposable: Disposable) : CoroutineState{
        this.disposableList = DisposableList.Cons(disposable,this.disposableList)
        return this
    }

    fun without(disposable: Disposable) : CoroutineState{
        this.disposableList = this.disposableList.remove(disposable)
        return this
    }

    fun clear(){
        this.disposableList = DisposableList.Nil
    }


    class Incomplete : CoroutineState()
    class Cancelling : CoroutineState()
    class Complete<T>(val value : T? =null , val exception: Throwable?=null):CoroutineState()
}

interface Disposable{
    fun disposable()
}

interface OnCancel {

    fun onCancel()

}

interface OnComplete{
    fun onComplete()
}