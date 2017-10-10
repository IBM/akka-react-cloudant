package com.epl.akka

import akka.actor.{ActorSystem, Props}
import com.epl.akka.WebCrawler.CrawlRequest

object CrawlingApp extends App {
  val system = ActorSystem("Crawler")
  val webCrawler = system.actorOf(Props[WebCrawler], "WebCrawler")
  // start the crawling
  webCrawler ! CrawlRequest("https://www.premierleague.com/", true)
}
