package com.example.rxjavademo

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 功能操作符
 */

private const val TAG = "FunOperator"

/**
 * delay()
 * 延迟操作
 */
fun operatorByDelay(){
    Observable.just(1,2,3,4)
        .delay(2,TimeUnit.SECONDS)
        .subscribe {
            // ....
        }
}

/**
 * do系列操作符
 *
 */
fun operatorByDoSeries(){
    Observable.create<Int>{ emitter ->
        emitter.onNext(1)
        emitter.onNext(2)
        emitter.onNext(3)
        emitter.onError(Throwable("发生错误了"));
    }
        // 1. 当Observable每发送1次数据事件就会调用1次
        .doOnEach {
            Log.d(TAG, "doOnEach: ${it.value}" )
        }
        // 2. 执行Next事件前调用
        .doOnNext {
            Log.d(TAG, "doOnNext: $it" )
        }
        // 3. 执行Next事件后调用
        .doAfterNext {
            Log.d(TAG, "doAfterNext: $it" )
        }
        // 4. Observable正常发送事件完毕后调用
        .doOnComplete {
            Log.d(TAG, "doOnComplete" )
        }
        // 5. Observable发送错误事件时调用
        .doOnError {
            Log.d(TAG, "doOnError" )
        }
        // 6. 观察者订阅时调用
        .doOnSubscribe {
            Log.d(TAG, "doOnSubscribe" )
        }
        // 7. Observable发送事件完毕后调用，无论正常发送完毕 / 异常终止
        .doOnTerminate {
            Log.d(TAG, "doOnTerminate" )
        }
        // 8. 最后执行
        .doFinally {
            Log.d(TAG, "doFinally" )
        }
        .subscribe(object : Observer<Int>{
            override fun onComplete() {
                Log.d(TAG, "onComplete" )
            }

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe" )
            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext -- value : $t" )
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError" )
            }

        })
}