package book.five.cancel.coroutine

import book.five.Disposable
import book.five.Job
import book.five.OnCancel

/**
 * 取消回调的控制对象
 */
class CancellationHandlerDisposable (
        val job: Job,
        val onCancel: OnCancel
) : Disposable {
    override fun disposable() {
        job.remove(this)
    }
}