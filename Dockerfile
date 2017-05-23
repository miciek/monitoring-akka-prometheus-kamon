FROM ubuntu:16.04
RUN mkdir /app
RUN apt-get update \
    && apt-get install -y openjdk-8-jdk \
    && apt-get install -y openjdk-8-dbg \
    && apt-get install -y cmake build-essential \
    && apt-get install -y linux-tools-common linux-tools-generic linux-tools-common
RUN apt-get install -y git \
    && git clone --depth=1 https://github.com/brendangregg/FlameGraph \
    && git clone --depth=1 https://github.com/jrudolph/perf-map-agent
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV JAVA_OPTS "-Xmx1024m -XX:+PreserveFramePointer"
RUN cd perf-map-agent \
    && export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 \
    && cmake . \
    && make

EXPOSE 8080
ADD target/scala-2.11/monitoring-akka-prometheus-kamon-assembly-1.0-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "/app/monitoring-akka-prometheus-kamon-assembly-1.0-SNAPSHOT.jar"]
