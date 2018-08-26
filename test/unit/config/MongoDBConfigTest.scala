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
  private val HOST = s"$MONGO_DB_CONFIG_PREFIX.host"
  private val PORT = s"$MONGO_DB_CONFIG_PREFIX.port"
  private val DB = s"$MONGO_DB_CONFIG_PREFIX.db"
  private val USERNAME = s"$MONGO_DB_CONFIG_PREFIX.username"
  private val PASSWORD = s"$MONGO_DB_CONFIG_PREFIX.password"

  val host = "localhost"
  val port = 27017
  val db = "io_logs"
  val username = "mongodb"
  val password = "mongodb"

  override def beforeEach(): Unit = {
    Mockito.reset(config)

    when(config.getString(HOST)).thenReturn(host)
    when(config.getInt(PORT)).thenReturn(port)
    when(config.getString(DB)).thenReturn(db)
  }

  behavior of "MongoDB Config"
  it should "get host config value" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(HOST)
    mongoDbConfig.host shouldEqual host
  }

  it should "get port config value" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getInt(PORT)
    mongoDbConfig.port shouldEqual port
  }

  it should "get db name config value" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(DB)
    mongoDbConfig.db shouldEqual db
  }

  it should "get username config value" in {
    when(config.getString(USERNAME)).thenReturn(username)

    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(USERNAME)
    mongoDbConfig.username shouldEqual Some(username)
  }

  it should "get password config value" in {
    when(config.getString(PASSWORD)).thenReturn(password)

    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(PASSWORD)
    mongoDbConfig.password shouldEqual Some(password)
  }

  it should "return None when username config value is not provided" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(USERNAME)
    mongoDbConfig.username shouldEqual None
  }

  it should "return None when password config value is not provided" in {
    val mongoDbConfig: MongoDBConfig = MongoDBConfig(config)

    verify(config, times(1)).getString(PASSWORD)
    mongoDbConfig.password shouldEqual None
  }
}
