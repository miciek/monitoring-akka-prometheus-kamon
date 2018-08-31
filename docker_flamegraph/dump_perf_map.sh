#!/bin/sh
JAVA_TOOL_OPTIONS=""
cd flamegraph/perf-map-agent/out

TARGET_PID="$1"

java -cp attach-main.jar:$JAVA_HOME/lib/tools.jar net.virtualvoid.perf.AttachOnce 1
mv /tmp/perf-1.map ../../perf-$TARGET_PID.map

