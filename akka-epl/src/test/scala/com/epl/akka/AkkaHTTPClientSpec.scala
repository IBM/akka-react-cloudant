package com.epl.akka

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.epl.akka.AkkaHTTPClient.GET
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.duration._
import scala.util.{Failure, Success}


/**
  * Created by sanjeevghimire on 9/19/17.
  */
class AkkaHTTPClientSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("AkkaHTTPClientSpec"))

  override def afterAll: Unit = {
    shutdown(system)
  }


//  "A AkkaHttpClient Actor" should "give HTML response when instructed to" in {
//    val testProbe = TestProbe()
//
//    val url = "http://akka.io"
//    val akkaHTTPClientActor = system.actorOf(AkkaHTTPClient.props(),"AkkaHttpClient")
//    akkaHTTPClientActor ! GET(url)
//    //expectMsg(20.seconds, "OK response")
//
//  }

    "A AkkaHttpClient Actor" should "give HTML response when instructed to" in {
      implicit val executionContext = system.dispatcher

      val url = "http://akka.io"

//      WebHttpClient.getUsingAkkaHttp(url) onComplete {
//        case Success(body) =>
//          println(body)
//        case Failure(err) =>
//          println("http request error:" + err.getMessage)
//
//      }
    }



}
