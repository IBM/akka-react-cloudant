package com.epl.akka

import play.api.libs.json.Json

/**
  * Created by sanjeevghimire on 9/6/17.
  */
// this looks like it was started on to do the right thing (tm) with actual types
// inside of the system
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

  // This is the domain model of the data in the system -
  // The JSON format would normally belong closer to the places that interfaces with the outer world -
  // one for where you ingest - what data you are served (and which of it you want to keep)
  // one for the database - how the data is stored (could also be augmented with application specific data, for example
  //   for example a timestamp when it was written, who ran it)
  // and one for the HTTP endpoint - how/what data is served to the outside (not necessarily all the data you have)
  // ofc those formats could be the same, but it is not a responsibility of the model itself how it is serialized
  // in different places
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