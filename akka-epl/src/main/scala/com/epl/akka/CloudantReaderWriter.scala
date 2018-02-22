package com.epl.akka

import akka.actor.{Actor, ActorLogging}
import com.epl.akka.CloudantReaderWriter.{ExpireCurrentDocument, GetDocument, SaveToCloudantDatabase}
import com.epl.akka.SoccerMainController.Document
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import akka.pattern.pipe

import scala.util.{Failure, Success}


object CloudantReaderWriter {
  final case class SaveToCloudantDatabase(jsonString: String)
  final case class GetDocument(documentType: DocumentType.Documenttype)
  final case class ExpireCurrentDocument()
}

/**
  * Created by sanjeevghimire on 9/19/17.
  */
class CloudantReaderWriter extends Actor with ActorLogging{

  implicit val ec = context.dispatcher

  private val config = context.system.settings.config

  // note that as each of these methods just trigger async work somewhere else, and there is
  // no state this actor is superflous and could easily be done with regular methods returning Futures
  override def receive: Receive = {
    case SaveToCloudantDatabase(jsonString: String) =>
      // in general side effecting from a Future callback like this is bad practice
      // since it will execute on a thread that is not the actor, using `log` should be thread safe
      // but it is not good to show in example code as it somewhat encourages doing the wrong thing(tm)
      // if you want to react on the future completion in the actor you should use `pipeTo`
      // see docs here: https://doc.akka.io/docs/akka/current/actors.html#ask-send-and-receive-future
      WebHttpClient.post(config.getString("cloudant.post_url"),jsonString,config.getString("cloudant.username"),config.getString("cloudant.password")) onComplete {
        case Success(body) =>
          log.info("Successfully Saved:: "+ body)
        case Failure(err) =>
          log.error(err,"Error while saving to Cloudant")
      }

    case GetDocument(documentType) =>
      val url: String = getUrl(documentType)
      val res = WebHttpClient.getWithHeader(url,config.getString("cloudant.username"),config.getString("cloudant.password"))
      res pipeTo sender()


    case ExpireCurrentDocument() =>
      val url: String = config.getString("cloudant.get_unexpireddoc_url")
      WebHttpClient.getWithHeader(url,config.getString("cloudant.username"),config.getString("cloudant.password")) onComplete {
        case Success(unexpiredDocumentJson) =>
          val jsValue: JsValue = Json.parse(unexpiredDocumentJson)
          (jsValue \ "docs").as[Seq[JsObject]].foreach( doc => {
            val updateJsonObject = doc + ("expired" -> Json.toJson("YES"))
            val updateUrl = config.getString("cloudant.post_url") + "/" + (updateJsonObject \ "_id")
            WebHttpClient.put(updateUrl,Json.stringify(updateJsonObject),config.getString("cloudant.username"),config.getString("cloudant.password")) onComplete {
              case Success(body) =>
                log.info("Successfully deleted:: "+ body)
              case Failure(err) =>
                log.error(err,"Error while saving to Cloudant")
            }
          })
        case Failure(err) =>
          log.error(err,"Error while retrieving from Cloudant DB")
      }

  }

  def getUrl(documentType: DocumentType.Documenttype): String ={
    var url: String = null
    if(documentType.equals(DocumentType.TeamTable)){
      url = config.getString("cloudant.get_tables_url")
    }else if(documentType.equals(DocumentType.Results)){
      url = config.getString("cloudant.get_results_url")
    }else{
      url = config.getString("cloudant.get_fixtures_url")
    }
    url
  }
}
