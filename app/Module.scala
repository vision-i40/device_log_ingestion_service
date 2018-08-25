import com.google.inject.AbstractModule
import io_log_ingestion.IOLogIngestionSubscriber
import org.joda.time.DateTimeZone
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {
  DateTimeZone.setDefault(DateTimeZone.UTC)

  override def configure(): Unit = {
    IOLogIngestionSubscriber()
  }
}
