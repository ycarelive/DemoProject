package book.five

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


/**
 * 对应《深入理解kotlin协程》中 协程的创建
 */


fun main() {

}

//fun launch(
//        context: CoroutineContext=EmptyCoroutineContext,
//        block : suspend ()->Unit
//):Job {
//    val completion = StandaloneCoroutine(context)
//    block.startCoroutine(completion)
//    return completion
//}

