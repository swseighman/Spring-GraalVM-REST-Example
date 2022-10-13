# Spring Boot / GraalVM REST Example

#### Prerequisites

Java 17 is used for this example, specifically GraalVM 22.2.0.1 and the `native-image` module.

If you intend on creating containers, `docker` or `podman` is required.

We'll use `termgraph` for visualizations in this example.  Since `termgraph` requires Python, it may be helpful to setup a Python virtual environment to install the necessary tools.

First, check your Python version (3.9 is recommended):

```
$ python3 --version
Python 3.9.7
```

Next, create a Python virtual environment:
 ```
 $ python3 -m venv demo-env
 ```

Activate the newly created virtual environment:
 ```
 $ source demo-env/bin/activate
 ```
 
With the virtual environment created, now install `termgraph`:
 ```
(demo-env) $ python3 -m pip install termgraph
 ```

>NOTE: If you are prompted to update `pip`, execute the following command:
 >```
 >$ python3 -m pip install --upgrade pip
 >```

Confirm `telegraph` installed correctly:
 ```
 $ termgraph
 usage: termgraph [-h] [--title TITLE] [--width WIDTH] [--format FORMAT] [--suffix SUFFIX] [--no-labels] [--no-values]
                 [--space-between] [--color [COLOR ...]] [--vertical] [--stacked] [--histogram] [--bins BINS]
                 [--different-scale] [--calendar] [--start-dt START_DT] [--custom-tick CUSTOM_TICK] [--delim DELIM]
                 [--verbose] [--label-before] [--version]
                 [filename]
```

Great, with all of the prerequisites in place, we can move to the next steps.

#### Building the Project

Let's begin by cloning the demo repository:

```
$ git clone https://github.com/swseighman/Basic-Native-Rest-Service
```
Now change directory to the new project:

```
$ cd Basic-Rest-Service
```

> **NOTE:** As an alternative to executing the following commands manually, there is a build script (`build.sh`) provided to build the project, create native image executables and build the container images.  Simply run:
>```
>./build.sh
>```
> Depending on your choice of build tools (Maven/Gradle), you will need to edit the script and comment/uncomment lines to accommodate your use case.

To build the project, execute:
```
mvn clean package
```

The `pom.xml` file contains configuration parameters (*via the GraalVM Native Image Build Tools for Maven plugin*) for the Tracing Agent. The following command will run unit tests and enable the Tracing Agent, thus generating the Tracing Agent configuration for your application:
```
$ mvn -Pnative -DskipNativeTests=true -DskipNativeBuild=true -Dagent=true test

```

To verify the newly created Tracing Agent configuration, execute the following command.

```
$ ls -l target/native/agent-output/test
total 52
drwxrwxr-x 2 sseighma sseighma  4096 Oct 13 13:19 agent-extracted-predefined-classes
-rw-rw-r-- 1 sseighma sseighma   538 Oct 13 13:19 jni-config.json
-rw-rw-r-- 1 sseighma sseighma    64 Oct 13 13:19 predefined-classes-config.json
-rw-rw-r-- 1 sseighma sseighma   448 Oct 13 13:19 proxy-config.json
-rw-rw-r-- 1 sseighma sseighma 27389 Oct 13 13:19 reflect-config.json
-rw-rw-r-- 1 sseighma sseighma   773 Oct 13 13:19 resource-config.json
-rw-rw-r-- 1 sseighma sseighma    51 Oct 13 13:19 serialization-config.json
```

Next, build the native image executable using the configuration files:
```
$ mvn -Pnative -Dagent=true -DskipTests package
```


```
$ docker-compose up
```

Browse to `localhost:8080/greeting`, where you should see:

```
{"id":1,"content":"Hello, World!"}
```

Or `curl http://localhost:8080/greeting`.

#### Building a Native Image Executable

We can build a standalone native image executable using the `native` profile which we can add to our custom containers later in this lab. Let's build a native executable:

