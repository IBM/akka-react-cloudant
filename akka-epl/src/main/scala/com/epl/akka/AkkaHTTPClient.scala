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
// this actor seems somewhat redundant in general as all it does is passing requests to singleRequest
// and then folding the response into memory (none of the actual work happens in the thread of the actor)
// it is also not used anywhere, from what I can see, so maybe just completely remove it
class AkkaHTTPClient() extends Actor
  with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  // this materializer is bound to the lifecycle of the actor system, that means it will leak if
  // this actor is stopped. Better either provide a single materializer to use across the app
  // and take as a constructor parameter here, or to create the materializer with the actor context
  // as actor-ref factory (then it will be bound to the actor)
  // See docs here: https://doc.akka.io/docs/akka/current/stream/stream-flows-and-basics.html#actor-materializer-lifecycle
  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val http = Http(context.system)

  // the commented out logic around this is incorrect as a GET message immediately triggers work
  // done in a future and the actor then continues processing the next message, meaning that when the
  // response comes back the originalSender field would contain the latest sender, not the original sender
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

  // use the lambda based props factory Props(new MyActor) instead of the reflection based one
  def props() =
    Props[AkkaHTTPClient]

  final case class GET(uri: String){}

}
