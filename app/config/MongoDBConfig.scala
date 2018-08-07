package config

import com.typesafe.config.{Config, ConfigFactory}

sealed case class MongoDBConfig(
  host: String,
  port: Int,
  db: String,
  username: String,
  password: String
)

object MongoDBConfig {
  private val MONGO_DB_CONFIG_PREFIX = "mongodb"
  private val HOST = s"$MONGO_DB_CONFIG_PREFIX.host"
  private val PORT = s"$MONGO_DB_CONFIG_PREFIX.port"
  private val DB = s"$MONGO_DB_CONFIG_PREFIX.db"
  private val USERNAME = s"$MONGO_DB_CONFIG_PREFIX.username"
  private val PASSWORD = s"$MONGO_DB_CONFIG_PREFIX.password"

  def apply(config: Config = ConfigFactory.load()): MongoDBConfig = {
    MongoDBConfig(
      host = config.getString(HOST),
      port = config.getInt(PORT),
      db = config.getString(DB),
      username = config.getString(USERNAME),
      password = config.getString(PASSWORD)
    )
  }
}
