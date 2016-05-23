#!/bin/sh
app_host=localhost
app_port=8888
if [ "$(uname)" == "Darwin" ]; then
    app_host=$(ifconfig | grep -oE "inet \d+.\d+.\d+.\d+" | cut -c 6- | grep "192" | head -n 1)
fi

mkdir target %> /dev/null
cp src/main/resources/prometheus.yml target/
sed -i '' "s/HOST/$app_host/; s/PORT/$app_port/" target/prometheus.yml

echo "Killing prometheus container (if it's running)"
docker rm -f prometheus &> /dev/null
echo "Starting prometheus container on port 9090..." 
docker run --name prometheus --net="host" -d -v $(pwd)/target/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
