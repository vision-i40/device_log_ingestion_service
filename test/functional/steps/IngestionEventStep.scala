package functional.steps

import com.typesafe.config.ConfigFactory
import common.{MongoDBHelper, QueueHelper}
import common.builders.{IngestionEventBuilder, WiseJsonLogBuilder}
import cucumber.api.scala.{EN, ScalaDsl}
import infrastructure.IngestionEvent
import infrastructure.parsing.ParseIngestionEvent
import io_log_ingestion.IOLog
import io_log_ingestion.devices.DeviceType
import org.scalatest.{BeforeAndAfterAll, Matchers}
import play.api.libs.json.Json
import scalaj.http.{Http, HttpResponse}
import org.scalatest.concurrent.Eventually.{eventually, interval, timeout}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class IngestionEventStep extends ScalaDsl with Matchers with EN with ParseIngestionEvent {
  private val config = ConfigFactory.load()
  private val MANAGER_CONFIG_HOST = "io_log_ingestion_manager.host"
  private val MANAGER_CONFIG_PORT = "io_log_ingestion_manager.port"
  private val managerHost = config.getString(MANAGER_CONFIG_HOST)
  private val managerPort = config.getString(MANAGER_CONFIG_PORT)
  private val MANAGER_URL: String = s"http://$managerHost:$managerPort"

  private val firstChannelInput = 1
  private val secondChannelInput = 1
  private val records = List(firstChannelInput, secondChannelInput)
  private val wiseJsonLogBuilder = WiseJsonLogBuilder(Record = records)
  private val wiseRawLog: String = wiseJsonLogBuilder.build
  private val wiseIngestionEvent: IngestionEvent = IngestionEventBuilder(rawLog = wiseRawLog).build

  private val unknownRawLog: String = "a-unidentified-log"
  private val unknownIngestionEvent: IngestionEvent = IngestionEventBuilder(rawLog = unknownRawLog).build

  private var storedWiseIOLog: Option[IOLog] = None
  private var storedUnknownIOLog: Option[IOLog] = None

  Given("""^the service is up and running$""") { () =>
    QueueHelper.setupExchange()
    QueueHelper.reset
    MongoDBHelper.reset

    val response: HttpResponse[String] = Http(MANAGER_URL).asString

    response.code shouldEqual 200
  }

  When("""^I receive an event from Wise device$""") { () =>
    QueueHelper.publish(Json.toJson(wiseIngestionEvent).toString)
  }

  When("""^I receive an event from unknown device type$""") { () =>
    QueueHelper.publish(Json.toJson(unknownIngestionEvent).toString)
  }

  Then("""^I should save Wise IO Log in database$""") { () =>

    eventually(timeout(30 seconds), interval(100 millis)) {
      storedWiseIOLog = Await.result(MongoDBHelper.getByDeviceId(wiseIngestionEvent.deviceId), 5 seconds)

      storedWiseIOLog.isDefined shouldEqual true
    }
  }

  Then("""^I should save unknown IO Log in database$""") { () =>

    eventually(timeout(30 seconds), interval(100 millis)) {
      storedUnknownIOLog = Await.result(MongoDBHelper.getByDeviceId(unknownIngestionEvent.deviceId), 5 seconds)

      storedUnknownIOLog.isDefined shouldEqual true
    }
  }

  And("""^get ingestion event info of Wise device$""") { () =>
    val ioLog = storedWiseIOLog.get

    ioLog.receivedAt shouldEqual wiseIngestionEvent.receivedAt
    ioLog.deviceId shouldEqual wiseIngestionEvent.deviceId
    ioLog.rawLog shouldEqual wiseIngestionEvent.rawLog
    ioLog.traceId shouldEqual wiseIngestionEvent.traceId
  }

  And("""^get ingestion event info of unknown device$""") { () =>
    val ioLog = storedUnknownIOLog.get

    ioLog.receivedAt shouldEqual unknownIngestionEvent.receivedAt
    ioLog.deviceId shouldEqual unknownIngestionEvent.deviceId
    ioLog.rawLog shouldEqual unknownIngestionEvent.rawLog
    ioLog.traceId shouldEqual unknownIngestionEvent.traceId

  }

  And("""^detect as Wise device$""") { () =>
    storedWiseIOLog.get.detectedDevice shouldEqual DeviceType.WISE
  }

  And("""^detect as unknown device$""") { () =>
    storedUnknownIOLog.get.detectedDevice shouldEqual DeviceType.UNKNOWN
  }

  And("""^extract Wise info$""") { () =>
    val wiseInfo = storedWiseIOLog.get.deviceLogInfo

    val expectedChannelInputs = Map(
      "channel_1" -> firstChannelInput.toString,
      "channel_2" -> secondChannelInput.toString
    )

    wiseInfo.isDefined shouldEqual true
    wiseInfo.get.uid shouldEqual wiseJsonLogBuilder.UID
    wiseInfo.get.deviceType shouldEqual DeviceType.WISE
    wiseInfo.get.logDateTime shouldEqual wiseJsonLogBuilder.TIM
    wiseInfo.get.channelInputs shouldEqual expectedChannelInputs
  }

  And("""^does not extract info$""") { () =>
    val deviceInfo = storedUnknownIOLog.get.deviceLogInfo

    deviceInfo shouldEqual None
  }
}
