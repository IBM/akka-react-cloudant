package com.epl.akka

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.ActorMaterializer
import com.epl.akka.CloudantReaderWriter.GetDocument
import akka.util.Timeout
import scala.concurrent.Future
import scala.io.StdIn
import scala.concurrent.duration._
import akka.http.scaladsl.server.Directives._


object SoccerMainController extends App with CorsSupport{

  case class Document(json: String)


    implicit val system = ActorSystem("SoccerSystem")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val config = system.settings.config

    val cloudantReader =
      system.actorOf(Props[CloudantReaderWriter], "cloudantWriter")

    implicit val timeout: Timeout = 10.seconds

    val route: Route =
      path("fixtures") {
        get {
          val fixtureJson:Future[String] = (cloudantReader ? GetDocument(DocumentType.Fixtures)).mapTo[String]
          complete(
            fixtureJson
          )
        }
      } ~
        path("teamtable") {
          get {
            val teamTableJson:Future[String] = (cloudantReader ? GetDocument(DocumentType.TeamTable)).mapTo[String]
            complete(
              teamTableJson
            )
          }
        } ~
        path("results") {
          get {
            val resultsJson:Future[String] = (cloudantReader ? GetDocument(DocumentType.Results)).mapTo[String]
            complete(
              resultsJson
            )
          }
        }

    val bindingFuture = Http().bindAndHandle(corsHandler(route),
                                             config.getString("http.host"),
                                             config.getInt("http.port"))

    println(
      s"Server online at ${config.getString("http.host")}: ${config.getString("http.port")}\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

}
