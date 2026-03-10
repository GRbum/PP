package org.stefan

data class RSSFeed(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    val items: MutableList<RSSItem> = mutableListOf()
)