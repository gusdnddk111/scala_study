package shapes;

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import com.typesafe.config.ConfigFactory

// 모든 메시지를 실행하기 위한 전역변수로, 액터 프로그래밍에서 사용되는 관용적 표현
private object Start

object ShapesDrawingDriver {
  def main(args: Array[String]) = {
    val system = ActorSystem("DrawingActorSystem", ConfigFactory.load())
    val drawer = system.actorOf(Props(new ShapesDrawingActor), "drawingActor")
    val driver = system.actorOf(Props(new ShapesDrawingDriver(drawer)),"drawingService")
    driver ! Start
  }
}

class ShapesDrawingDriver(drawerActor: ActorRef) extends Actor {
  import Messages._

  override def receive = {
    case Start =>
      drawerActor ! Circle(Point(0.0, 0.0), 1.0)
      drawerActor ! Rectangle(Point(0.0,0.0), 2, 5)
      drawerActor ! 3.141592
      drawerActor ! Triangle(Point(0.0, 0.0), Point(2.0, 0.0), Point(1.0, 2.0))
      drawerActor ! Exit
    case Finished =>
      println(s"DrawingDriver 왈 : 이제 다 끝났다~ 프로그램 끈다!")
      context.system.terminate()
    case response: Response =>
      println("Actor로 부터 응답을 받았습니다. => " + response)
    case unexpected =>
      println(s"DrawingDriver 왈 : 에러났다. => " + unexpected)
  }
}
