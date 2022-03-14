object Upper {
  def main(args: Array[String]) = {
    // String 배열 선언
    val s = Array("hello", "my", "world!!")

    // 배열의 각 엘리먼트(string)가 익명변수 "_"로 들어오는 map 함수에서 대문자로 만드는 String built-in 메서드 실행 후
    // 문자 사이에 (혹은 양 옆까지) seperator를 추가해서 작렬화 해주는 mkString 함수 적용하여 print 함.
    val output = s.map(_.toUpperCase()).mkString("[ "," "," ]")
    println(output)
  }
}
