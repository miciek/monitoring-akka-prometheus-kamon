#!/bin/sh

set -e

# take a 30-second profile at 99 hertz (samples per second) of all processes
sudo perf record -F 99 -a -g -- sleep 30 

# cache symbols for provided containers and save to /tmp
./get_docker_perf_map.sh <CONTAINER_NAME>

# generate the flame graph
sudo perf script | FlameGraph/stackcollapse-perf.pl | FlameGraph/flamegraph.pl --color=java --hash > flamegraph.svg
echo "Flame Graph generated: flamegraph.svg"

# clean up
rm -rf perf.data

