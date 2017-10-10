package com.epl.akka

import java.net.URL

import akka.actor.{Actor, ActorLogging, Status}
import com.epl.akka.HTMLParser._
import com.epl.akka.WebCrawler.{CrawlRequest, SaveJsonToCloudant}
import org.jsoup.Jsoup
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.util.{Failure, Success}


object HTMLParser {

  case class CrawlAndGetValidLinks(url: String){}

  case class CrawlAndPrepareJson(url: String){}

}


/**
  * Created by sanjeevghimire on 9/1/17.
  */
class HTMLParser() extends Actor with ActorLogging{

  val EXPIRED_NO = "NO"
  val EXPIRED_YES = "YES"

  implicit val ec = context.dispatcher
  import scala.collection.JavaConverters._


  def getValidLinks(content: String, url:String): Iterator[String] = {
    Jsoup.parse(content, url).select("a[href]").iterator().asScala.map(_.absUrl("href"))
  }

  def stop(): Unit = {
    context.stop(self)
  }

  def getFixturesAndConvertToJson(fixturesBody: String,url:String): String ={
    val resultsDetail = Jsoup.parse(fixturesBody, url)
      .select("section.fixtures")
      .select("div[data-competition-matches-list]")
      .iterator().asScala
      .map(divElement => {
        val dateTimeStr = divElement.attr("data-competition-matches-list")
        val resultsByDateList = divElement.select("ul.matchList")
          .select("li")
          .iterator().asScala
          .map(liElement => {
            val jsonString = liElement.select("span").attr("data-ui-args")
            Json.parse(jsonString)
          }).toList
        dateTimeStr -> resultsByDateList
      }).toMap

    val resultsMap = Map("games" -> resultsDetail)

    val jsValue: JsValue = Json.toJson(resultsMap)
    val returnJson: JsObject = jsValue.as[JsObject] + ("expired" -> Json.toJson(EXPIRED_NO)) + ("documentType" -> Json.toJson(DocumentType.Fixtures.toString))
    Json.stringify(returnJson)
  }

  def getTablesAndConvertToJson(tablesBody: String,url:String): String ={
    val tableDetail = Jsoup.parse(tablesBody, url)
      .select("tbody.tableBodyContainer").first()
      .select("tr")
      .iterator().asScala
      .filterNot(element => element.hasClass("expandable"))
      .map(trElement => {
        val teamName = trElement.attr("data-filtered-table-row-name")
        val teamDetails = trElement.select("td").iterator().asScala
          .filterNot(tdElement => tdElement.hasClass("nextMatchCol hideMed"))
          .filterNot(tdElement => tdElement.hasClass("revealMore"))
          .filterNot(tdElement => tdElement.hasClass("form hideMed"))
          .flatMap({
            tdElement =>
                  if (tdElement.hasClass("team")) {
                    tdElement.select("a").select("span").iterator().asScala
                      .filterNot(_.classNames().contains("badge-25"))
                      .map(_.text()).toSeq
                  } else if(tdElement.hasClass("pos button-tooltip")){
                    Seq(tdElement.select("span.value").first().text())
                  }else{
                    Seq(tdElement.text())
                  }

          }).toList
        TeamTable.of(teamName,teamDetails)
      }).toList

    val teamTableMap = Map("teams" -> tableDetail.sortBy(teamTable => teamTable.position))
    val jsValue: JsValue = Json.toJson(teamTableMap)
    val returnJson: JsObject = jsValue.as[JsObject] + ("expired" -> Json.toJson(EXPIRED_NO)) + ("documentType" -> Json.toJson(DocumentType.Results.toString))
    val string = Json.stringify(returnJson)
    string

  }

  def getResultsAndConvertToJson(resultsBody: String, url:String): String ={
    val resultsDetail = Jsoup.parse(resultsBody, url)
      .select("section.fixtures")
      .select("div[data-competition-matches-list]")
      .iterator().asScala
      .map(divElement => {
        val dateTimeStr = divElement.attr("data-competition-matches-list")
        val resultsByDateList = divElement.select("ul.matchList")
          .select("li")
          .iterator().asScala
          .map(liElement => {
            val homeTeam = liElement.attr("data-home")
            val awayTeam = liElement.attr("data-away")
            val venue = liElement.attr("data-venue")
            val scoreStr = liElement.select("span.score").text().split("-").map(_.toInt)
            val dateTimeInMillis = liElement.attr("data-comp-match-item-ko")
            ResultTable.of(List(homeTeam,awayTeam,scoreStr(0),scoreStr(1),venue,dateTimeInMillis))
          }).toList
        ResultByDate(dateTimeStr,resultsByDateList)
      }).toList

    val resultsMap = Map("results" -> resultsDetail)

    val jsValue: JsValue = Json.toJson(resultsMap)
    val returnJson: JsObject = jsValue.as[JsObject] + ("expired" -> Json.toJson(EXPIRED_NO)) + ("documentType" -> Json.toJson(DocumentType.Results.toString))
    Json.stringify(returnJson)
  }

  override def receive = {
    case CrawlAndGetValidLinks(url: String) =>
      WebHttpClient.get(url) onComplete {
        case Success(body) =>
          getValidLinks(body, url)
            .filter(link => link != null && link.length > 0)
            .filter(link => !link.contains("mailto"))
            .filter(link => !link.equals(url))
            .filter(link =>  new URL(url).getHost  == new URL(link).getHost)
            //.filter(link => link.endsWith("fixtures") || link.endsWith("tables") || link.endsWith("results"))
            .filter(link => link.endsWith("fixtures"))
            .foreach(context.parent ! CrawlRequest(_,false))
        case Failure(err) =>
          log.error(err, "Error while crawling url: "+ url)
          stop()
      }


    case CrawlAndPrepareJson(url: String) =>
      val isFixtures = url.endsWith("fixtures")
      val isTables = url.endsWith("tables")
      val isResults = url.endsWith("results")
      val body: String = WebHttpClient.getUsingPhantom(url)
      var jsonString: String = null
      if(isFixtures) {
        jsonString = getFixturesAndConvertToJson(body,url)
      }else if(isTables){
        jsonString = getTablesAndConvertToJson(body,url)
      }else if(isResults){
        jsonString = getResultsAndConvertToJson(body,url)
      }
      log.info("HTML to JSON: "+jsonString)
      context.parent ! SaveJsonToCloudant(jsonString)

    case _: Status.Failure => stop()

  }
}
