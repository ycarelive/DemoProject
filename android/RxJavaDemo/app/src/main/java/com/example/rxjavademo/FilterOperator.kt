package com.example.rxjavademo

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * 过滤操作符
 *
 */


private const val TAG = "FilterOperator"

/**
 * filter()
 *
 * filter operator
 */
fun operatorByFilter(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }.filter {
        // a. 返回true，则继续发送
        // b. 返回false，则不发送（即过滤）
        it < 3
    }.subscribe {
        Log.e( TAG , "finally value $it")
    }
}

/**
 * ofType()
 * 对于ofType是用来过滤数据类型用的
 * 需要注意的是：它的作用是把需要的过滤出来
 */
fun operatorByOfType(){
    Observable.just("1",2,"3",4)
        .ofType(String::class.java)
        .subscribe {
            Log.e( TAG , "finally value $it")
        }
}

/**
 * skip()/skipLast()
 * 跳过某个事件/某些事件
 */
fun operatorBySkip(){
    Observable.just(1,2,3,4)
        .skip(1) // 跳过序列的前n个事件，n取决于传入的参数，比如n=2时，那么最终传递的序列为3，4
        .subscribe { Log.e( TAG , "finally value $it") }

    Observable.just(1,2,3,4)
        .skipLast(1) // 跳过序列的后n个事件，n取决于传入的参数，比如n=2时，那么最终传递的序列为1，2
        .subscribe { Log.e( TAG , "finally value $it") }
}

/**
 * distinct()：过滤掉多余的，重复的元素
 * distinctUntilChanged()：过滤掉重复和连续重复的多余元素
 * 过滤事件序列中的重复的事件  / 连续重复的事件
 */
fun operatorByDistinct(){
    // 使用distinct()
    Observable.just(1,2,1,3,3,3,4,5)
        .distinct()
        .subscribe {
            Log.e( TAG , "finally value $it")
        }

    Log.e( TAG , "distinctUntilChanged ->>>>>>>>>>")
    // distinctUntilChanged()
    Observable.just(1,2,3,3,3,4,5)
        .distinctUntilChanged()
        .subscribe {
            Log.e( TAG , "finally value $it")
        }
}

/**
 *  take() 和 takeLast()
 *  作用：指定观察者只能接受多少个事件
 *  takeLast()表示观察者这能接受最后多少个事件
 *
 *  在实际的功能上类似skip()和skipLast()
 */
fun operatorByTake(){
    // 使用take()
    Observable.just(1,2,1,3,3,3,4,5)
        .take(3)
        .subscribe {
            Log.e( TAG , "finally value $it")
        }

    Log.e( TAG , "distinctUntilChanged ->>>>>>>>>>")
    // distinctUntilChanged()
    Observable.just(1,2,3,3,3,4,5)
        .takeLast(3)
        .subscribe {
            Log.e( TAG , "finally value $it")
        }
}


/**
 * throttleFirst（）/ throttleLast（）
 * 在指定时间频段内，选取第一个时间和最后一个事件
 *
 */
fun operatorByThrottle(){
    /**
     * throttleFirst,设置的频率为1秒
     * 则：对于每一秒内都取第一个事件，分别是1，4，7
     */
    Observable.create<Int> {
        it.onNext(1)
        Thread.sleep(500)
        it.onNext(2)
        Thread.sleep(400)
        it.onNext(3)
        Thread.sleep(300)
        it.onNext(4)
        Thread.sleep(300)
        it.onNext(5)
        Thread.sleep(300)
        it.onNext(6)
        Thread.sleep(400)
        it.onNext(7)
        Thread.sleep(300)
        it.onNext(8)
        Thread.sleep(300)
        it.onNext(9)
        Thread.sleep(300)
        it.onComplete()
    }.throttleFirst(1,TimeUnit.SECONDS)
        .subscribe {
            Log.e( TAG , "finally value $it")
        }

    /**
     * 对于throttleLast差不多一样，只不过是去单位频率时间内的最后一个
     * 所以分别是3，6，9
     */
    Observable.create<Int> {
        it.onNext(1)
        Thread.sleep(500)
        it.onNext(2)
        Thread.sleep(400)
        it.onNext(3)
        Thread.sleep(300)
        it.onNext(4)
        Thread.sleep(300)
        it.onNext(5)
        Thread.sleep(300)
        it.onNext(6)
        Thread.sleep(400)
        it.onNext(7)
        Thread.sleep(300)
        it.onNext(8)
        Thread.sleep(300)
        it.onNext(9)
        Thread.sleep(300)
        it.onComplete()
    }.throttleLast(1,TimeUnit.SECONDS)
        .subscribe {
            Log.e( TAG , "finally value $it")
        }
}

/**
 * 功能与throttleLast一致
 */
fun operatorBySimple(){
    // 参照throttleLast
}


/**
 * throttleWithTimeout （） / debounce（）
 * 发送数据事件时，若2次发送事件的间隔＜指定时间，就会丢弃前一次的数据，直到指定时间内都没有新数据发射时才会发送后一次的数据
 */

/**
 * firstElement（） / lastElement（）
 * 作用
 * 仅选取第1个元素 / 最后一个元素
 */
fun operatorByLocation(){
    Observable.just(1,2,3,4)
        .firstElement()
        .subscribe{
            Log.e( TAG , "finally value $it")
        }

    Observable.just(1,2,3,4)
        .lastElement()
        .subscribe{
            Log.e( TAG , "finally value $it")
        }
}

/**
 * elementAt（）
 * 指定接收某个元素（通过 索引值 确定）
 * 注：允许越界，即获取的位置索引 ＞ 发送事件序列长度
 */


/**
 * elementAtOrError（）
 * 在elementAt（）的基础上，当出现越界情况（即获取的位置索引 ＞ 发送事件序列长度）时，即抛出异常
 */



