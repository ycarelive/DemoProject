package book.three;

import book.CopyLuaCoroutineKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;

/**
 * 对应《深入理解kotlin协程》中的CPS变换
 */
public class CpsTransfer {

    public static void main(String[] args) {
        executeSuspendMethod();
    }

    /**
     * 在Java中执行Kotlin中定义的suspend函数，观察Continuation<T>
     * 在suspendCoroutine函数中是怎么来的
     *
     * 在该函数的调用中可以探究得出：为什么Kotlin语法要求挂起函数一定要运行在协程体内或者其他挂起函数中呢？
     *
     * 答案就是，任何一个协程体或者挂起函数中都有一个隐含的Continuation实例，编译器能够对这个实例进行正确传递
     * ，并将这个细节隐藏在协程的背后，让我们的异步代码看起来像同步代码一样。
     */
    public static void executeSuspendMethod(){
        // suspend()->Int类型的函数notSuspend在Java语言看来实际上是(Continuation<Integer>)->Object类型
        Object result = CopyLuaCoroutineKt.noSuspend(new Continuation<Integer>() {
            @NotNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NotNull Object o) {
                System.out.println(o);
            }
        });
        System.out.println(result);
    }

}



