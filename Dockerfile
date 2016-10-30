FROM hseeberger/scala-sbt
RUN mkdir /app
WORKDIR /app
ADD . /app
RUN sbt compile
