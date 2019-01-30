package functional.steps

import com.typesafe.config.ConfigFactory
import common.MongoDBHelper
import common.builders.WiseJsonLogBuilder
import cucumber.api.scala.{EN, ScalaDsl}
import infrastructure.parsing.DeviceLogRecordReadersAndWriters
import io_log_ingestion.DeviceLogRecord
import org.scalatest.Matchers
import org.scalatest.concurrent.Eventually.{eventually, interval, timeout}
import scalaj.http.{Http, HttpResponse}

import scala.concurrent.duration._
import scala.language.postfixOps

class IngestionEventStep extends ScalaDsl with Matchers with EN with DeviceLogRecordReadersAndWriters {
  private val config = ConfigFactory.load()
  private val MANAGER_CONFIG_HOST = "io_log_ingestion_manager.host"
  private val MANAGER_CONFIG_PORT = "io_log_ingestion_manager.port"
  private val managerHost = config.getString(MANAGER_CONFIG_HOST)
  private val managerPort = config.getString(MANAGER_CONFIG_PORT)
  private val MANAGER_URL: String = s"http://$managerHost:$managerPort"
  private val DEVICE_LOG_INGESTION_URL: String = s"http://$managerHost:$managerPort/log"

  private val firstChannelInput = 1
  private val secondChannelInput = 1
  private val records = List(firstChannelInput, secondChannelInput)
  private val wiseJsonLogBuilder = WiseJsonLogBuilder(Record = records)
  private val wiseRawLog: String = wiseJsonLogBuilder.build

  private val unknownRawLog: String = "a-unidentified-log"

  private var storedWiseDeviceLog: Option[DeviceLogRecord] = None
  private var storedUnknownDeviceLog: Option[DeviceLogRecord] = None

  Given("""^the service is up and running$""") { () =>
    MongoDBHelper.reset

    val response: HttpResponse[String] = Http(MANAGER_URL).asString

    response.code shouldEqual 200
  }

  When("""^I receive a request from Wise device$""") { () =>
    val response: HttpResponse[String] = Http(DEVICE_LOG_INGESTION_URL)
                                          .postData(wiseRawLog)
                                          .header("content-type", "application/json").asString

    println(response.body)
  }

  Then("""^I should save Wise Device Log in database$""") { () =>

    eventually(timeout(30 seconds), interval(100 millis)) {
//      storedWiseDeviceLog = Await.result(MongoDBHelper.getByDeviceId(), 5 seconds)

      storedWiseDeviceLog.isDefined shouldEqual true
    }
  }
//
//  Then("""^I should save unknown IO Log in database$""") { () =>
//
//    eventually(timeout(30 seconds), interval(100 millis)) {
//      storedUnknownDeviceLog = Await.result(MongoDBHelper.getByDeviceId(unknownIngestionEvent.deviceId), 5 seconds)
//
//      storedUnknownDeviceLog.isDefined shouldEqual true
//    }
//  }
//
//  And("""^get ingestion event info of Wise device$""") { () =>
//    val ioLog = storedWiseDeviceLog.get
//
//    ioLog.receivedAt shouldEqual wiseIngestionEvent.receivedAt
//    ioLog.deviceId shouldEqual wiseIngestionEvent.deviceId
//    ioLog.rawLog shouldEqual wiseIngestionEvent.rawLog
//    ioLog.traceId shouldEqual wiseIngestionEvent.traceId
//  }
//
//  And("""^get ingestion event info of unknown device$""") { () =>
//    val ioLog = storedUnknownDeviceLog.get
//
//    ioLog.receivedAt shouldEqual unknownIngestionEvent.receivedAt
//    ioLog.deviceId shouldEqual unknownIngestionEvent.deviceId
//    ioLog.rawLog shouldEqual unknownIngestionEvent.rawLog
//    ioLog.traceId shouldEqual unknownIngestionEvent.traceId
//
//  }
//
//  And("""^detect as Wise device$""") { () =>
//    storedWiseDeviceLog.get.detectedDevice shouldEqual DeviceType.WISE
//  }
//
//  And("""^detect as unknown device$""") { () =>
//    storedUnknownDeviceLog.get.detectedDevice shouldEqual DeviceType.UNKNOWN
//  }
//
//  And("""^extract Wise info$""") { () =>
//    val wiseInfo = storedWiseDeviceLog.get.deviceLogInfo
//
//    val expectedChannelInputs = Map(
//      "channel_1" -> firstChannelInput.toString,
//      "channel_2" -> secondChannelInput.toString
//    )
//
//    wiseInfo.isDefined shouldEqual true
//    wiseInfo.get.uid shouldEqual wiseJsonLogBuilder.UID
//    wiseInfo.get.deviceType shouldEqual DeviceType.WISE
//    wiseInfo.get.logDateTime shouldEqual wiseJsonLogBuilder.TIM
//    wiseInfo.get.channelInputs shouldEqual expectedChannelInputs
//  }
//
//  And("""^does not extract info$""") { () =>
//    val deviceInfo = storedUnknownDeviceLog.get.deviceLogInfo
//
//    deviceInfo shouldEqual None
//  }
}
