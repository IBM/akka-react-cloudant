package com.epl.akka

import play.api.libs.json.Json

/**
  * Created by sanjeevghimire on 9/15/17.
  */
case class ResultTable(
                      homeTeam: String,
                      awayTeam: String,
                      homeScore: Int,
                      awayScore: Int,
                      venue:  String,
                      dateTime: Long
                      ) extends Serializable

object ResultTable {

  implicit val jsonFormat = Json.format[ResultTable]

  def of(items: List[Any]): ResultTable ={
    ResultTable(
      homeTeam = items(0).toString,
      awayTeam = items(1).toString,
      homeScore = items(2).toString.toInt,
      awayScore = items(3).toString.toInt,
      venue = items(4).toString,
      dateTime = items(5).toString.toLong
    )
  }
}

case class ResultByDate(
                        date: String,
                        results: List[ResultTable]
                      ) extends Serializable

object ResultByDate {

  implicit val jsonFormat = Json.format[ResultByDate]



}