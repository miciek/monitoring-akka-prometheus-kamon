# Monitoring Akka Applications with Kamon

This project serves as a playground for trying out monitoring options for both single noded and clustered Akka applications.
 
The application itself is a simulation of [Conveyor Sorting Subsystem](http://i.imgur.com/mctb4HC.gifv) and its original code (without monitoring and using older Akka version) can be found in [akka-sharding-example repository](https://github.com/miciek/akka-sharding-example). 

## Benchmarking
You can test both applications on your local machine by using included `resources/URLs.txt` file and the following command:

### SingleNodedApp

Just run `SingleNodedApp` from your IDE and then:

```
cat src/main/resources/URLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

This command uses:
- `ab` - Apache HTTP server benchmarking tool
- `parallel` - GNU Parallel - The Command-Line Power Tool

### ShardedApp
First build the application:

```mvn clean package```

This will build `target/SortingDecider-1.0-SNAPSHOT-uber.jar`. You will be able to run two nodes by overriding default values:
- first node: `java -jar target/SortingDecider-1.0-SNAPSHOT-uber.jar`
- second node: `java -Dclustering.port=2552 -Dapplication.exposed-port=8081 -jar target/SortingDecider-1.0-SNAPSHOT-uber.jar`

For benchmarking sharded application you need to use haproxy. Simple configuration for haproxy daemon can be found in resources dir. Run it with:
```haproxy -f src/main/resources/haproxy.conf````

This will set up a round-robing load balancer with frontend on port `8000` and backends on `8080` and `8081`. You can then use different `shardedURLs.txt` file:

```
cat src/main/resources/shardedURLs.txt | parallel -j 5 'ab -ql -n 2000 -c 1 -k {}' | grep 'Requests per second'
```

