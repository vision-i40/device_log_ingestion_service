package common.builders

import java.util.UUID.randomUUID

import org.joda.time.DateTime

case class DeviceLogPayloadBuilder(
  pe : String = "a-pe-value",
  uid: String = randomUUID.toString,
  mac: String = "fc:01:7c:c0:42:37",
  tim: DateTime = DateTime.now
) {
  def build: String = {
    s"""{
       |"PE": "$pe",
       |"UID": "$uid",
       |"MAC": "$mac",
       |"TIM": "$tim",
       |"Record": [1, 3, 2, 5, 6]
       |}""".stripMargin
  }
}
