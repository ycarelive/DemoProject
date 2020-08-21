package book.five

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * 对应《深入理解协程》delay声明
 */

private val executor = Executors.newScheduledThreadPool(1){runnable ->
    Thread(runnable , "Scheduler").apply { isDaemon = true }
}

/**
 * 模拟协程delay函数的实现
 */
suspend fun delay(time : Long , unit: TimeUnit = TimeUnit.MILLISECONDS){
    if (time <= 0){
        return
    }
    suspendCoroutine<Unit> {continuation ->
        executor.schedule({continuation.resume(Unit)},time,unit)
    }
}