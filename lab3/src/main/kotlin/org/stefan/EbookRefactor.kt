package org.stefan

import java.io.File

class EbookProcessor(private var text: String) {

    fun process(): String {

        // Detectarea autorului prin repetiție
        text = removeRepeatedAuthor(text)

        // Detectarea / eliminarea numarului de pagina
        text = text.replace(Regex("\\s{2,}\\d+(\\s{2,}|$)"), " ")

        // Eliminarea spațiilor multiple din text (rămâne numai unul)
        text = text.replace(Regex("[ ]{2,}"), " ")

        // Eliminarea salturilor la linie nouă multiple din text
        text = text.replace(Regex("(\\r?\\n){2,}"), "\n")

        // Identificarea si corectarea diacriticelor
        text = fixRomanianDiacritics(text)

        return text.trim()
    }

    private fun fixRomanianDiacritics(input: String): String {
        return input.replace('ş', 'ș').replace('Ş', 'Ș')
            .replace('ţ', 'ț').replace('Ţ', 'Ț')
            .replace('ã', 'ă').replace('Ã', 'Ă')
    }

    private fun removeRepeatedAuthor(input: String): String {
        val lines = input.lines()
        val counts = lines.groupingBy { it.trim() }.eachCount()

        val possibleMatch = counts.filter { it.value > 1 && it.key.split(" ").size >= 2 }.keys

        return lines.filterNot { it.trim() in possibleMatch }.joinToString("\n")
    }
}

fun main() {
    // Exemplu de utilizare
    val filePath = "ebook_example.txt"
    val file = File(filePath)

    if (file.exists()) {
        val content = file.readText()
        val processor = EbookProcessor(content)
        val cleanedText = processor.process()

        File("ebook_cleaned.txt").writeText(cleanedText)
        println("Procesare finalizata cu succes!")
    } else {
        println("Fisierul $filePath nu a fost gasit.")
    }
}