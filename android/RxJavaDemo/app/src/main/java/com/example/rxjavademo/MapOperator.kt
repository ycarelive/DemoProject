package com.example.rxjavademo

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer

private const val Tag = "RxJava_Map_Operator"

/**
 * map()
 * 将序列中的事件进行转换，比如Int类型转换成String类型
 */
fun operatorByMap(){
    val observable = Observable.range(0,10)
    observable.map {
        return@map "map转化后的值为：$$it"
    }.subscribe {
        Log.e(Tag , it)
    }
}

/**
 * flatMap()
 * flatMap()与map()函数的区别就是返回的类型不同
 * --map(): 返回的是数值的类型，所以map()的作用是将数值进行转换
 * --flapMap(): 返回的是一个Observable对象，这个对象中可以是一个或者多个数据，那么这样就可以理解为flatMap()函数
 *   是由原始Observable传递过来的各个事件关联，生成多个对应的Observables，并将Observables合并成一个最后的
 *   Observable，然后将最终的Observable中的事件分发给观察者Observer
 * <p>理论上flatMap()最终组装后的数据是无序的(针对源数据序列来说)，即最终的数据序列与原序列顺序没有关系</p>
 */
fun operatorByFlatMap(){
    val observable = Observable.range(0,3)
    observable.flatMap {
        return@flatMap Observable.just("$it 中拆分后的子事件 1","$it 中拆分后的子事件 2"
            ,"$it 中拆分后的子事件 3")
    }.subscribe{
        Log.e(Tag , it)
    }
}

/**
 * concatMap()
 * concatMap和 flatMap()差不多，都是返回Observable对象，只是concatMap最终返回的数据是有序的(同样是
 * 相对于源数据来说的)
 */
fun operatorByConcatMap(){
    val observable = Observable.range(0,3)
    observable.concatMap {
        return@concatMap Observable.just("$it 中拆分后的子事件 1","$it 中拆分后的子事件 2"
            ,"$it 中拆分后的子事件 3")
    }.subscribe {
        Log.e(Tag , it)
    }
}

/**
 * buffer()
 * 参数说明：参数1表示缓冲区大小，参数2表示缓冲区的移动步长，每次执行OnNext，缓冲区都会向后移动设置的步长长度
 */
fun operatorByBuffer(){
    val observable = Observable.range(0,5)
    observable.buffer(3,1)
        .subscribe{
            Log.e(Tag , " 缓存区里的事件数量 =  ${it.size}")
            for (value in it){
                Log.e(Tag , " 事件= $value" )
            }
        }
}


