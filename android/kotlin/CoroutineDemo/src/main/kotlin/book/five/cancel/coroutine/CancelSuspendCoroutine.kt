package book.five.cancel.coroutine

import kotlinx.coroutines.CancellableContinuation
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * 对应《深入理解协程》
 * 支持取消的挂起函数
 */

/**
 * 不支持取消的挂起函数
 */
suspend fun nonCancellationFunction() = suspendCoroutine<Int> {
    continuation ->

    val completableFuture = CompletableFuture.supplyAsync{

    }

    completableFuture.thenApply {
        continuation.resume(5)
    }.exceptionally {
        continuation.resumeWithException(it)
    }
}


//suspend fun cancellableFunction()
//
//
//suspend fun <T> suspendCancelCoroutine(
//    crossinline block : (CancellableContinuation)
//): T {
//
//}