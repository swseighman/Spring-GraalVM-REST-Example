#!/usr/bin/env bash

echo -ne "Building containers ...\n"
docker login container-registry.oracle.com
docker build -f ./Dockerfile.jvm -t localhost/rest-service-demo:jvm . > /dev/null 2>&1
echo -ne "Building JVM container ... "
echo "Done."
docker build -f ./Dockerfile.jlink -t localhost/rest-service-demo:jlink . > /dev/null 2>&1
echo -ne "Building jlink container ... "
echo "Done."
docker build -f ./Dockerfile.native -t localhost/rest-service-demo:native . > /dev/null 2>&1
echo -ne "Building Native Image container ... "
echo "Done."
docker build -f ./Dockerfile.upx -t localhost/rest-service-demo:upx . > /dev/null 2>&1
echo -ne "Building Compressed Native Image container ... "
echo "Done."
# docker build -f ./Dockerfile.stage -t localhost/rest-service-demo:stage . > /dev/null 2>&1
# echo -ne "Multistage build ... "
# echo "Done."
docker build -f ./Dockerfile.distroless -t localhost/rest-service-demo:distroless . > /dev/null 2>&1
echo -ne "Building Distroless container ... "
echo "Done."
docker build -f ./Dockerfile.pgo -t localhost/rest-service-demo:pgo . > /dev/null 2>&1
echo -ne "Building PGO Native Image container ... "
echo "Done."
docker build -f ./Dockerfile.static -t localhost/rest-service-demo:static . > /dev/null 2>&1
echo -ne "Building Static Native Image container ... "
echo "Done."
echo ""
echo "Build complete!"
echo ""
echo "See README for instructions on how to run the application and individual containers."
echo ""