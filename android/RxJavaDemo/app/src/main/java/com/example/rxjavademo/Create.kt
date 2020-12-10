package com.example.rxjavademo

import android.nfc.Tag
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

/**
 * 创建操作符相关
 */


/*
* Observable的正常创建方式*/

private val Tag : String = "RxJava--Create"

// 用最简单和最普通的方式创建Observable
fun normalCreate(){
    /**
     * <p>1. 最简单的实现一个Observable和Observer</p>
     */
    val observable : Observable<Int> = Observable.create{emitter ->
        emitter.onNext(1)
        emitter.onNext(2)
        emitter.onNext(3)
        emitter.onComplete()
    }
    val observer : Observer<Int> = object : Observer<Int>{
        override fun onComplete() {
            Log.e(Tag , "执行onComplete()")
        }

        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag , "执行onSubscribe")
        }

        override fun onNext(t: Int?) {
            Log.e(Tag , "执行onNext() 值为:$t")
        }

        override fun onError(e: Throwable?) {
            Log.e(Tag , "执行onError")
        }

    }
    observable.subscribe(observer)
}

/**
 * <p>Just操作符能快速创建一个Observable对象，内部会调用onNext</p>
 * <p>以just方式实现创建Observable
 * 需要注意的是Just创建符最多只能使用10个参数</p>
 *
 * <p>如果传入的参数超过10个，那么just函数会报错</p>
 */
fun createByJust() {
    val observable = Observable.just("1", "2", "3")
    val observer = object : Observer<String> {
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }

        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }

        override fun onNext(t: String?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }

        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/**
 * 通过fromArray操作符快捷创建一个Observable
 * <p>由操作符顾名思义，fromArray就是将一个</p>
 *
 * <p>数组可以是任意类型</p>
 */
fun createByFromArray(){
    val array = arrayOf(1,2,3,4,5)
    val observable = Observable.fromArray(*array)
    val observer  = object : Observer<Int>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }

        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }

        override fun onNext(t: Int?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }

        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }


    }
    observable.subscribe(observer)
}

/**
 * <p>能够将iterable接口的数据转化成Observable</p>
 */
fun createByIterable(){
    val list = listOf(1,2,3,4,5)
    val observable = Observable.fromIterable(list)
    val observer = object : Observer<Int>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Int?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/**
 * <p>空创建，一般只是只会传递一个onComplete,通常情况下用来测试使用</p>
 */
fun createByEmpty(){
    val observable = Observable.empty<Int>()
    val observer = object : Observer<Int>{
        override fun onComplete() {
            TODO("Not yet implemented")
        }

        override fun onSubscribe(d: Disposable?) {
            TODO("Not yet implemented")
        }

        override fun onNext(t: Int?) {
            TODO("Not yet implemented")
        }

        override fun onError(e: Throwable?) {
            TODO("Not yet implemented")
        }

    }
    observable.subscribe(observer)
}

/**
 * 仅发送error事件，直接通知异常
 */
fun createByError(){
    val observable = Observable.error<Int>(RuntimeException())
    val observer = object : Observer<Int>{
        override fun onComplete() {
            TODO("Not yet implemented")
        }

        override fun onSubscribe(d: Disposable?) {
            TODO("Not yet implemented")
        }

        override fun onNext(t: Int?) {
            TODO("Not yet implemented")
        }

        override fun onError(e: Throwable?) {
            TODO("Not yet implemented")
        }

    }
    observable.subscribe(observer)
}

/**
 * <p>不发送通知事件</p>
 */
fun createByNever(){
    val observable = Observable.never<Int>()
    val observer = object : Observer<Int>{
        override fun onComplete() {
            TODO("Not yet implemented")
        }

        override fun onSubscribe(d: Disposable?) {
            TODO("Not yet implemented")
        }

        override fun onNext(t: Int?) {
            TODO("Not yet implemented")
        }

        override fun onError(e: Throwable?) {
            TODO("Not yet implemented")
        }

    }
    observable.subscribe(observer)
}

/*Observable 的延迟创建******************/

/**
 * defer()
 * <p>作用：直到有观察者(observer)订阅时，被观察者(observable)才被创建并发送事件</p>
 * <p>1. 通过 Observable工厂方法创建被观察者对象（Observable）</p>
 * <p>2. 每次订阅后，都会得到一个刚创建的最新的Observable对象，这可以确保Observable对象里的数据是最新的</p>
 * 应用场景:
 * 动态创建被观察者对象（Observable） & 获取最新的Observable对象数据
 */
fun createByDefer(){
    var i = 1
    val observable = Observable.defer {
        return@defer Observable.just(i)
    }
    i = 2
    val observer = object : Observer<Int>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Int?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/**
 * timer()
 * <p>延迟n秒后发送一个事件，通常为事件0，n秒取决于设定</p>
 * <p>timer()操作符通常情况下运行在新的线程中，但是也可以通过第三个参数Schedule配置运行具体的线程</p>
 * <p>timer生成的Observable类型是Long</p>
 * <p>一般情况下用户检测</p>
 */
fun createByTimer(){
    val observable = Observable.timer(2,TimeUnit.SECONDS)
    val observer = object : Observer<Long>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Long?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/**
 * interval()
 * <p>快速创建1个被观察者对象（Observable）</p>
 * <p>发送事件的特点：每隔指定时间 就发送 事件</p>
 * <p>发送的事件序列 = 从0开始、无限递增1的的整数序列</p>
 * <p>数据类型为Long</p>
 */
fun createByInterVal(){
    val observable = Observable.interval(2,1,TimeUnit.SECONDS)
    val observer = object : Observer<Long>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Long?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/**
 * intervalRange()
 * 相比于Interval操作符，加了两个参数：事件序列的起始点和事件的数量
 */
fun createByIntervalRange(){
    val observable = Observable.intervalRange(3,13,2,1,TimeUnit.SECONDS)
    val observer = object : Observer<Long>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Long?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}

/** Observable 的无延迟有序列创建*************/

/**
 * range()
 * 创建一个无延迟的序列Observable
 * 类型为Int
 */
fun createByRange(){
    val observable = Observable.range(1,10)
    val observer = object : Observer<Int>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Int?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)

}

/**
 * rangeLong()
 * 无延迟序列和Range操作符相似，只是类型为Long
 */
fun createByRangeLong(){
    val observable = Observable.rangeLong(1,10)
    val observer = object : Observer<Long>{
        override fun onComplete() {
            Log.e(Tag, "执行onComplete()")
        }
        override fun onSubscribe(d: Disposable?) {
            Log.e(Tag, "执行onSubscribe")
        }
        override fun onNext(t: Long?) {
            Log.e(Tag, "执行onNext() 值为:$t")
        }
        override fun onError(e: Throwable?) {
            Log.e(Tag, "执行onError")

        }
    }
    observable.subscribe(observer)
}