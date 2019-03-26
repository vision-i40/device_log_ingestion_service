package infrastructure.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Success, Try}

sealed case class MongoDBConfig(
  uri: String,
  db: String
)

object MongoDBConfig {
  private val MONGO_DB_CONFIG_PREFIX = "mongodb"
  private val URI = s"$MONGO_DB_CONFIG_PREFIX.uri"
  private val DB = s"$MONGO_DB_CONFIG_PREFIX.db"

  def apply(config: Config = ConfigFactory.load()): MongoDBConfig = {
    MongoDBConfig(
      uri = config.getString(URI),
      db = config.getString(DB)
    )
  }
}
