FROM mysql
ENV MYSQL_DATABASE b2b
ENV MYSQL_ROOT_PASSWORD admin1234
ENV MYSQL_USER admin
ENV MYSQL_PASSWORD pass
COPY ./scripts/ /docker-entrypoint-initdb.d/
