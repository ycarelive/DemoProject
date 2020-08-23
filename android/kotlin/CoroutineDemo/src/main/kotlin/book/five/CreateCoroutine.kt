package book.five

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


/**
 * 对应《深入理解kotlin协程》中 5.3 协程的创建
 */


fun main() {

}


/**
 * 无返回值的协程 launch函数
 */
fun launch(
        context: CoroutineContext=EmptyCoroutineContext,
        block : suspend ()->Unit
):Job {
    val completion = StandLoneCoroutine(newCoroutineContext(context))
    block.startCoroutine(completion)
    return completion
}


class StandLoneCoroutine(context: CoroutineContext ) : AbstractCoroutine<Unit>(context)

class  CompletionHandleDispose<T>(
        val job: Job ,
        val onComplete : (Result<T>) -> Unit
) : Disposable{
    override fun disposable() {
        job.remove(this)
    }
}

