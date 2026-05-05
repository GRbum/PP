import java.util.concurrent.ArrayBlockingQueue
import kotlin.concurrent.thread
import kotlin.io.path.Path
import kotlin.io.path.readText

data class IntAdt(private val elements: List<Int>) {
    fun multiplyBy(alpha: Int): IntAdt =
        IntAdt(elements.map { it * alpha })

    fun sorted(): IntAdt =
        IntAdt(elements.sorted())

    override fun toString(): String =
        elements.joinToString(prefix = "[", postfix = "]")
}

fun readIntAdtFromFile(fileName: String): IntAdt {
    val numbers = Path(fileName)
        .readText()
        .split(Regex("[,\\s]+"))
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    return IntAdt(numbers)
}

fun main() {
    val v = readIntAdtFromFile("ex2_in.txt")
    val alpha = 3

    val multiplyToSortPipe = ArrayBlockingQueue<IntAdt>(1)
    val sortToPrintPipe = ArrayBlockingQueue<IntAdt>(1)

    val multiplier = thread(name = "Multiplier") {
        val result = v.multiplyBy(alpha)
        println("${Thread.currentThread().name}: V * $alpha = $result")
        multiplyToSortPipe.put(result)
    }

    val sorter = thread(name = "Sorter") {
        val received = multiplyToSortPipe.take()
        val result = received.sorted()
        println("${Thread.currentThread().name}: vector ordonat = $result")
        sortToPrintPipe.put(result)
    }

    val printer = thread(name = "Printer") {
        val result = sortToPrintPipe.take()
        println("${Thread.currentThread().name}: rezultat final = $result")
    }

    multiplier.join()
    sorter.join()
    printer.join()
}
