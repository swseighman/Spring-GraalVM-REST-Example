#!/usr/bin/env bash

JDK_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:jvm | numfmt --to=si | sed 's/.$//'`
NATIVE_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:native | numfmt --to=si | sed 's/.$//'`
NATIVE_STATIC_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:static | numfmt --to=si | sed 's/.$//'`
NATIVEDISTROLESS_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:distroless | numfmt --to=si | sed 's/.$//'`
JLINK_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:jlink | numfmt --to=si | sed 's/.$//'`
NATIVEPGO_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:pgo | numfmt --to=si | sed 's/.$//'`
COMPRESSED_IMG_SIZE=`docker inspect -f "{{ .Size }}" localhost/rest-service-demo:upx | numfmt --to=si | sed 's/.$//'`


# Chart of the image sizes
echo "JDK-Container ${JDK_IMG_SIZE}
    JLink-Container ${JLINK_IMG_SIZE}
    PGO-Container ${NATIVEPGO_IMG_SIZE}
    Native-Container ${NATIVE_IMG_SIZE}
    Compressed-Container ${COMPRESSED_IMG_SIZE}
    Static-Container ${NATIVE_STATIC_IMG_SIZE}
    Distroless-Container ${NATIVEDISTROLESS_IMG_SIZE}" \
    | termgraph --title "Container Sizes" --width 60 --format '{:0.0f}' --color {green,} --suffix " MB"