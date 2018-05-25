package com.epl.akka

import akka.actor.{Actor, ActorLogging, Props}
import com.epl.akka.CloudantReaderWriter.{ExpireCurrentDocument, SaveToCloudantDatabase}
import com.epl.akka.HTMLParser.{CrawlAndGetValidLinks, CrawlAndPrepareJson}
import com.epl.akka.URLValidator.ValidateUrl
import com.epl.akka.WebCrawler._


object WebCrawler {
  case class CrawlRequest(url: String, isRootUrl: Boolean) {}
  // Not used
  case class CrawlResponse(links: Set[String]) {}
  case class Crawl(url: String,isRootUrl: Boolean){}
  case class SaveJsonToCloudant(jsonString: String){}
  case class GetJsonOutput(documentType: DocumentType.Documenttype){}
}


/**
  * Created by sanjeevghimire on 8/30/17.
  */
class WebCrawler extends Actor with ActorLogging{

  // create a props factory for the actor companions  instead of an adhoc Props call here
  val urlValidator = context.actorOf(Props[URLValidator](new URLValidator()))
  val htmlParser = context.actorOf(Props[HTMLParser](new HTMLParser()))
  val cloudantWriter = context.actorOf(Props[CloudantReaderWriter](new CloudantReaderWriter()))

  override def receive: Receive = {
    case CrawlRequest(url, isRootUrl) =>
//      if (isRootUrl) {
//        cloudantWriter ! ExpireCurrentDocument()
//      }
      urlValidator ! ValidateUrl(url,isRootUrl)

    case Crawl(url, isRootUrl) =>
      if(isRootUrl) {
        htmlParser ! CrawlAndGetValidLinks(url)
      }else{
        htmlParser ! CrawlAndPrepareJson(url)
      }

    case SaveJsonToCloudant(jsonString) =>
      cloudantWriter ! SaveToCloudantDatabase(jsonString)


  }
}
