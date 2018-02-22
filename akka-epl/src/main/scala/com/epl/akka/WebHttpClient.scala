package com.epl.akka


import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.util.{Failure, Success}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import com.ning.http.client._
import org.apache.commons.codec.binary.Base64
import org.openqa.selenium.WebDriver
import org.openqa.selenium.phantomjs.{PhantomJSDriver, PhantomJSDriverService}
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.ui.WebDriverWait

import scala.concurrent.{ExecutionContext, Future, Promise}



/**
  * HTTPClient to get the response from the URL.
  * In this case this returns the html output of the url
  * being crawled
  */
object WebHttpClient {

  // in general not a good idea to put state like this in a singleton, could be ok here, but what happens
  // if the async http client crashes - no way to restart it as it is effectively a constant per JVM
  // (that would be a great use case for using an actor)
  val PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX: String = "phantomjs.page.customHeaders."
  val PHANTOMJS_CLI_ARGS: String = "phantomjs.cli.args"

  val config = new AsyncHttpClientConfig.Builder()
  val client = new AsyncHttpClient(config
    .setFollowRedirect(true)
    .setExecutorService(Executors.newWorkStealingPool(64))
    .build())

  val USER_AGENT: String= "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36"
  System.setProperty("phantomjs.page.settings.userAgent", USER_AGENT)

  // if this was instead
  var driver: WebDriver = null


  import scala.concurrent.duration._

  // here you create a separate actor system and materializer that starts thread pools/uses resources but
  // is not really used
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = ActorMaterializer()



  def get(url: String): Future[String] = {
    val promise = Promise[String]()
    val request = client.prepareGet(url).build()
    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        response
      }

      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }
    })
    promise.future
  }


  def getWithHeader(url: String,username: String, password: String): Future[String] = {
    val promise = Promise[String]()
    val encodedUserPass: String = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
    val request = new RequestBuilder("GET")
      .setUrl(url)
      .addHeader("Content-Type", "application/json")
      .addHeader("Accept", "application/json")
      .addHeader("Authorization", "Basic " + encodedUserPass)
      .build()
    client.prepareRequest(request)
    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        response
      }

      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }
    })
    promise.future
  }

// remove stuff rather than keep them commented out in a published tutorial sample
//  def getUsingAkkaHttp(url: String)(implicit system: ActorSystem, mat: Materializer): Future[String] = {
//    implicit val executionContext = system.dispatcher
//
//    val req = HttpRequest(uri = url)
//    Http().singleRequest(req) flatMap { resp =>
//      resp.entity.toStrict(5.seconds).map(_.data.decodeString("UTF-8"))
//    }
//  }

  def post(url: String, jsonString: String, username: String, password: String): Future[String] = {
    val promise = Promise[String]()
    val encodedUserPass: String = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
    val request = new RequestBuilder("POST")
      .setUrl(url)
      .addHeader("Content-Type", "application/json")
      .addHeader("Accept", "application/json")
      .addHeader("Authorization", "Basic " + encodedUserPass)
      .setBody(jsonString)
      .build()
    client.prepareRequest(request)

    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        response
      }

      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }
    })
    promise.future
  }

  def put(url: String, jsonString: String, username: String, password: String): Future[String] = {
    val promise = Promise[String]()
    val encodedUserPass: String = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
    val request = new RequestBuilder("PUT")
      .setUrl(url)
      .addHeader("Content-Type", "application/json")
      .addHeader("Accept", "application/json")
      .addHeader("Authorization", "Basic " + encodedUserPass)
      .setBody(jsonString)
      .build()
    client.prepareRequest(request)

    client.executeRequest(request, new AsyncCompletionHandler[Response]() {
      override def onCompleted(response: Response): Response = {
        promise.success(response.getResponseBody)
        response
      }

      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }
    })
    promise.future
  }



  // another great reason to put this inside an actor, actors have explicit lifecycle,
  // and concurrency is handled for you. What happens here if multiple threads calls this at the same time?
  def initPhantomJS(){
    val desiredCaps: DesiredCapabilities = DesiredCapabilities.phantomjs()
    desiredCaps.setJavascriptEnabled(true)
    desiredCaps.setCapability("takeScreenshot",false: Any)
    desiredCaps.setCapability( PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY , "/usr/local/Cellar/phantomjs/2.1.1/bin/phantomjs" : Any)
    desiredCaps.setCapability(PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent" , USER_AGENT: Any)

    val cliArgsCap: List[String] =  List("--takeScreenshot=true","--web-security=false","--ssl-protocol=any","--ignore-ssl-errors=true","--webdriver-loglevel=ERROR")
    desiredCaps.setCapability(PHANTOMJS_CLI_ARGS, cliArgsCap);

    driver = new PhantomJSDriver(desiredCaps);
  }

  // and here as well, this is not thread safe, this will not work given that you call it from
  // future callbacks in the rest of hte code
  def getUsingPhantom(url: String): String = {
    initPhantomJS()
    driver.get(url)
    val isTables = url.endsWith("tables")
    if (!isTables) {
      val wait: WebDriverWait = new WebDriverWait(driver,10)
      Thread.sleep(10000)
    }
    val pageSource:String = driver.getPageSource
    driver.quit()
    pageSource
  }



}
