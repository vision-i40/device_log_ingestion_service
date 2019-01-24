package common.builders

import java.util.UUID.randomUUID

import org.joda.time.LocalDateTime

case class IOLogPayloadBuilder(
  pe : String = "a-pe-value",
  uuid: String = randomUUID.toString,
  mac: String = "fc:01:7c:c0:42:37",
  receivedAt: String = LocalDateTime.now.toString
) {
  def build: String = {
    s"""{
       |"PE": "$pe",
       |"UID": "$uuid",
       |"MAC": "$mac",
       |"TIM": "$receivedAt",
       |"Record": [1, 3, 2, 5, 6]
       |}""".stripMargin
  }
}
