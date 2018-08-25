package infrastructure.mongodb

import config.MongoDBConfig
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed class Connection(connectionUrl: String, databaseName: String) {
  private val driver = MongoDriver()
  private val parsedUri = MongoConnection.parseURI(connectionUrl)

  def collection(collectionName: String): Future[BSONCollection] = {
    Future
      .fromTry(parsedUri.map(driver.connection))
      .flatMap(_.database(databaseName))
      .map((db: DefaultDB) => db[BSONCollection](collectionName))
  }
}

object Connection {
  val config: MongoDBConfig = MongoDBConfig()

  def apply(): Connection = {
    new Connection(buildMongoDBUrl(config), config.db)
  }

  private def buildMongoDBUrl(config: MongoDBConfig): String = {
    s"mongodb://${config.username}:${config.password}@${config.host}:${config.port}/${config.db}"
  }
}
