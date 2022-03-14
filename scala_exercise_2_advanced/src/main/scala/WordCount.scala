import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.Map
import java.util.concurrent.TimeUnit.NANOSECONDS
import scala.io.Source

private object Start


var allString = Source.fromFile("testText.txt").getLines.mkString

var strArr = allString.split(" ")
var countResult = Map[String, Int]()

var asyncNum = 2
var offset = strArr.length/asyncNum

var startTime = System.nanoTime()

object WordCount {
  def main(args: Array[String]) = {
    // 1. 보통 수행시간
    /*
    countResult.addAll(strArr.groupBy(l => l).map(t => (t._1.toLowerCase(), t._2.length)))
    println(countResult)
    println(countResult.get("scala"))

    var endTime = System.nanoTime()
    println(s"수행시간 : ${NANOSECONDS.toNanos((endTime-startTime))} ns")
    */
    // 수행시간 : 138,213,826 ns


    // 2. 비동기 병렬처리 수행시간

    val system = ActorSystem("WordCountSystem", ConfigFactory.load())
    val hardWorker = system.actorOf(Props(new Worker), "hardWorker")
    val driver = system.actorOf(Props(new WordCount(hardWorker)),"WordCountService")
    driver ! Start

    // 수행시간 : 239,428,111 ns


    // 결론 : 비동기 병렬처리 수행 시간이 더 빠를것으로 예상됐지만, 평균 두배 가량 더 소요됨
    //       처음에는 병렬 처리 후 취합하는 과정에서 너무 많은 시간이 낭비되는 줄 알았으나, 취합 과정을 제외하고도 비슷한 시간 소요됨
    //       지금 생각해 볼 수 있는 원인은 텍스트 예제의 단어가 비슷한 단어로 구성(같은 문단을 수백번 복사함...)되어 있기 때문에
    //       스레드를 나눠 처리하면 캐싱문제나 공간낭비 등과 같은 측면에서 손해가 발생하는 것으로 추측
    //       스칼라와 akka를 이용한 병렬처리는 좀 더 공부하는걸로...
  }
}

class WordCount(worker: ActorRef) extends Actor {
  import Messages._

  def receive = {
    case Start =>
      var i = 0

      for( i <- 0 to asyncNum){
        worker ! strArr.slice(i*offset, (i+1)*offset)
      }
      worker ! Exit
    case Finished =>
      println("Word Count Service : success!")
      //TODO - 취합 결과 print
      println(countResult)
      println(countResult.get("scala"))

      var endTime = System.nanoTime()
      println(s"수행시간 : ${NANOSECONDS.toNanos((endTime-startTime))} ns")
      context.system.terminate()
    case response: Response =>
      //TODO - reponse 취합
      //countResult.addAll(response.message.map{ case (k,v) => k -> (v + countResult.getOrElse(k,0)) })
    case unexpected =>
      println("Word Count Service Error")
  }
}

