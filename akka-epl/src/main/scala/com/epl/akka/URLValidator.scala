package com.epl.akka

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.epl.akka.URLValidator.ValidateUrl
import com.epl.akka.WebCrawler.Crawl


object URLValidator {
  case class ValidateUrl(url: String,isRootUrl: Boolean) {}

  case class GetCrawledURLs(links: Set[String]) {}
}

/**
  * Created by sanjeevghimire on 9/1/17.
  */

// Making this an actor doesn't really make sense, it introduces an async boundary from the parent
// actor but there is no gain (neither more resilient nor more performant) than just keeping this state
// directly in WebCrawler
class URLValidator extends Actor with ActorLogging{
  var visitedUrl = Set.empty[String]
  var childUrls = Set.empty[ActorRef]

  override def receive: Receive = {
    case ValidateUrl(url,isRootUrl) =>
      if (!visitedUrl(url)) {
        context.parent ! Crawl(url, isRootUrl)
        visitedUrl += url
      }
  }

}
