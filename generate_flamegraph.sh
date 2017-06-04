#!/bin/sh

set -e

if [ ! -d "flamegraph" ]; then
  echo "flamegraph directory doesn't exits. Please run ./install_flamegraph_tools.sh first."
  exit 1
fi

if [ -z "$1" ]; then
  echo "Usage: ./generate_flamegraph.sh JAVA_PID"
  exit 1
fi

PID="$1"

cd flamegraph

# take a 30-second profile at 99 hertz (samples per second) of all processes
sudo perf record -F 99 -a -g -- sleep 30 

cd perf-map-agent/out
# cache symbols for provided Java process and save to /tmp
java -cp attach-main.jar:/usr/lib/jvm/java-8-oracle/lib/tools.jar net.virtualvoid.perf.AttachOnce $PID
sudo chown root /tmp/perf-$PID.map
cd ../..

# generate the flame graph
sudo perf script | FlameGraph/stackcollapse-perf.pl | FlameGraph/flamegraph.pl --color=java --hash > flamegraph.svg
echo "Flame Graph generated: flamegraph/flamegraph.svg"

# clean up
rm -rf perf.data
sudo rm /tmp/perf-$PID.map

