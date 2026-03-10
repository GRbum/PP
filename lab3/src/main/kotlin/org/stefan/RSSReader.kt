package org.stefan

import org.jsoup.Jsoup
import org.jsoup.parser.Parser

fun main() {
    //Link-ul RSS-ului
    val url = "https://wordpress.com/blog/feed/"

    val doc = Jsoup.connect(url)
        .parser(Parser.xmlParser())
        .get()

    val feed = RSSFeed()

    feed.title = doc.selectFirst("channel > title")?.text() ?: ""
    feed.link = doc.selectFirst("channel > link")?.text() ?: ""
    feed.description = doc.selectFirst("channel > description")?.text() ?: ""

    val items = doc.select("item")

    for (item in items) {

        val title = item.selectFirst("title")?.text() ?: ""
        val link = item.selectFirst("link")?.text() ?: ""
        val description = item.selectFirst("description")?.text() ?: ""
        val pubDate = item.selectFirst("pubDate")?.text() ?: ""

        val rssItem = RSSItem(title, link, description, pubDate)

        feed.items.add(rssItem)
    }

    println("Feed: ${feed.title}\n")

    for (item in feed.items) {
        println("Title: ${item.title}")
        println("Link: ${item.link}")
        println("Date: ${item.pubDate}")
        println("Description: ${item.description}")
        println("-------------")
    }
}