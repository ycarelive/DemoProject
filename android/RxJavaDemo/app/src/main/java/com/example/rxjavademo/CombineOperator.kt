package com.example.rxjavademo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction
import java.util.function.Supplier
import kotlin.concurrent.thread

private const val TAG = "CombineOperator"

/**
 * concat()
 * 合并对个Observables成一个Observable
 * 但是concat()有限制，最多只能合并4个Observable
 */
fun operatorByConcat(){
    val observable = Observable.concat(Observable.just(1,2)
        ,Observable.just(3,4),Observable.just(5,6)
        ,Observable.just(7,8))
        .subscribe{
            Log.e(TAG,"Combine Operator value : $it")
        }
}

/**
 * concatArray()
 * 和concat()功能是一致的，只是解除了concat操作符只能合并4个的限制
 */
fun operatorByConcatArray(){
    val observable = Observable.concatArray(Observable.just(1,2)
        ,Observable.just(3,4),Observable.just(5,6)
        ,Observable.just(7,8),Observable.just(9,10))
        .subscribe{
            Log.e(TAG,"Combine Operator value : $it")
        }
}

/**
 * merge()
 * 功能和concat()一致的，都是将多个Observables组合成一个Observable，但是merge()是并行的，而concat是串行的
 * 同样的，对于merge()也有参数的个数限制，同样不能超过4个
 *
 */
fun operatorByMerge(){
    val observable = Observable.merge(Observable.intervalRange(0,3,1,1,TimeUnit.SECONDS)
        ,Observable.intervalRange(3,3,1,1,TimeUnit.SECONDS))
        .subscribe{
            Log.e(TAG,"Combine Operator value : $it")
        }
}

/**
 * mergeArray()
 * 跟merge()一样的，但是解除了参数个数的限制，可以大于4个
 */
fun operatorByMergeArray(){
    // 写法跟merge()一样
}

/**
 * concatArrayDelayError()
 * <p>作用：也是将多个Observables进行合并，但是多了对于异常的处理，concat()在出现异常的</p>
 */
fun operatorByConcatDelayError(){
    /**
     * 使用普通的concat操作符，但是在发射时间的时候包含了一个异常时间
     * 对于concat(),会被异常中断，导致第二个observable无法被执行
     */
    val observable1 = Observable.concat(Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("concat exception"))
    }, Observable.range(4, 3))
        .subscribe(object : Observer<Int>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable?) {}
            override fun onNext(t: Int?) {
                Log.e(TAG , "concat error value : $t")
            }
            override fun onError(e: Throwable?) {
                Log.e(TAG , e.toString())
            }

        })

    val observable2 = Observable.concatArrayDelayError(Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("concat exception"))
    }, Observable.range(4, 3))
        .subscribe(object : Observer<Int>{
        override fun onComplete() {}
        override fun onSubscribe(d: Disposable?) {}
        override fun onNext(t: Int?) {
            Log.e(TAG , "concat error value : $t")
        }
        override fun onError(e: Throwable?) {
            Log.e(TAG , e.toString())
        }

    })
}

/**
 * mergeArrayDelayError()
 *
 */
fun operatorByMergeArrayDelayError(){
    // 用法和concatArrayDelayError()一样
}

/**
 *  zip()
 *  1.事件组合方式 = 严格按照原先事件序列 进行对位合并
 *  2.最终合并的事件数量 = 多个被观察者（Observable）中数量最少的数量
 *  3.尽管被观察者2的事件D没有事件与其合并，但还是会继续发送
 *  4.若在被观察者1 & 被观察者2的事件序列最后发送onComplete()事件，则被观察者2的事件D也不会发送
 */
fun operatorByZip(){
    val observable1 = Observable.create<Int> {
        it.onNext(1)
        Thread.sleep(1000)
        it.onNext(2)
        Thread.sleep(1000)
        it.onNext(3)
        Thread.sleep(100)
        it.onComplete()
    }
    val observable2 = Observable.create<String> {
        it.onNext("A")
        Thread.sleep(1000)
        it.onNext("B")
        Thread.sleep(1000)
        it.onNext("C")
        Thread.sleep(1000)
        it.onNext("D")
        it.onComplete()
    }
    val observable = Observable.zip(observable1, observable2 , @RequiresApi(Build.VERSION_CODES.N)
    object : BiFunction<Int,String,String>,
        @io.reactivex.rxjava3.annotations.NonNull io.reactivex.rxjava3.functions.BiFunction<Int, String, Any> {
        override fun apply(t: Int, u: String): String {
            return "finally value : $t - $u"
        }
    }).subscribe(object : Observer<Any?>{
        override fun onComplete() {
            Log.e(TAG , "onComplete")
        }
        override fun onSubscribe(d: Disposable?) {

        }
        override fun onNext(t: Any?) {
            Log.e(TAG , "onNext value : $t")
        }
        override fun onError(e: Throwable?) {
        }

    })
}

/**
 * reduce()
 * 将Observable内的数据进行聚合，合成最终的数据，再最终传递出事件
 */
fun operatorByReduce(){
    Observable.just(1,2,3,4)
        .reduce{ t1: Int?, t2: Int? ->
            Log.e(TAG, "本次计算的数据是： $t1 乘 $t2")
            return@reduce t1!! * t2!!
        }.subscribe{
            Log.e(TAG , "最后的数据为：$it")
        }
}

/**
 * collect()
 * 将Observable中的数据填装到某个容器中，填装和容器由开发者自定义
 */
fun operatorByCollect(){
//    Observable.just(1,2,3,4)
//        .collect()
}

/**
 * startWith()
 * 在Observable前面追加Observable或者追加一些数据
 */
fun operatorByStartWith(){
    Observable.just(1,2,3,4)
        .startWith(Observable.just(5,6))
        .startWithArray(7,8)
        .subscribe{

        }

}

/**
 * count()
 * 统计Observable中的事件个数
 */
fun operatorByCount(){
    Observable.just(1,2,3,4)
        .count()

}

