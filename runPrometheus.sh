#!/bin/sh
app_host=$(ifconfig | grep '\<inet\>' | cut -d ' ' -f2 | grep -v '127.0.0.1')
app_port=8888

cp src/main/resources/prometheus.yml target/
sed -i '' "s/HOST/$app_host/; s/PORT/$app_port/" target/prometheus.yml

echo "Killing prometheus container (if it's running)"
docker rm -f prometheus &> /dev/null
echo "Starting prometheus container on port 9090..." 
docker run --name prometheus -p 9090:9090 --add-host app:$app_host -d -v $(pwd)/target/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
