# Yields service

For a given postcode, once validated, the service return its average yield. The service uses postcodes.io to validate 
a postcode, and the `outcode-stats-service` to fall back in case there are not enough data-points to calculate the yield
for full postcode (hence returning the yield for the outcode only).
The service OAS is accessible at `<base-path>/swagger-ui`, whereas its health status at `<base-path>/health-ui`.

This project uses Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

##Configuration
The `application.properties` file holds the configuration parameters required for the application to start. At run-time,
the service looks for such values in a Kubernetes secret and config-map. This can be disabled by setting the property 
`quarkus.kubernetes-config.enabled` to **false**.


##Usage

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

### Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `yelds-1.0-SNAPSHOT-runner.jar` file in the `/target` directory. Be aware that it’s not an _über-jar_ as
the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/yelds-1.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/yelds-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html
.

##Project status

The service relies on 3 services: 
* postcodes
* outcode-stats
* property-data

The service will NOT be live if the first 2 services are unreachable. Unfortunately
property-data does not expose a status endpoint, hence the service adds a circuit breaker, 
and a timeout of 5 seconds in case it becomes unavailable. This behaviour might change in the future.

