package com.epl.akka_v1

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, RequestEntity, headers}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString
import com.epl.akka.DocumentType

import scala.concurrent.Future

object HttpClient {

  implicit val system = ActorSystem()
  final val config = system.settings.config
  implicit val ec = system.dispatcher

  private val authorization = headers.Authorization(BasicHttpCredentials(config.getString("cloudant.username"), config.getString("cloudant.password")))


  /**
    * POST call to Cloudant database API
    * @param jsonString
    * @return
    */
  def post(jsonString: String): Future[String] = {
    val responseEntity = for {
      request <- Marshal(jsonString).to[RequestEntity]
      response <- Http().singleRequest(HttpRequest(
        method = HttpMethods.POST,
        uri = config.getString("cloudant.post_url"),
        headers = List(authorization),
        entity = request))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    responseEntity.mapTo[String]

  }


  /**
    * GET call to Cloudant database API
    * @param documentType
    * @return
    */
  def get(documentType: DocumentType.Documenttype): Future[String] = {
    val responseEntity = for {
      response <- Http().singleRequest(HttpRequest(
        method = HttpMethods.GET,
        uri = getUrl(documentType),
        headers = List(authorization)))
      entity <- Unmarshal(response.entity).to[ByteString]
    } yield entity

    responseEntity.mapTo[String]

  }


  /**
    * Get URI based on the type of document requested.
    * @param documentType
    * @return
    */
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
