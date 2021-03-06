package com.metinosman

import com.mongodb.client.MongoCollection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import java.lang.Thread.sleep

val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.1.2 Safari/603.3.8"

val client = KMongo.createClient() //get com.mongodb.MongoClient new instance
val database = client.getDatabase("egdb")!! //normal java driver usage

val roomExclusions = listOf("Adresse :", "Contact", "Nouveaux escape games", "Caractéristiques", "Villes", "Nos sélections", "Plus d’infos")

fun main(args: Array<String>) {
    val site = "https://www.wescape.fr/escape-game/?_sft_category=escape-game-france"
    val collection = database.getCollection<Game>() //KMongo extension method

    val doc : Document = Jsoup.connect(site).userAgent(userAgent).get()

    val urls: List<String> = doc.select("h3 > a").map { it.attr("href") }

    for ((i, url) in urls.withIndex()) {
        sleep((Math.random() * 5000).toLong())
        try {
            gameDetail(url, collection)
        } catch (_ : Exception) {
            println("Error on :" + url)
        }
/*
        if (i > 1) {
            break
        }
*/
    }

}

fun gameDetail(url: String, collection: MongoCollection<Game>) {
    val doc = Jsoup.connect(url).userAgent(userAgent).get()
    val (title, city) = doc.select("h1 > a").first().ownText().split(" | ").slice(0..1)

    val rooms = doc.select("h3.widget-title").map { it.ownText() }.filter { ! roomExclusions.contains(it) }

    val g = Game(title, city, rooms)

    collection.insertOne(g)
    println("Added " + title)

}