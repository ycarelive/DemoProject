package com.example.rxjavademo

import android.nfc.Tag
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable


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