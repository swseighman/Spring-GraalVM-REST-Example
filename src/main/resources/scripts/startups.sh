#!/usr/bin/env bash

# Extract time to start from the logs - convert to ms
JDK_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-jvm) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`
NATIVE_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-native) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`
NATIVE_STATIC_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-static) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`
NATIVEDISTROLESS_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-distroless) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`
JLINK_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-jlink) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`
NATIVEPGO_START=`docker logs --tail 10 $(docker ps -q -f name=rest-service-demo-pgo) | grep -Eo 'Started .+ in [0-9]+.[0-9]+ sec' | sed 's/[[:space:]]sec$//' | sed 's/Started[[:space:]][a-zA-Z]\+[[:space:]]in[[:space:]]//'`

# Convert to ms
JDK_START=$(echo "${JDK_START}" | awk '{printf "%d", $1*1000}')
NATIVE_START=$(echo "${NATIVE_START}" | awk '{printf "%d", $1*1000}')
NATIVE_STATIC_START=$(echo "${NATIVE_STATIC_START}" | awk '{printf "%d", $1*1000}')
NATIVEDISTROLESS_START=$(echo "${NATIVEDISTROLESS_START}" | awk '{printf "%d", $1*1000}')
JLINK_START=$(echo "${JLINK_START}" | awk '{printf "%d", $1*1000}')
NATIVEPGO_START=$(echo "${NATIVEPGO_START}" | awk '{printf "%d", $1*1000}')

# Display as a chart
echo "
    JLink ${JLINK_START}
    JDK ${JDK_START}
    Static ${NATIVE_STATIC_START}
    Native-Image ${NATIVE_START}
    Distroless ${NATIVEDISTROLESS_START}
    Native-Image+PGO ${NATIVEPGO_START}" \
    | termgraph --title "App Start Time - via Container" --width 60  --color {green,} --suffix " ms"