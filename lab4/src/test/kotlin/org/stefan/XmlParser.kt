package org.stefan

import org.jsoup.Jsoup
import org.jsoup.parser.Parser

class XmlParser : org.stefan.Parser
{
    override fun parse(text: String): Map<String, Any?> {
        val doc = Jsoup.parse(text, "", Parser.xmlParser())

        val result = mutableMapOf<String, Any?>()

        doc.children().first()?.children()?.forEach {
            result[it.tagName()] = it.text()
        }

        return result
    }
}