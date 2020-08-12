package book

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

/*
仿照Lua实现非对称api
* */

suspend fun main() {
    val producer = Coroutine.create<Unit,Int> {
        param :Unit ->
        for (i in 0..3){
            println("send: $i")
            yield(i)
        }
        200
    }

    val consumer = Coroutine.create<Int,Unit> {param : Int->

        println("start:$param")
        for (i in 0..3){
           val value = yield(Unit)
            println("receiver: $value")
        }
    }

    while (producer.isActive && consumer.isActive){
        val result = producer.resume(Unit)
        consumer.resume(result)
    }
}

/**
 * 是个挂起函数，但是并不会挂起
 */
suspend fun noSuspend() = suspendCoroutine<Int> {
    continuation ->
    continuation.resume(5)
}



class Coroutine<P,R>(
        override val context: CoroutineContext = EmptyCoroutineContext,
        private val block : suspend CoroutineScope<P,R>.(P)->R
):Continuation<R>{

    companion object{
        fun <P,R> create(
                context: CoroutineContext = EmptyCoroutineContext,
                block : suspend CoroutineScope<P,R>.(P)->R
        ): Coroutine<P,R>{
            return Coroutine(context , block)
        }
    }

    suspend fun resume(value : P) : R = suspendCoroutine {
        continuation ->
        val previousStatus = status.getAndUpdate{
            when(it){
                is Status.Created ->{
                    scope.parameters = value
                    Status.Resumed(continuation)
                }
                is Status.Yielded<*> ->{
                    Status.Resumed(continuation)
                }
                is Status.Resumed<*> ->{
                    throw IllegalAccessException("Already Resume!")
                }
                Status.Dead ->
                    throw IllegalAccessException("Already Dead!")
            }
        }
        when(previousStatus){
            is Status.Created->{
                previousStatus.continuation.resume(Unit)
            }
            is Status.Yielded<*>->{
                (previousStatus as? Status.Yielded<P>)?.continuation?.resume(value)
            }
        }
    }

    override fun resumeWith(result: Result<R>) {
        val previousStatus = status.getAndUpdate{
            when(it){
                is Status.Created ->
                    throw IllegalStateException("Never started!")
                is Status.Yielded<*> ->
                    throw IllegalStateException("Already yielded!")
                is Status.Resumed<*> ->
                    Status.Dead
                Status.Dead ->
                    throw IllegalAccessException("Already Dead!")
            }
        }

        (previousStatus as? Status.Resumed<R>)?.continuation?.resumeWith(result)
    }

    private val scope = object : CoroutineScope<P,R>{
        override var parameters: P? = null

        override suspend fun yield(value: R): P = suspendCoroutine {
            continuation ->
            val previousStatus = status.getAndUpdate{
                when(it){
                    is Status.Created ->
                        throw IllegalStateException("Never started!")
                    is Status.Yielded<*> ->
                        throw IllegalStateException("Already yielded!")
                    is Status.Resumed<*> ->
                        Status.Yielded(continuation)
                    Status.Dead ->
                        throw IllegalAccessException("Already Dead!")
                }
            }

            (previousStatus as? Status.Resumed<R>)?.continuation?.resume(value)
        }


    }

    private val status : AtomicReference<Status>

    val isActive : Boolean
    get() = status.get() != Status.Dead


    init {
        val coroutineBlock : suspend CoroutineScope<P,R>.() -> R = {
            block(parameters!!)
        }
        val start = coroutineBlock.createCoroutine(scope,this)

        status = AtomicReference(Status.Created(start))
    }



}

interface CoroutineScope<P,R>{
    val parameters : P?

    suspend fun yield(value : R):P
}

sealed class Status{
    class Created(val continuation: Continuation<Unit>) : Status()
    class Yielded<P>(val continuation: Continuation<P>) : Status()
    class Resumed<R>(val continuation: Continuation<R>) : Status()
    object Dead : Status()
}
