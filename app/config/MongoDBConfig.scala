package config

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.{Success, Try}

sealed case class MongoDBConfig(
  host: String,
  port: Int,
  db: String,
  username: Option[String],
  password: Option[String]
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
      username = getUsername(config),
      password = getPassword(config)
    )
  }

  def getUsername(config: Config): Option[String] = Try(config.getString(USERNAME)) match {
    case Success(username: String) => Some(username)
    case _ => None
  }

  def getPassword(config: Config): Option[String] = Try(config.getString(PASSWORD)) match {
    case Success(password: String) => Some(password)
    case _ => None
  }
}
