
# Device Log Ingestion Service

### Tech Stack
    - [Scala](https://www.scala-lang.org/)
    - [SBT](https://www.scala-sbt.org/)
    - [Docker](https://www.docker.com/)

### Running
Before run the application, setup dependencies:

```
docker-compose up
```

Use the following at the command line to start up Play in development mode:

```
sbt run
```

Play will start up on the HTTP port at http://localhost:9000/. You don't need to deploy or reload anything -- changing any source code while the server is running will automatically recompile and hot-reload the application on the next HTTP request. 

### Testing

Running unit tests:

```
sbt unit
```

Running integration tests:

```
sbt integration
```

Running functional tests:

```
sbt cucumber
```


