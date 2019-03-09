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

/*

{
  "PE": "a-pe-value",
  "UID": "09ausd0u1-as908djh01-0as9dj01-123",
  "MAC": "12:13:14:e4::i7:90",
  "TIM": "2019-01-01T00:00:00",
  "Record": ["1", "2", "3"]
}

 */