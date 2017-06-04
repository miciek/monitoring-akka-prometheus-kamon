#!/bin/sh

# Based on: https://medium.com/netflix-techblog/java-in-flames-e763b3d32166

mkdir flamegraph
cd flamegraph

# 1. Install perf
sudo apt install -y perf-tools-common
sudo apt install -y linux-tools-common
sudo apt install -y linux-tools-generic

# 2. Build perf-map-agent
sudo apt install -y cmake
export JAVA_HOME=/usr/lib/jvm/java-8-oracle/
git clone --depth=1 https://github.com/jrudolph/perf-map-agent
cd perf-map-agent
cmake .
make
cd ..

# 3. Install FlameGraph
git clone --depth=1 https://github.com/brendangregg/FlameGraph

