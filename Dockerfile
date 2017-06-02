FROM openjdk:8-jre-alpine
EXPOSE 8080
ADD target/scala-2.11/monitoring-akka-prometheus-kamon-assembly-1.0-SNAPSHOT.jar /app.jar
ADD lib_managed/jars/org.aspectj/aspectjweaver/aspectjweaver-1.8.10.jar /aspectjweaver.jar
ENTRYPOINT ["java", "-javaagent:/aspectjweaver.jar", "-jar", "/app.jar"]