```
mvn package -Pnative
```
>If you're using **Gradle**, execute the following command to build the native image executable:
>```
>./gradlew nativeCompile
>```

The result will produce a native image executable.

>**NOTE:** Depending on your OS distribution, you may need to install some additional packages.  For example, with Oracle Linux/RHEL/Fedora distributions, I recommend installing the `Development Tools` to cover all of the dependencies you'll need to compile a native executable.  *You would also add this option in the appropriate Dockerfile.*
>
>```
>$ sudo dnf group install "Development Tools"
>```

To run the native executable application, execute the following:

```
$ target/rest-service-demo
...<snip>
2022-04-04 11:27:58.076  INFO 27055 --- [           main] c.e.restservice.RestServiceApplication   : Started RestServiceApplication in 0.03 seconds (JVM running for 0.032)
```

#### Building a Static Native Image (x64 Linux only)

See [instructions](https://docs.oracle.com/en/graalvm/enterprise/22/docs/reference-manual/native-image/StaticImages/) for building and installing the required libraries.

After the process has been completed, copy `$ZLIB_DIR/libz.a` to `$GRAALVM_HOME/lib/static/linux-amd64/musl/`

Also add `x86_64-linux-musl-native/bin/x86_64-linux-musl-gcc` to your PATH.

Then execute:
```
mvn package -Pstatic
```

To run the static native executable application, execute the following:
```
target/rest-service-demo-static
```


#### Container Options

Within this repository, there are a few examples of deploying applications in various container environments, from distroless to full OS images.  Choose the appropriate version for your use case and build the images.

For example, to build the JAR version:

```
$ docker build -f Dockerfile.jvm -t localhost/rest-service-demo:jvm .
```

```
$ docker run -i --rm -p 8080:8080 localhost/rest-service-demo:jvm
```

Browse to `localhost:8080/greeting`, where you should see:

```
{"id":1,"content":"Hello, World!"}
```

You can repeat these steps for each container option:

* Dockerfile.jvm
* Dockerfile.native
* Dockerfile.stage
* Dockerfile.distroless
* Dockerfile.static (x64 Linux only)

There is also a `build-containers.sh` script provided to build the container images.

Notice the variation in container image size for each of the options:
```
$ docker images
localhost/rest-service-demo   upx            7d43ba8808df   23 hours ago    121MB
localhost/rest-service-demo   distroless     d09302740238   23 hours ago    37.2MB
localhost/rest-service-demo   native         18772054f07d   23 hours ago    154MB
localhost/rest-service-demo   pgo            bdbf1a188973   23 hours ago    179MB
localhost/rest-service-demo   jvm            e48787a8875a   23 hours ago    594MB
localhost/rest-service-demo   jlink          aabfde3c2c31   3 months ago    214MB
localhost/rest-service-demo   static         8bf6f43fd6cf   4 months ago    76.3MB
localhost/rest-service-demo   stage          428fdc2f55a0   4 months ago    177MB
```

Also, you can choose to compress the native image executable using the [upx](https://upx.github.io/) utility which will reduce your container size but have little impact on startup performance.

For example:

```
$ upx -7 -k target/rest-service-demo
Ultimate Packer for eXecutables
                          Copyright (C) 1996 - 2020
UPX 3.96        Markus Oberhumer, Laszlo Molnar & John Reiser   Jan 23rd 2020

        File size         Ratio      Format      Name
   --------------------   ------   -----------   -----------
  84541616 ->  26604004   31.47%   linux/amd64   rest-service-demo

Packed 1 file.
```
Using `upx` we reduced the native image executable size by ~32% (from **73 M** to **24 M**):
```
-rwxrwxr-x 1 sseighma sseighma  24M Apr  4 10:44 rest-service-demo
-rwxrwxr-x 1 sseighma sseighma  73M Apr  4 10:44 rest-service-demo.~
```

Our native image container is now **139 MB** (versus the uncompressed version at **190 MB**):

```
$ docker images
localhost/rest-service-demo            native           ff77aee72e96  8 seconds ago  139 MB
```