import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun sumFromZeroTo(n: Long): Long =
    n * (n + 1) / 2

fun launchCoroutine(
    executor: ExecutorService,
    doneSignal: CountDownLatch,
    block: suspend () -> Unit
) {
    executor.execute {
        block.startCoroutine(object : Continuation<Unit> {
            override val context: CoroutineContext = EmptyCoroutineContext

            override fun resumeWith(result: Result<Unit>) {
                result.exceptionOrNull()?.printStackTrace()
                doneSignal.countDown()
            }
        })
    }
}

fun main() {
    val nQueue = ConcurrentLinkedQueue(listOf(10L, 25L, 50L, 100L))
    val executor = Executors.newFixedThreadPool(4)
    val doneSignal = CountDownLatch(4)

    for (coroutineId in 1..4) {
        launchCoroutine(executor, doneSignal) {
            val n = nQueue.poll()

            if (n != null) {
                val sum = sumFromZeroTo(n)
                println("Corutina $coroutineId: suma de la 0 la $n = $sum")
            } else {
                println("Corutina $coroutineId: coada este goala")
            }
        }
    }

    doneSignal.await()
    executor.shutdown()
}
