package com.epl.akka


import java.util.Properties

import akka.actor.{Actor, ActorLogging, Status}
import com.cloudant.client.api.{ClientBuilder, CloudantClient}
import com.epl.akka.DBOperation.{SaveToCloudantDatabase, SaveToCloudantDatabaseUsingRESTAPI}
import play.api.libs.json.{JsValue, Json}

import scala.util.{Failure, Success}


object DBOperation{
  case class SaveToCloudantDatabase(jsValue: JsValue){}

  case class SaveToCloudantDatabaseUsingRESTAPI(jsonString: String){}
}



/**
  * Created by sanjeevghimire on 9/5/17.
  */
class DBOperation(jsonString: String) extends Actor with ActorLogging{

  implicit val ec = context.dispatcher

  val prop = new Properties()

  val dbName = "akka-epl"
  self ! SaveToCloudantDatabaseUsingRESTAPI(jsonString)

  override def receive: Receive = {

    case SaveToCloudantDatabase(jsValue: JsValue) =>
      getCloudantClient().database(dbName, false)
        .save(jsValue)
      println("Saved: "+ jsValue)
      context.stop(self)

    case SaveToCloudantDatabaseUsingRESTAPI(jsonString: String) =>
      loadProperties()
      WebHttpClient.post(prop.getProperty("post_url"),jsonString,prop.getProperty("username"),prop.getProperty("password")) onComplete {
        case Success(body) => println("Successfully Saved:: "+ body)
        case Failure(err) =>
          println(err)
          context.stop(self)
      }
      context.stop(self)


  }

  /**
    * Get cloudant connection using connection parameters from config.properties
    */
  def getCloudantClient(): CloudantClient = {
    loadProperties()

    ClientBuilder.account(prop.getProperty("host"))
      .username(prop.getProperty("username"))
      .password(prop.getProperty("password"))
        .build()
  }


  def loadProperties() = {
    val (host, port, url, username, password, dbname, key, passcode) =
      try {
        prop.load(getClass().getResourceAsStream("/config.properties"))
        (
          prop.getProperty("host"),
          new Integer(prop.getProperty("port")),
          prop.getProperty("url"),
          prop.getProperty("username"),
          prop.getProperty("password"),
          prop.getProperty("dbname"),
          prop.getProperty("key"),
          prop.getProperty("passcode"))
      } catch {
        case e: Exception =>
          e.printStackTrace()
          sys.exit(1)
      }

  }



}
