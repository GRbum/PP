package org.stefan

import java.io.File

fun main() {

    // JSON test
    val jsonCrawler = Crawler("https://jsonplaceholder.typicode.com/todos/1")
    val jsonResult = jsonCrawler.processContent("json")

    println("JSON RESULT:")
    println(jsonResult)
    println()

    // XML test
    val XmlContent = File("pom.xml").readText();

    val xmlParser = XmlParser()
    val xmlResult = xmlParser.parse(XmlContent)

    println("XML RESULT:")
    println(xmlResult)
    println()

    // YAML test
    val yamlText = """
        name: Ana
        age: 22
        city: Iasi
    """.trimIndent()

    val yamlParser = YamlParser()
    val yamlResult = yamlParser.parse(yamlText)

    println("YAML RESULT:")
    println(yamlResult)
}