package unit.io_log_ingestion.devices.wise

import common.builders.WiseJsonLogBuilder
import io_log_ingestion.devices.wise.ParseWiseLog
import org.joda.time.DateTimeZone
import org.scalatest.{FlatSpec, Matchers}

class ParseWiseLogTest extends FlatSpec with Matchers {
  DateTimeZone.setDefault(DateTimeZone.UTC)

  behavior of "invalid wise being parsed"
  it should "return a failure when the json is not valid" in {
    val wiseInvalidRawLog = "an-invalid-log"

    val actualResult = ParseWiseLog(wiseInvalidRawLog)

    actualResult.isFailure shouldEqual true
  }

  behavior of "valid wise being parsed"
  it should "return a failure when the json is not valid" in {
    val wiseValidRawLogBuilder = WiseJsonLogBuilder()
    val wiseValidRawLog = wiseValidRawLogBuilder.build

    val actualTryResult = ParseWiseLog(wiseValidRawLog)

    actualTryResult.isSuccess shouldEqual true
    val actualResult = actualTryResult.get

    actualResult.PE shouldEqual wiseValidRawLogBuilder.PE
    actualResult.UID shouldEqual wiseValidRawLogBuilder.UID
    actualResult.MAC shouldEqual wiseValidRawLogBuilder.MAC
    actualResult.TIM shouldEqual wiseValidRawLogBuilder.TIM
    actualResult.Record shouldEqual wiseValidRawLogBuilder.Record
  }
}
