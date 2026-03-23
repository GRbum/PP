package org.stefan

class YamlParser : Parser {

    override fun parse(text: String): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()

        text.lines().forEach {
            if (it.contains(":")) {
                val pair = it.split(":")
                val key = pair[0].trim()
                val value = pair[1].trim()
                result[key] = value
            }
        }

        return result
    }
}