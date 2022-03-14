package shapes
import akka.actor.Actor

// Actor간 전송할 대부분의 메시지를 정의한 Messages 객체로, Actor들 간의 "신호"처럼 작용함
object Messages{
  // Exit과 Finished에는 따로 상태가 없음. 신호를 보내는 flag 역할
  object Exit
  object Finished

  // 메시지를 수신한 다름, 그 메시지를 송신한 Actor에 문자열 메시지를 응답
  case class Response(message: String)
}

class ShapesDrawingActor extends Actor{
  import Messages._

  // Actor class를 상속받으면 반드시 구현해야하는 추상메서드
  // 부분함수로 패턴매칭을 수행하는 case절로 이루어짐
  // Scala 타입 계층의 최상위 계층인 "Any" 클래스가 인자로 넘어오고, return 값은 Unit
  def receive = {
    case s:Shape =>
      // 메시지가 Shape의 인스턴스라면 아래 수행
      s.draw(str => println(s"Drawing Actor 왈 : $str 그린다~"))  // draw 메서드에 String을 매개변수로 받는 익명함수를 전달
      sender() ! Response(s"Drawing Actor 왈 : $s 그렸다~")  // 송신 Actor에 응답 메시지 보냄
    case Exit =>
      // 모든 작업이 끝남
      println(s"Drawing Actor 왈 : 다 그렸으니까 간다~")
      sender() ! Finished
    case unexpected =>
      // 위 2개 패턴 이외에 패턴이 들어올 경우
      val res =  Response(s"Error : Unknown message : $unexpected")
      println(s"Drawing Actor 왈 : 에러났어~($res)")
      sender() ! res    // !는 비동기 메시지 보내는 메서드
  }
}