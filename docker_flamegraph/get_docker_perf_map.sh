#!/bin/sh
CONTAINER="$1"

PID=$(docker inspect --format '{{.State.Pid}}' $CONTAINER)
echo $PID

docker exec -it $CONTAINER flamegraph/dump_perf_map.sh $PID
ls -l perf-$PID.map
mv perf-$PID.map /tmp/
