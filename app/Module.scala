import com.google.inject.AbstractModule
import infrastructure.QueueSubscriber
import io_log_ingestion.IOLogIngestionSubscriber
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[QueueSubscriber]).to(classOf[IOLogIngestionSubscriber]).asEagerSingleton()
  }
}
