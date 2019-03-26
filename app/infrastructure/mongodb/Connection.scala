package infrastructure.mongodb

import infrastructure.config.MongoDBConfig
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
    new Connection(config.uri, config.db)
  }
}
