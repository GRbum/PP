package org.stefan

import org.jsoup.Jsoup

class Crawler(private val url: String) {

    fun getResource(): String {
        return Jsoup.connect(url)
            .ignoreContentType(true)
            .execute()
            .body()
    }

    fun processContent(contentType: String): Map<String, Any?> {
        val text = getResource()

        val parser: Parser = when (contentType.lowercase()) {
            "json" -> JsonParser()
            "xml" -> XmlParser()
            "yaml" -> YamlParser()
            else -> throw IllegalArgumentException("Unsupported type")
        }

        return parser.parse(text)
    }
}