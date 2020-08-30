package book.five.exception.handler

import book.five.launch
import java.lang.ArithmeticException
import java.lang.IllegalStateException
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


suspend fun main() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("[exceptionHandler] ${throwable.message}")
    }

    launch(exceptionHandler) {
        println("1")
        throw ArithmeticException("div by 0")
        println("2")
    }.join()
//    suspend {
//        throw ArithmeticException("div by 0")
//    }.startCoroutine(object : Continuation<Unit>{
//        override val context: CoroutineContext
//            get() = EmptyCoroutineContext
//
//        override fun resumeWith(result: Result<Unit>) {
//            println(result)
//        }
//
//    })

}