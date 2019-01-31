package functional.steps

import com.typesafe.config.ConfigFactory
import common.MongoDBHelper
import common.builders.WiseJsonLogBuilder
import cucumber.api.scala.{EN, ScalaDsl}
import infrastructure.parsing.DeviceLogRecordReadersAndWriters
import io_log_ingestion.DeviceLogRecord
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.scalatest.Matchers
import org.scalatest.concurrent.Eventually.{eventually, interval, timeout}
import play.api.http.HttpVerbs
import play.api.libs.json.Json
import scalaj.http.{Http, HttpResponse}
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.Try

class IngestionEventStep extends ScalaDsl with Matchers with HttpVerbs with EN with DeviceLogRecordReadersAndWriters {
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

  private var deviceLogRecordResponse: Option[DeviceLogRecord] = None
  private var storedWiseDeviceLog: Option[DeviceLogRecord] = None
  private var unknownDeviceRequestResponse: Option[HttpResponse[String]] = None

  Given("""^the service is up and running$""") { () =>
    val response: HttpResponse[String] = Http(MANAGER_URL).asString

    response.code shouldEqual 200
  }

  And("""^database is clear$""") { () =>
    MongoDBHelper.reset
  }

  When("""^A request is received from Wise device$""") { () =>
    val response: HttpResponse[String] = Http(DEVICE_LOG_INGESTION_URL)
                                          .postData(wiseRawLog)
                                          .header("content-type", "application/json")
                                          .asString
    response.code shouldEqual CREATED
    deviceLogRecordResponse = Try(Json.parse(response.body).as[DeviceLogRecord]).toOption
  }

  Then("""^Wise Device Log should be saved in database$""") { () =>
    eventually(timeout(10 seconds), interval(100 millis)) {
      deviceLogRecordResponse.isDefined shouldEqual true

      storedWiseDeviceLog = Await.result(MongoDBHelper.getByDeviceId(deviceLogRecordResponse.get.deviceId), 5 seconds)
    }
  }

  And("""^detect as Wise device$""") { () =>
    storedWiseDeviceLog.isDefined shouldEqual true
    val actualwiseLogRecord = storedWiseDeviceLog.get

    actualwiseLogRecord.detectedDevice shouldEqual DeviceType.WISE
  }

  And("""^extract Wise information$""") { () =>
    storedWiseDeviceLog.isDefined shouldEqual true
    val actualWiseLogRecord = storedWiseDeviceLog.get

    actualWiseLogRecord.deviceLog shouldEqual Some(DeviceLog(
      deviceType = DeviceType.WISE,
      uid = wiseJsonLogBuilder.UID,
      logDateTime = wiseJsonLogBuilder.TIM,
      channelInputs = Map("channel_1" -> firstChannelInput.toString, "channel_2" -> secondChannelInput.toString)
    ))
  }

  When("""^a request with unrecognized payload$""") { () =>
    unknownDeviceRequestResponse = Some(Http(DEVICE_LOG_INGESTION_URL)
                                        .postData(unknownRawLog)
                                        .header("content-type", "application/json")
                                        .asString)
  }

  And("""^the log should not be saved in the database$""") { () =>
    MongoDBHelper.countDeviceLogs shouldEqual 0
  }

  And("""^should answer with a bad request response$"""){ () =>
    unknownDeviceRequestResponse.isDefined shouldEqual true
    unknownDeviceRequestResponse.get.code shouldEqual BAD_REQUEST
  }
}
