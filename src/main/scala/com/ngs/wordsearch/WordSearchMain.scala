

package com.ngs.wordsearch

import com.ngs.wordsearch.actors._
import com.ngs.wordsearch.utils._
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import akka.actor.ActorSystem

object WordSearchApp extends App {

  val parms = new HashMap[String, String]
  val config = ConfigFactory.load()
  parms("maxActors") = config.getString("app.akka.maxActors")
  parms("rows") = config.getString("app.commands.rows")
  parms("cols") = config.getString("app.commands.cols")
  parms("wordLength") = config.getString("app.commands.minwordlength")
  parms("wordStorFilePath") = config.getString("app.files.wordStorFilePath")

  val wordstor = WordStorUtil.loadWordStor(parms("wordStorFilePath"))

  if (wordstor != EmptyCharTrie) {

    val board = WordSearchBoard.initWordBoard(
      parms.getOrElse("rows", WordStorUtil.maxWordLength.toString).toInt,
      parms.getOrElse("cols", WordStorUtil.maxWordLength.toString).toInt)

    if (!board.isEmpty) {
      val sys = ActorSystem("WordSearchSystem")
      val supervisor = sys.actorOf(WordSearchSupervisor.props, name = "WordSearchSupervisor")

      supervisor ! StartSystemMsg(parms)
    }
  }//-----------------------------------------

}