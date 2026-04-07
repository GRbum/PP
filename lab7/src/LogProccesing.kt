import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class HistoryLogRecord(
    val timestamp: Long,
    val commandLine: String,
    val startDateRaw: String
) : Comparable<HistoryLogRecord> {

    override fun compareTo(other: HistoryLogRecord): Int {
        return this.timestamp.compareTo(other.timestamp)
    }
}

fun parseStartDateToTimestamp(startDate: String): Long {
    val normalized = startDate.trim().replace(Regex("\\s+"), " ")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(normalized, formatter)

    return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun parseHistoryBlock(block: String): HistoryLogRecord? {
    var startDate: String? = null
    var commandLine: String? = null

    for (line in block.lines()) {
        when {
            line.startsWith("Start-Date:") -> {
                startDate = line.substringAfter(":").trim()
            }
            line.startsWith("Commandline:") || line.startsWith("CommandLine:") -> {
                commandLine = line.substringAfter(":").trim()
            }
        }
    }

    if (startDate == null || commandLine == null) return null

    return HistoryLogRecord(
        timestamp = parseStartDateToTimestamp(startDate),
        commandLine = commandLine,
        startDateRaw = startDate
    )
}

fun readLast50HistoryRecords(path: String): List<HistoryLogRecord> {
    val file = File(path)
    require(file.exists()) { "Fisierul $path nu exista!" }

    val content = file.readText()

    val blocks = content
        .trim()
        .split(Regex("(\\r?\\n){2,}"))
        .filter { it.isNotBlank() }

    return blocks.takeLast(50).mapNotNull { parseHistoryBlock(it) }
}

fun buildHistoryMap(records: List<HistoryLogRecord>): HashMap<Long, HistoryLogRecord> {
    val map = HashMap<Long, HistoryLogRecord>()
    for (record in records) {
        map[record.timestamp] = record
    }
    return map
}

fun <T : Comparable<T>> maxOfTwo(a: T, b: T): T {
    return if (a >= b) a else b
}

fun <K, V> searchAndReplace(
    target: V,
    replacement: V,
    map: HashMap<K, out V>
): Boolean {
    val keysToReplace = map.filterValues { it == target }.keys.toList()
    if (keysToReplace.isEmpty()) return false

    @Suppress("UNCHECKED_CAST")
    val writableMap = map as HashMap<K, V>

    for (key in keysToReplace) {
        writableMap[key] = replacement
    }

    return true
}

fun main() {
    val path = "history.log"   // fișier local în proiect

    val records = readLast50HistoryRecords(path)
    val historyMap = buildHistoryMap(records)

    println("Inregistrari citite:")
    for (record in records) {
        println("timestamp=${record.timestamp}, command=${record.commandLine}")
    }

    if (records.size >= 2) {
        val maxRecord = maxOfTwo(records[0], records[1])
        println("\nCea mai recenta dintre primele doua:")
        println(maxRecord)
    }

    if (records.isNotEmpty()) {
        val target = records[0]
        val replacement = target.copy(commandLine = "COMANDA_INLOCUITA")

        val result = searchAndReplace(target, replacement, historyMap)
        println("\nS-a facut înlocuirea? $result")

        println("\nContinutul map-ului dupa inlocuire:")
        for ((key, value) in historyMap) {
            println("$key -> $value")
        }
    }
}