package blog.model

import kotlinx.coroutines.*
import kotlin.coroutines.*

suspend fun main() {

//    GlobalScope.launch {
//        log(1)
//    }
//    log(2)

//    var list : List<Int> = listOf(1,2,3)
//    list.take(2)
//

//
//
//
    var continuation = suspend {
        println("suspend action")
        5
    }.createCoroutine(object : Continuation<Int>{
        override fun resumeWith(result: Result<Int>) {
           println(result)
        }

        override val context: CoroutineContext
            get() = EmptyCoroutineContext

    })
//    continuation.resume(Unit)
    //callLaunchCoroutine()
//    continuation.resumeWith(Result.success(Unit))
//
//    GlobalScope.launch(CoroutineName("test-01")+ CoroutineExceptionHandler { coroutineContext, throwable ->
//
//    }) {
//
//    }





}

class a{
    fun b(int: Int) = demo {
        val m = it + ""
        val a = 10
        a+int
    }
}

fun demo(block : (String) -> Int){
    block("haha")
}

suspend fun test() = suspendCoroutine<Int> {

}

class ProducerScope<T>{
    var a : Int = 0
    suspend fun produce(value : T){

    }
}

fun <R,T> launchContinue(receiver: R , block : suspend R.()-> T ){
    block.startCoroutine(receiver , object : Continuation<T>{
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<T>) {
            println(result)
        }

    })
}

fun callLaunchCoroutine(){
    val scope = ProducerScope<Int>()
    scope.a = 10
    launchContinue(scope){
        println(this)
        delay(1000)
        5

    }
}

fun <R,T> test(block: R.() -> T){

}
