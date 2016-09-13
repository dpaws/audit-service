# Docker in Production using AWS - Audit Service

This service is part of the sample application included with the Pluralsight course Docker in Production using Amazon Web Services.

## Quick Start

To run tests and create an application "fat" JAR:

```
$ make test
```

This will build a development image, run tests and create a fat JAR that is output to the local `target` folder.

To run the fat JAR:

```
$ java -jar target/audit-service-0.1.0-fat.jar -conf=src/conf/config.json
Sep 07, 2016 1:12:32 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
Server started

```

By default the audit service will run on port 33000.  

> You can set the `HTTP_PORT` environment variable to change this port.

In another terminal you can test the endpoint as follows:

```
$ curl localhost:33000
```