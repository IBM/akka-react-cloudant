package com.epl.akka

/**
  * Created by sanjeevghimire on 9/15/17.
  */
// Enumeration is not often used in Scala code (because it is a pretty clumsy tool)
// a much more common/useful way to do enumerations is to use ADTs, like so:
// sealed trait DocumentType
// case object TeamTableDocument extends DocumentType
// case object FixturesDocument extends DocumentType
object DocumentType extends Enumeration{
  type Documenttype  = Value
  val TeamTable,Fixtures,Results = Value
}
