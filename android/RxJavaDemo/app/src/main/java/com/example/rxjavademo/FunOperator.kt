package com.example.rxjavademo

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.RuntimeException
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
 *  基本上都是在相应的方法之前执行的
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

/**
 * onErrorReturn()
 * 发生了异常的事件的时候，该操作符会执行并返回一个特殊事件
 * 在onComplete()之前执行
 */
fun operatorByOnErrorReturn(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("onErrorReturn operator error"))
    }.onErrorReturn {
        // 对Throwable的处理
        Log.e(TAG , "execute onErrorReturn!!!")
        return@onErrorReturn 666
    }.subscribe(object : Observer<Int>{
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

/**
 * onErrorResumeNext()
 *  同时拦截Exception和Throwable，同样是拦截错误的，执行
 *  但是和onErrorReturn()不一样的是：onErrorResumeNext()返回的是一个Observable,而onErrorReturn()
 *  返回的是一个事件
 */
fun operatorByOnErrorResumeNext(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("onErrorReturn operator error"))
    }.onErrorResumeNext {
        // 对Throwable的处理
        Log.e(TAG , "execute onErrorResumeNext!!!")
        Observable.just(4,5,6)
    }.subscribe(object : Observer<Int>{
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

/**
 * return()方法的各个重载方法
 * <-- 1. retry（） -->
 *      作用：出现错误时，让被观察者重新发送数据
 *      注：若一直错误，则一直重新发送
 * <-- 2. retry（long time） -->
 *     作用：出现错误时，让被观察者重新发送数据（具备重试次数限制
 *     参数 = 重试次数
 * <-- 3. retry（Predicate predicate） -->
 *     作用：出现错误后，判断是否需要重新发送数据（若需要重新发送& 持续遇到错误，则持续重试）
 *     参数 = 判断逻辑
 * <--  4. retry（new BiPredicate<Integer, Throwable>） -->
 *     作用：出现错误后，判断是否需要重新发送数据（若需要重新发送 & 持续遇到错误，则持续重试
 *     参数 =  判断逻辑（传入当前重试次数 & 异常错误信息）
 * <-- 5. retry（long time,Predicate predicate） -->
 *     作用：出现错误后，判断是否需要重新发送数据（具备重试次数限制
 *     参数 = 设置重试次数 & 判断逻辑
 */
fun operatorByRetrySeries(){
    // retry()
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误了"))
    }.retry()
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
    // retry（long time）
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误了"))
    }.retry(3)  // times = 3表示重试的次数
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
    // retry（Predicate predicate）
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误"))
    }.retry { t ->
        Log.e(TAG , "执行错误retry（Predicate predicate）")
        true
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
    // retry（new BiPredicate<Integer, Throwable>）
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误"))
    }.retry { t1: Int?, t2: Throwable? ->
        Log.e(TAG , "retry（new BiPredicate<Integer, Throwable>）")
        true
    }
        .subscribe(object : Observer<Int> {
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext -- value : $t")
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError")
            }

        })
    // retry（long time,Predicate predicate）
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误"))
    }.retry (3){
        Log.e(TAG , "retry（long time,Predicate predicate）")
        true
    }
        .subscribe(object : Observer<Int> {
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext -- value : $t")
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError")
            }

        })
}

/**
 * retryUntil()
 * 出现错误后，判断是否需要重新发送数据
 * 若需要重新发送 & 持续遇到错误，则持续重试
 * 作用类似于retry（Predicate predicate）
 */
fun operatorByRetryUntil(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(RuntimeException("发生错误"))
    }.retryUntil {
        Log.e(TAG , "execute retryUntil() ")
        true
    }
        .subscribe(object : Observer<Int> {
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(t: Int?) {
                Log.d(TAG, "onNext -- value : $t")
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError")
            }

        })
}

/**
 * retryWhen()
 * 遇到错误时，将发生的错误传递给一个新的被观察者（Observable），
 * 并决定是否需要重新订阅原始被观察者（Observable）& 发送事件
 */
fun operatorByRetryWhen(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onError(Throwable("发生了第一个错误了"))
        it.onError(Throwable("发生了第二个错误了"))
    }.retryWhen {
        it.flatMap {
            // 可以通过flatMap获取整个Observable中的error事件进行逻辑处理
            return@flatMap Observable.timer(3,TimeUnit.SECONDS)
        }
    }.subscribe {

    }
}

/**
 * repeat()
 * 和retry()差不多，但是retry()触发的条件是error,而repeat的触发条件是complete()
 */
fun operatorByRepeat(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }.repeat()
        .subscribe {

        }

    //
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }.repeat(3)
        .subscribe {

        }
}

/**
 * repeatWhen()
 *
 */
fun operatorByRepeatWhen(){
    Observable.create<Int> {
        it.onNext(1)
        it.onNext(2)
        it.onNext(3)
        it.onComplete()
    }.repeatWhen {
        Observable.just(1)
    }
}
