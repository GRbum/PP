package org.stefan

class JsonParser : Parser {

    override fun parse(text: String): Map<String, Any?> {
        val cleaned = text
            .trim()
            .removePrefix("{")
            .removeSuffix("}")

        val result = mutableMapOf<String, Any?>()

        cleaned.split(",").forEach {
            val pair = it.split(":")
            val key = pair[0].trim().removeSurrounding("\"")
            val value = pair[1].trim().removeSurrounding("\"")
            result[key] = value
        }

        return result
    }
}