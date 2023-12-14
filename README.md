# Spring Boot / GraalVM REST Example

### Prerequisites

Java 17 is used for this example, specifically GraalVM for JDK 17. 

Download GraalVM for JDK 17 [here](https://www.oracle.com/java/technologies/downloads/#graalvmjava17) or use SDKMAN to install GraalVM.

You also have the option of using [script-friendly URLs](https://www.oracle.com/java/technologies/jdk-script-friendly-urls/) (including containers) to automate downloads.

Spring Boot 3.2.0 with native support is used and requires GraalVM 23.0.

Oracle Linux 8/9 `(x86_64)` was used as the underlying OS as some features are only available on `x86_64` platforms.

You can use choose to use `Maven` or `Gradle` as a build tool but `Maven` is highlighted in the example project.

You'll also need `git`.

If you intend on creating containers, `docker` or `podman` is required. See docs [here](https://docs.oracle.com/en/operating-systems/oracle-linux/podman/podman-InstallingPodmanandRelatedUtilities.html#podman-install).

**Optional**

We'll use `termgraph` for visualizations in this example.  Since `termgraph` requires Python, it may be helpful to setup a Python virtual environment to install the necessary tools.

First, check your Python version (3.9 is recommended):

```
$ python3 --version
Python 3.9.7
```
If you need to install Python 3.9.x, execute:
```
$ sudo dnf install python39 -y
```
Then change what version the system uses:
```
$ sudo alternatives --config python3

There are 2 programs which provide 'python3'.

  Selection    Command
-----------------------------------------------
*+ 1           /usr/bin/python3.6
   2           /usr/bin/python3.9

Enter to keep the current selection[+], or type selection number: 2
$ python3 --version
Python 3.9.13
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

>**NOTE:** If you receive the following error:
>```
>/usr/bin/python3: No module named pip
>```
>
>You can install `pip` by executing the following command:
>
>```
>$ sudo dnf install python3-pip -y
>```
> 
> Or if you are prompted to update `pip`, execute the following command:
 >```
 >$ python3 -m pip install --upgrade pip
 >```

Confirm `termgraph` installed correctly:
 ```
 $ termgraph
 usage: termgraph [-h] [--title TITLE] [--width WIDTH] [--format FORMAT] [--suffix SUFFIX] [--no-labels] [--no-values]
                 [--space-between] [--color [COLOR ...]] [--vertical] [--stacked] [--histogram] [--bins BINS]
                 [--different-scale] [--calendar] [--start-dt START_DT] [--custom-tick CUSTOM_TICK] [--delim DELIM]
                 [--verbose] [--label-before] [--version]
                 [filename]
```

Great, with all of the prerequisites in place, we can move to the next steps.

### Building the Project

Let's begin by cloning the demo repository:

```
(demo-env) $ git clone https://github.com/swseighman/Spring-GraalVM-REST-Example
```
Now change directory to the new project:

```
(demo-env) $ cd Spring-GraalVM-REST-Example
```

To build the project, execute:
```
(demo-env) $ ./mvnw clean package
```
The previous command generates an executable `.jar` file in the `target` directory.

The following command will run unit tests and enable the Tracing Agent, thus generating the Tracing Agent configuration for your application:

```
(demo-env) $ ./mvnw -PnativeTest -DskipNativeTests=true -DskipNativeBuild=true -Dagent=true test
```

To verify the newly created Tracing Agent configuration, execute the following command.

```
(demo-env) $ ls -l target/native/agent-output/test
total 52
drwxrwxr-x 2 sseighma sseighma  4096 Oct 13 13:19 agent-extracted-predefined-classes
-rw-rw-r-- 1 sseighma sseighma   538 Oct 13 13:19 jni-config.json
-rw-rw-r-- 1 sseighma sseighma    64 Oct 13 13:19 predefined-classes-config.json
-rw-rw-r-- 1 sseighma sseighma   448 Oct 13 13:19 proxy-config.json
-rw-rw-r-- 1 sseighma sseighma 27389 Oct 13 13:19 reflect-config.json
-rw-rw-r-- 1 sseighma sseighma   773 Oct 13 13:19 resource-config.json
-rw-rw-r-- 1 sseighma sseighma    51 Oct 13 13:19 serialization-config.json
```

Next, build the native image executable using the configuration files. The `pom.xml` file contains configuration parameters *(via the Maven resources plugin)* to move the Tracing Agent configuration files from `target/native/agent-output/test` to the `/src/main/resources/META-INF/native-image` directory.

> **NOTE:** With the introduction of Spring Boot 3.0, there is a new goal to trigger native image compilation, see more information on Spring Boot 3.0 [here](https://docs.spring.io/spring-boot/docs/3.0.0/reference/html/native-image.html#native-image.developing-your-first-application.native-build-tools.maven).

```
(demo-env) $ ./mvnw -Pnative native:compile -Dagent=true -DskipTests
```
>**NOTE:** If you're using an Oracle Cloud Infrastructure (OCI) instance, you may need to install the `libstdc` library:
>```
>$ sudo dnf config-manager --set-enabled ol9_codeready_builder
>$ sudo dnf install libstdc++-static -y

To run the native executable application, execute the following:

```
(demo-env) $ target/rest-service-demo
...<snip>
2022-04-04 11:27:58.076  INFO 27055 --- [           main] c.e.restservice.RestServiceApplication   : Started RestServiceApplication in 0.026 seconds (JVM running for 0.027)
```

### Building a PGO Executable

You can optimize this native executable even more for additional performance gain and higher throughput by applying Profile-Guided Optimizations (PGO).

With PGO you can collect the profiling data in advance and then feed it to the `native-image` tool, which will use this information to optimize the performance of the resulting binary.

>**NOTE:** PGO is available with GraalVM Enterprise Edition only.

First, we'll build an instrumented native executable using the following command: 
```
(demo-env) $ ./mvnw -Ppgo-inst -DskipTests
```

>**NOTE:** If you encounter the following error:
>
>```
>Error: Main entry point class 'com.example.restservice.RestServiceApplication' neither found on the classpath nor on the modulepath.
>```
>
>comment out the following line in the `<configuration>` section:
>```
><mainClass>${exec.mainClass}</mainClass>
>```

Next, you'll need to run the newly created instrumented app to generate the profile information:

```
(demo-env) $  target/rest-service-demo-pgoinst
```

Finally, we'll build an optimized native executable (using the `pom.xml` profile to specify the path to the collected profile information):
```
(demo-env) $ ./mvwn -Ppgo -DskipTests
```



### Building a Static Native Image (x64 Linux only)

See [instructions](https://docs.oracle.com/en/graalvm/enterprise/22/docs/reference-manual/native-image/guides/build-static-executables/) for building and installing the required libraries.

>**NOTE:** Confirmed the static image will build using `musl 10.2.1` (fails to build with `musl 11.2.1`).
>To download `musl 10.2.1`, click on the `more ...` link under **toolchains** and then choose the `10.2.1` directory.
>![](images/musl-download.png)
>![](images/musl-download-1.png)

After the process has been completed, copy `$ZLIB_DIR/libz.a` to `$GRAALVM_HOME/lib/static/linux-amd64/musl/`

Also add `x86_64-linux-musl-native/bin/` to your PATH.

Then execute:
```
(demo-env) $ ./mvnw -Pstatic -DskipTests
```

To run the static native executable application, execute the following:
```
(demo-env) $ target/rest-service-demo-static
```


### Deployment Options

Included in this example are options to create/deploy your application using containers using traditional methods plus Buildpacks and Kubernetes.

#### Using Buildpacks

Cloud Native Buildpacks are also supported to generate a lightweight container containing a native executable.

To use Buildpacks, enter the following command:

```
$ ./mvnw -Pnative spring-boot:build-image
```

When the process is completed, you should have a new image:

```
$ docker images
REPOSITORY                                TAG              IMAGE ID       CREATED         SIZE
rest-service-demo                         0.0.1-SNAPSHOT   d4ddd877a035   43 years ago    130MB
```

#### Using Containers

Within this repository, there are a few examples of deploying applications in various container environments, from distroless to full OS images.  Choose the appropriate version for your use case and build the images.

For example, to build the JAR version:

```
(demo-env) $ docker build -f src/main/resources/containers/Dockerfile.jvm -t localhost/rest-service-demo:jvm .
```

```
(demo-env) $ docker run -i --rm -p 8080:8080 localhost/rest-service-demo:jvm
```

Browse to `localhost:8080/greeting`, where you should see:

```
{"id":1,"content":"Hello, World!"}
```

Or browse to `http://localhost:8080/greeting?name=User`

You should see:
```
{"id":12,"content":"Hello, User!"}
```


You can repeat these steps for each container option:

* Dockerfile.jvm
* Dockerfile.native
* Dockerfile.pgo
* Dockerfile.upx
* Dockerfile.stage
* Dockerfile.jlink
* Dockerfile.distroless
* Dockerfile.static (x64 Linux only)

There is also a `build-containers.sh` script provided to build the container images. You should run the script from the root project directory:

```
(demo-env) $ src/main/resources/containers/build-containers.sh
```

>**NOTE:** If you're building on macOS or Windows, the containerized native image apps won't execute due to base architecture differences.  You'll need to use the multi-stage container build (`Dockerfile.stage`) to run a native executable in those environments.
>The following containers will not run as expected:
>```
>rest-service-demo:upx
>rest-service-demo:distroless
>rest-service-demo:native
>rest-service-demo:pgo
>rest-service-demo:static
>```

Notice the variation in container image size for each of the options:
```
(demo-env) $ docker images
localhost/rest-service-demo   upx            7d43ba8808df   23 hours ago    121MB
localhost/rest-service-demo   distroless     d09302740238   23 hours ago    37.2MB
localhost/rest-service-demo   native         18772054f07d   23 hours ago    154MB
localhost/rest-service-demo   pgo            bdbf1a188973   23 hours ago    179MB
localhost/rest-service-demo   jvm            e48787a8875a   23 hours ago    594MB
localhost/rest-service-demo   jlink          aabfde3c2c31   3 months ago    214MB
localhost/rest-service-demo   static         8bf6f43fd6cf   4 months ago    76.3MB
localhost/rest-service-demo   stage          428fdc2f55a0   4 months ago    177MB
```

To deploy all of the containers, run:
```
(demo-env) $ docker-compose -f src/main/resources/containers/docker-compose.yml up -d
[+] Running 7/7
 ⠿ Container rest-service-demo-distroless  Running                                                 0.0s
 ⠿ Container rest-service-demo-jvm         Running                                                 0.0s
 ⠿ Container rest-service-demo-native      Running                                                 0.0s
 ⠿ Container rest-service-demo-upx         Running                                                 0.0s
 ⠿ Container rest-service-demo-static      Running                                                 0.0s
 ⠿ Container rest-service-demo-pgo         Running                                                 0.0s
 ⠿ Container rest-service-demo-jlink       Started                                                 0.4s
```

>NOTE: If you're using `podman`, you can install `podman-compose` using the instructions [here](https://github.com/containers/podman-compose).

```
(demo-env) $ docker ps
CONTAINER ID   IMAGE                                    COMMAND                  CREATED       STATUS       PORTS
       NAMES
5fef9e8aec02   localhost/rest-service-demo:jvm          "java -jar app.jar -…"   8 hours ago   Up 8 hours   0.0.0.0:8081->8080/tcp   rest-service-demo-jvm
907a0e4e9513   localhost/rest-service-demo:static       "/app -Xms64m -Xmx64m"   8 hours ago   Up 8 hours   0.0.0.0:8087->8080/tcp   rest-service-demo-static
959baabdf130   localhost/rest-service-demo:upx          "/app -Xms64m -Xmx64m"   8 hours ago   Up 8 hours   0.0.0.0:8083->8080/tcp   rest-service-demo-upx
54281b6e59d2   localhost/rest-service-demo:native       "/app -Xms64m -Xmx64m"   8 hours ago   Up 8 hours   0.0.0.0:8082->8080/tcp   rest-service-demo-native
a8e45684e8f3   localhost/rest-service-demo:pgo          "/app -Xms64m -Xmx64m"   8 hours ago   Up 8 hours   0.0.0.0:8086->8080/tcp   rest-service-demo-pgo
66e4f93d3dab   localhost/rest-service-demo:distroless   "/app -Xms64m -Xmx64m"   8 hours ago   Up 8 hours   0.0.0.0:8084->8080/tcp   rest-service-demo-distroless
```

To stop the containers, execute:

```
(demo-env) $ docker-compose -f src/main/resources/containers/docker-compose.yml down
```

### Deploying to Kubernetes Using Maven (minikube)

Using the Maven [Eclipse JKube plugin](https://www.eclipse.org/jkube/), we can deploy the application to `minikube` (or any Kubernetes platform) directly.

>NOTE: Instructions for installing `minikube`` are [here](https://minikube.sigs.k8s.io/docs/start/).

For this example, the **no configuration** option, meaning we'll use the built-in `k8s` profile.

First, we'll configure our environment to use the Docker Engine inside of `minikube`:

```
(demo-env) $ eval $(minikube docker-env)
```

Next, we'll need to build a container image:
```
(demo-env) $ mvn k8s:build
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.example:rest-service-demo >--------------------
[INFO] Building rest-service-demo 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- kubernetes-maven-plugin:1.12.0:build (default-cli) @ rest-service-demo ---
[INFO] k8s: Building Docker image in Kubernetes mode
[INFO] k8s: Using Dockerfile: /home/sseighma/code/Spring-GraalVM-REST-Example/Dockerfile
[INFO] k8s: Using Docker Context Directory: /home/sseighma/code/Spring-GraalVM-REST-Example
[INFO] k8s: [example/rest-service-demo:latest]: Created docker-build.tar in 6 seconds 
[INFO] k8s: [example/rest-service-demo:latest]: Built image sha256:a4fc1
[INFO] k8s: [example/rest-service-demo:latest]: Removed old image sha256:f9fc4
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  15.440 s
[INFO] Finished at: 2023-02-22T15:59:16-05:00
[INFO] ------------------------------------------------------------------------
```

Confirm your image was created:

```
(demo-env) $ docker images
﻿example/rest-service-demo    latest    a4fc17272ed0   8 minutes ago       157MB
```

Next, we'll deploy the application to `minikube`:

```
(demo-env) $ mvn k8s:resource k8s:apply
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.example:rest-service-demo >--------------------
[INFO] Building rest-service-demo 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- kubernetes-maven-plugin:1.15.0:resource (default-cli) @ rest-service-demo ---
[INFO] k8s: Using Dockerfile: /home/sseighma/code/Spring-GraalVM-REST-Example/Dockerfile
[INFO] k8s: Using Docker Context Directory: /home/sseighma/code/Spring-GraalVM-REST-Example
[INFO] k8s: Using resource templates from /home/sseighma/code/Spring-GraalVM-REST-Example/src/main/jkube
[INFO] k8s: jkube-controller: Adding a default Deployment
[INFO] k8s: jkube-service: Adding a default service 'rest-service-demo' with ports [8080]
[INFO] k8s: jkube-healthcheck-spring-boot: Adding readiness probe on port 8080, path='/actuator/health', scheme='HTTP', with initial delay 10 seconds
[INFO] k8s: jkube-healthcheck-spring-boot: Adding liveness probe on port 8080, path='/actuator/health', scheme='HTTP', with initial delay 180 seconds
[INFO] k8s: jkube-service-discovery: Using first mentioned service port '8080' 
[INFO] k8s: jkube-revision-history: Adding revision history limit to 2
[INFO] k8s: validating /home/sseighma/code/Spring-GraalVM-REST-Example/target/classes/META-INF/jkube/kubernetes/rest-service-demo-service.yml resource
[INFO] k8s: validating /home/sseighma/code/Spring-GraalVM-REST-Example/target/classes/META-INF/jkube/kubernetes/rest-service-demo-deployment.yml resource
[INFO] 
[INFO] --- kubernetes-maven-plugin:1.15.0:apply (default-cli) @ rest-service-demo ---
[INFO] k8s: Using Kubernetes at https://192.168.49.2:8443/ in namespace null with manifest /home/sseighma/code/Spring-GraalVM-REST-Example/target/classes/META-INF/jkube/kubernetes.yml 
[INFO] k8s: Creating a Service from kubernetes.yml namespace default name rest-service-demo
[INFO] k8s: Created Service: target/jkube/applyJson/default/service-rest-service-demo.json
[INFO] k8s: Creating a Deployment from kubernetes.yml namespace default name rest-service-demo
[INFO] k8s: Created Deployment: target/jkube/applyJson/default/deployment-rest-service-demo.json
[INFO] k8s: HINT: Use the command `kubectl get pods -w` to watch your pods start up
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  9.534 s
[INFO] Finished at: 2023-02-22T16:00:42-05:00
[INFO] ------------------------------------------------------------------------

```

You can see the pod running:
```
(demo-env) $ k get pods -w
﻿NAME                                READY   STATUS    RESTARTS   AGE
rest-service-demo-5bc6dfbb54-g6cvv   1/1     Running   0          6s
```

To get the URL for the service, execute the following command (it will automatically open a browser tab):

```
(demo-env) $ minikube service rest-service-demo
﻿|-----------|-------------------|-------------|---------------------------|
| NAMESPACE |       NAME        | TARGET PORT |            URL            |
|-----------|-------------------|-------------|---------------------------|
| default   | rest-service-demo | http/8080   | http://192.168.49.2:30716 |
|-----------|-------------------|-------------|---------------------------|
🎉  Opening service default/rest-service-demo in default browser...
```

You'll need to add a proper endpoint to the URL.  For example:

[http://192.168.49.2:30716/greeting](http://192.168.49.2:30716/greeting)

or 

[http://192.168.49.2:30716/greeting?name=User](http://192.168.49.2:30716/greeting?name=User)

Finally, you can delete the deployment by executing this command:
```
(demo-env) $ mvn k8s:undeploy
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.example:rest-service-demo >--------------------
[INFO] Building rest-service-demo 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- kubernetes-maven-plugin:1.15.0:undeploy (default-cli) @ rest-service-demo ---
[INFO] k8s: Deleting resource Deployment default/rest-service-demo
[INFO] k8s: Deleting resource Service default/rest-service-demo
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.094 s
[INFO] Finished at: 2023-02-22T16:03:38-05:00
[INFO] ------------------------------------------------------------------------

```

### Compressing the Native Image Executable

You can choose to compress the native image executable using the [upx](https://upx.github.io/) utility which will reduce your container size but have little impact on startup performance.

For example:

```
(demo-env) $ upx -7 -k target/rest-service-demo
Ultimate Packer for eXecutables
                          Copyright (C) 1996 - 2020
UPX 3.96        Markus Oberhumer, Laszlo Molnar & John Reiser   Jan 23rd 2020

        File size         Ratio      Format      Name
   --------------------   ------   -----------   -----------
  84541616 ->  26604004   31.47%   linux/amd64   rest-service-demo

Packed 1 file.
```
Using `upx` we reduced the native image executable size by ~33% (from **48 MB** to **16 MB**):
```
-rwxrwxr-x 1 sseighma sseighma  16M Oct 13 13:28 rest-service-demo
-rwxrwxr-x 1 sseighma sseighma  48M Oct 13 13:28 rest-service-demo.~
```

Our native image container is now **121 MB** (versus the uncompressed version at **154 MB**):

```
(demo-env) $ docker images
localhost/rest-service-demo   upx            7d43ba8808df   26 hours ago    121MB
localhost/rest-service-demo   native         18772054f07d   26 hours ago    154MB
```

### Enabling JDK Flight Recorder

To build a native image with the JFR events support, you first need to enable JFR at image build time. The `pom.xml` includes parameters to build a native executable with JFR support enabled (in the `<buildArgs`):

```
    <buildArgs>
	<!-- Quick build mode is enabled  -->
	<buildArg>-Ob</buildArg>
	<!-- G1 is supported on Linux only, comment out next line if on another platform -->
	<buildArg>--gc=G1</buildArg>
	<!-- Enable JFR support -->
	<buildArg>--enable-monitoring=jfr</buildArg>
	<!-- Show exception stack traces for exceptions during image building -->
	<buildArg>-H:+ReportExceptionStackTraces</buildArg>
    </buildArgs>
```
					
After building the native executable, to enable JFR and start a recording, execute the following command:
```
(demo-env) $ target/rest-service-demo -XX:+FlightRecorder -XX:StartFlightRecording="filename=recording.jfr"
```
You will notice a `recording.jfr` file in the project root directory.  You can import this file into JDK Mission Control or view events via the command line. See more info [here](https://docs.oracle.com/en/java/java-components/jdk-mission-control/8/user-guide/using-jdk-flight-recorder.html#GUID-D38849B6-61C7-4ED6-A395-EA4BC32A9FD6).


Currently, JFR support includes these limitations:
* JFR events recording is not supported on GraalVM distribution for Windows.

See the [docs](https://docs.oracle.com/en/graalvm/enterprise/22/docs/reference-manual/native-image/debugging-and-diagnostics/JFR/) for additional information.


### Viewing Project Metrics

If you're curious about image and container sizes or want to see the startup times for the containers created in this example, there are scripts located in the `src/main/resources/scripts` directory that create bar graphs for each.

To compare images sizes, run the `image-sizes.sh` script:

![](images/image-size.png)

To compare container sizes, run the `container-sizes.sh` script:

![](images/container-size.png)

To compare startup times, run the `startups.sh` script:

![](images/startup.png)

The graphs are generated using the `termgraph` tool we installed earlier.

Your results will vary depending on the system used to host the project examples.