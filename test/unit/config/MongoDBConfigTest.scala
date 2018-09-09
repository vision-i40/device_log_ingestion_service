package unit.config

import com.typesafe.config.Config
import config.MongoDBConfig
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}
import org.mockito.Mockito._

class MongoDBConfigTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  val config: Config = mock[Config]

  private val MONGO_DB_CONFIG_PREFIX = "mongodb"
  private val URI = s"$MONGO_DB_CONFIG_PREFIX.uri"
  private val DB = s"$MONGO_DB_CONFIG_PREFIX.db"

  val uri = "mongodb://user:password@localhost:27017/test?retryWrites=true"
  val db = "io_logs"

  override def beforeEach(): Unit = {
    Mockito.reset(config)

    when(config.getString(URI)).thenReturn(uri)
    when(config.getString(DB)).thenReturn(db)
  }

  behavior of "MongoDB Config"
  it should "get uri config value" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(URI)
    mongoDbConfig.uri shouldEqual uri
  }

  it should "get db name config value" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(DB)
    mongoDbConfig.db shouldEqual db
  }

}
