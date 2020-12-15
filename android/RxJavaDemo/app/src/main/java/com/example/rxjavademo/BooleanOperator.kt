package com.example.rxjavademo

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import java.util.concurrent.TimeUnit

/**
 * 逻辑操作符
 */

private const val TAG = "BooleanOperator"

/**
 * all()
 * 判断发送的每项数据是否都满足 设置的函数条件
 * 若满足，返回 true；否则，返回 false
 */
fun operatorByAll(){
    Observable.just(1,2,3,4)
        .all {
            it < 10
        }.subscribe { t1: Boolean?, t2: Throwable? ->
            Log.e(TAG , "result is $t1")
        }
}

/**
 * takeWhile()
 * 判断发送的每项数据是否满足 设置函数条件
 * 若发送的数据满足该条件，则发送该项数据；否则不发送
 */
fun operatorByTakeWhile(){
    Observable.interval(1,TimeUnit.SECONDS)
        .takeWhile {
            it < 4
        }.subscribe {
            Log.e(TAG , "result is $it")
        }
}

/**
 * skipWhile()
 * 判断发送的每项数据是否满足 设置函数条件
 * 直到该判断条件 = false时，才开始发送Observable的数据
 * 当条件成立时表示事件被忽略
 */
fun operatorBySkipWhile(){
    Observable.interval(1,TimeUnit.SECONDS)
        .skipWhile{
            // 直到判断条件不成立 = false = 发射的数据≥5，才开始发送数据
            it < 5
        }.subscribe {
            Log.e(TAG , "result is $it")
        }
}

/**
 * takeUntil()
 * 执行到某个条件时，停止发送事件。具体使用如下
 */
fun operatorByTakeUntil(){
    Observable.interval(1,TimeUnit.SECONDS)
        .takeUntil {
            // 返回true时，就停止发送事件
            // 当发送的数据满足>5时，就停止发送Observable的数据
            it > 5
        }.subscribe {
            Log.e(TAG , "result is $it")
        }
}

/**
 * skipUntil()
 * 等到 skipUntil（） 传入的Observable开始发送数据，（原始）第1个Observable的数据才开始发送数据
 */
fun operatorSkipUntil(){
    Observable.interval(1,TimeUnit.SECONDS)
        .skipUntil(Observable.timer(5,TimeUnit.SECONDS))
        .subscribe {
            Log.e(TAG , "result is $it")
        }
}

/**
 * SequenceEqual()
 * 判定两个Observables需要发送的数据是否相同
 */
fun operatorBySequenceEqual(){
    Observable.sequenceEqual(
        Observable.just(1,2,3),
        Observable.just(1,2,3)
    ).subscribe { t1: Boolean?, t2: Throwable? ->
        Log.e(TAG , "2个Observable是否相同: $t1")
    }
}

/**
 * contains()
 * 判断发送的数据中是否包含指定数据
 */
fun operatorByContains(){
    Observable.just(1,2,3,4)
        .contains(4)
        .subscribe { t1: Boolean?, t2: Throwable? ->
            Log.e(TAG , "result is $t1")
        }
}

/**
 * isEmpty()
 * 判断发送的数据是否为空
 */
fun operatorByIsEmpty(){
    Observable.just(1,2,3,4)
        .isEmpty
        .subscribe { t1: Boolean?, _: Throwable? ->
            Log.e(TAG , "result is $t1" )
        }
}

/**
 * amb()
 * 当需要发送多个 Observable时，只发送 先发送数据的Observable的数据，而其余 Observable则被丢弃
 *
 */
fun operatorByAmb(){
    val list : MutableList<ObservableSource<Int>> = mutableListOf()
    // kotlin 中没有了add的方法等方法 ， 感觉更加不方便。。
    list[0] = Observable.just(1,2,3).delay(1,TimeUnit.SECONDS)
    list[1] = Observable.just(6,7,8)
    Observable.amb(list)
        .subscribe {
            Log.e(TAG , "result is $it" )
        }
}