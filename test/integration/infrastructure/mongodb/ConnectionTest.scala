package integration.infrastructure.mongodb

import config.MongoDBConfig
import infrastructure.mongodb.Connection
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class ConnectionTest extends AsyncFlatSpec with Matchers {
  val config = MongoDBConfig()

  behavior of "Mongo DB Connection"
  it should "connect to database provided db and collection" in {
    val collectionName = "a_collection"
    val collectionFuture = Connection().collection(collectionName)

    collectionFuture.map { collection =>
      collection.name shouldEqual collectionName
      collection.db.name shouldEqual config.db
    }
  }

}
