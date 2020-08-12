package book.three

import kotlin.concurrent.thread
import kotlin.coroutines.*

/*
* 对应《深入理解kotlin协程》中的kotlin协程拦截器的实现
*
* 1： 如何定义一个拦截器，并使用它
* */

suspend fun main() {

    suspend {
        suspendFunction_02("Hello","Kotlin")
        suspendFunction_02("Hello","Coroutine")
    }.startCoroutine(object : Continuation<Int>{
        override val context: CoroutineContext
            get() = LogInterceptor()

        override fun resumeWith(result: Result<Int>) {
            println(result)
        }

    })
}

suspend fun suspendFunction_01(a : Int){
    return
}

suspend fun suspendFunction_02(a : String , b : String) = suspendCoroutine<Int> {
    continuation ->
    thread {
        continuation.resumeWith(Result.success(5))
    }
}

/**
 * 拦截器interceptor返回的是Continuation<T>
 */
class LogInterceptor : ContinuationInterceptor{
    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> = LogContinuation(continuation)

}


class  LogContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> by continuation{
    override fun resumeWith(result: Result<T>) {
        println("before resumeWith $result")
        continuation.resumeWith(result)
        println("after resumeWith")
    }
}