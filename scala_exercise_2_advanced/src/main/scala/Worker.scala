import akka.actor.Actor

object Messages{
  object Exit
  object Finished
  case class Response(message: Map[String, Int])
}

class Worker extends Actor{
  import Messages._

  def receive = {
    case strArr:Array[String] =>
      //TODO wordCount
      println("Worker : I'm counting hard...")
      val result = strArr.groupBy(l => l).map(t => (t._1.toLowerCase(), t._2.length))
      sender() ! Response(result)
    case Exit =>
      println("Worker : Word Count Complete")
      sender() ! Finished
    case unexpected =>
      println(unexpected.getClass.toString())
      println("Worker : Error! ("+unexpected+")")
      sender() ! Exit
  }

}
