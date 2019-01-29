package unit.io_log_ingestion.devices.wise

import common.builders.WiseJsonLogBuilder
import io_log_ingestion.devices.wise.{IsWiseLog, WiseLog}
import org.scalatest.{FlatSpec, Matchers}

class IsWiseLogTest extends FlatSpec with Matchers {
  behavior of "parsing a log that is not from Wise device"
  it should "return none" in {
    val notAWiseRarLog = "log-from-other:device"

    IsWiseLog.unapply(notAWiseRarLog) shouldEqual None
  }

  behavior of "parsing a log that is from Wise device"
  it should "return some device info" in {
    val firstRecord = 0
    val secondRecord = 1
    val thirdRecord = 156
    val wiseLogBuilder = WiseJsonLogBuilder(Record = List(firstRecord, secondRecord, thirdRecord))
    val wiseRawJsonLog = wiseLogBuilder.build

    IsWiseLog.unapply(wiseRawJsonLog) shouldEqual Some(WiseLog(
      PE = wiseLogBuilder.PE,
      UID = wiseLogBuilder.UID,
      MAC = wiseLogBuilder.MAC,
      TIM = wiseLogBuilder.TIM,
      Record = wiseLogBuilder.Record
    ))
  }
}
