package common.builders

import java.util.UUID.randomUUID
import org.joda.time.{DateTime, LocalDate}

case class WiseJsonLogBuilder(
  PE : String = "a-pe-value",
  UID: String = randomUUID.toString,
  MAC: String = "fc:01:7c:c0:42:37",
  TIM: DateTime = DateTime.now,
  Record: List[Int] = List(1,2,3,4,5,6)
) {
  def build: String = {
    s"""{
       |"PE": "$PE",
       |"UID": "$UID",
       |"MAC": "$MAC",
       |"TIM": "${TIM.toString}",
       |"Record": [${Record.mkString(",")}]
       |}""".stripMargin
  }
}
