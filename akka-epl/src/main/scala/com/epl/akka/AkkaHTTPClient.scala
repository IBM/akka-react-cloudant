package com.epl.akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import com.epl.akka.AkkaHTTPClient.GET

/**
  * Created by sanjeevghimire on 9/19/17.
  */
class AkkaHTTPClient() extends Actor
  with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  //var originalSender: Option[ActorRef] = None // <-- added

  override def receive: Receive = {
    case GET(uri: String) =>
      //val s = sender             // <-- added
      //originalSender = Option(s) // <-- added
      log.info("getting the url")
      http
        .singleRequest(HttpRequest(HttpMethods.GET,uri = uri))
        .pipeTo(self)

    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        println("Got response, body: " + body.utf8String)
      }
      //originalSender.foreach(_ ! "OK response") // <-- added

    case resp @ HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()


  }

}


object AkkaHTTPClient {

  def props() =
    Props[AkkaHTTPClient]

  final case class GET(uri: String){}

}
