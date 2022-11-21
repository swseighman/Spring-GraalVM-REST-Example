JARFILE_NAME=rest-service-0.0.1-SNAPSHOT.jar

everything: clean profile compile

build:
	# ./gradlew --stop
	# export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=true && ./gradlew build
	# export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=true && ./gradlew bootJar
	export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=true && ./mvnw clean package
.PHONY: build

compile:
	# export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=false && ./gradlew nativeCompile
	export USE_NATIVE_IMAGE_JAVA_PLATFORM_MODULE_SYSTEM=false && tracing=false && ./mvnw -Pnative native:compile
.PHONY: compile

clean:
	./gradlew clean
.PHONY: clean

profile: build
	@echo "Profiling to generate config.."
	rm -f src/main/resources/META-INF/native-image/config-*.json
	# Run this in the backgournd
	java -DspringAot=true \
		-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image,track-reflection-metadata=false \
		-jar build/libs/$(JARFILE_NAME) \
		-Djava.util.logging.config.file=./src/main/resources/logging.properties & echo $$! > .pid
	sleep 5
	# Hit the Rest endpoint
	curl -v http://localhost:8080/greeting
    # Kill the Java process
	cat .pid | xargs kill
	rm -f .pid
.PHONY: profile

run:
	echo "Profiling to generate config.."
	rm -f src/main/resources/META-INF/native-image/config-*.json
	# Run this in the backgournd
	java -DspringAot=true \
		-agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image,track-reflection-metadata=false \
		-jar build/libs/$(JARFILE_NAME) \
		-Djava.util.logging.config.file=./src/main/resources/logging.properties & echo $$! > .pid
.PHONY: run