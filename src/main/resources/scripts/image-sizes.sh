#!/usr/bin/env bash

NATIVE_PGO_SIZE=`ls -lh target/rest-service-demo-pgo | awk '{print $5}' | sed 's/M//'`
NATIVE_STATIC_SIZE=`ls -lh target/rest-service-demo-static | awk '{print $5}' | sed 's/M//'`
NATIVE_SIZE=`ls -lh target/rest-service-demo | awk '{print $5}' | sed 's/M//'`
COMPRESSED_NATIVE_SIZE=`ls -lh target/rest-service-demo.upx | awk '{print $5}' | sed 's/M//'`
JAR_SIZE=`ls -lh target/rest-service-demo-0.0.1-SNAPSHOT-exec.jar | awk '{print $5}' | sed 's/M//'`

# Chart of the image sizes
echo "JAR-File ${JAR_SIZE}
    Native-Image ${NATIVE_SIZE}
    Compressed-Image ${COMPRESSED_NATIVE_SIZE}
    Native-Image+Static ${NATIVE_STATIC_SIZE}
    Native-Image+PGO ${NATIVE_PGO_SIZE}" \
    | termgraph --title "Image Sizes" --width 60 --format '{:0.0f}' --color {green,} --suffix " MB"