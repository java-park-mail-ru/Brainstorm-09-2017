FROM ubuntu:16.04
MAINTAINER Alexander Kiryanenko <kiryanenkoav@gmail.com>

# Копируем исходный код в Docker-контейнер
ENV WORK /opt/Bubblerise
ADD ./ $WORK

RUN apt-get update

# Установка postgresql
RUN apt-get install -y postgresql
# Run the rest of the commands as the ``postgres`` user created by the ``postgres-$PGVER`` package when it was ``apt-get installed``
USER postgres
# Create a PostgreSQL role named ``docker`` with ``docker`` as the password and
# then create a database `docker` owned by the ``docker`` role.
RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER bubblerise WITH SUPERUSER PASSWORD '123456';" &&\
    createdb -E utf8 -T template0 -O bubblerise bubblerise &&\
    /etc/init.d/postgresql stop

# Expose the PostgreSQL port
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

# Back to the root user
USER root

# Установка JDK
RUN apt-get install -y openjdk-8-jdk-headless
RUN apt-get install -y maven

# Собираем и устанавливаем пакет
WORKDIR $WORK
RUN service postgresql start && mvn package

# Make ports available to the world outside this container
EXPOSE 8080

# run the application
CMD service postgresql start && java -Dserver.port=8080 -Xmx300M -Xmx300M -jar target/*.jar
