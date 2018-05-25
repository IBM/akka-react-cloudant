package com.epl.akka

import akka.actor.{ActorSystem, Props}
import com.epl.akka.WebCrawler.CrawlRequest

object CrawlingApp extends App {
  val system = ActorSystem("Crawler")
  // don't ad-hoc create the Props here, define a props method in the companion object instead,
  // and avoid using the reflection based props factory method
  val webCrawler = system.actorOf(Props[WebCrawler], "WebCrawler")
  // start the crawling
  webCrawler ! CrawlRequest("https://www.premierleague.com/", true)

  // this app does a single "crawl", but it never completes, the application
  // will live forever. That's not what I'd expect from such an app.
  // It should somehow track if it is done or not and then terminate the actor system
  // so that the JVM can shut down.
}
