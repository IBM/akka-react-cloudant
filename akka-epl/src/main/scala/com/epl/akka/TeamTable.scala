package com.epl.akka

import play.api.libs.json.Json

/**
  * Created by sanjeevghimire on 9/6/17.
  */
case class TeamTable(position: Int,
                     teamName: String,
                     teamShortName: String,
                     played: Int,
                     won: Int,
                     drawn: Int,
                     lost: Int,
                     goalFor: Int,
                     goalAgainst: Int,
                     goalDifference: Int,
                     points: Int
                    ) extends Serializable


object TeamTable {

  implicit val jsonFormat = Json.format[TeamTable]

  def of(teamName:String,item: List[Any]) ={
    TeamTable(
      position = item(0).toString.toInt,
      teamName = item(1).toString,
      teamShortName = item(2).toString,
      played = item(3).toString.toInt,
      won = item(4).toString.toInt,
      drawn = item(5).toString.toInt,
      lost = item(6).toString.toInt,
      goalFor = item(7).toString.toInt,
      goalAgainst = item(8).toString.toInt,
      goalDifference = item(9).toString.toInt,
      points = item(10).toString.toInt
    )
  }


}