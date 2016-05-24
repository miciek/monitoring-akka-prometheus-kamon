# Monitoring Akka Applications with Kamon

This project serves as a playground for trying out monitoring options for both single noded and clustered (sharded) Akka applications.
 
The application itself is a simulation of [Conveyor Sorting Subsystem](http://i.imgur.com/mctb4HC.gifv) and its original code (without monitoring and using older Akka version) can be found in [akka-sharding-example repository](https://github.com/miciek/akka-sharding-example). 

## Running Prometheus
This project uses [Prometheus](https://prometheus.io/) as monitoring solution. You can use [runPrometheus.sh](runPrometheus.sh) script that will run Prometheus Docker container with proper configuration.
After running the script you should be able to access Prometheus instance at `<DOCKER MACHINE IP>:9090`. Prometheus will poll for metrics, but we need to run the app and generate HTTP requests first.

## Generating traffic
We can generate lots of HTTP requests using these tools:

- `ab` - Apache HTTP server benchmarking tool,
- `parallel` - GNU Parallel - The Command-Line Power Tool,
- `haproxy` - fast and reliable http reverse proxy and load balancer,
- provided [URLs.txt](src/main/resources/URLs.txt) for single-noded app,
- provided [shardedURLs.txt](src/main/resources/shardedURLs.txt) and [haproxy.conf](src/main/resources/haproxy.conf).

### SingleNodedApp
Just run `SingleNodedApp` from your IDE or `sbt runSingle` and then:

```
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

### ShardedApp (2 nodes)
You can run two nodes by executing:

- first node: `sbt runSharded`
- second node: `sbt '; set javaOptions += "-Dclustering.port=2552" ; set javaOptions += "-Dapplication.exposed-port=8081" ; set javaOptions += "-Dmetrics.enabled=false" ; runSharded'`

We want to balance the traffic between the two nodes. Simple configuration for haproxy daemon can be found in resources dir. Run it with:
`haproxy -f src/main/resources/haproxy.conf`

This will set up a round-robing load balancer with frontend on port `8000` and backends on `8080` and `8081`. You can then use different `shardedURLs.txt` file:

```
cat src/main/resources/shardedURLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

## Analysing results
TODO
