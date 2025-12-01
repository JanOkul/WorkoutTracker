FROM postgres:18.0-alpine3.22

COPY schema.sql /docker-entrypoint-initdb.d/01-schema.sql
COPY init-user.sh /docker-entrypoint-initdb.d/02-init-user.sh
COPY exercises.csv /docker-entrypoint-initdb.d/exercises.csv
COPY load_exercises.sh /docker-entrypoint-initdb.d/03-load_exercises.sh

RUN chmod +x /docker-entrypoint-initdb.d/02-init-user.sh
RUN chmod +x /docker-entrypoint-initdb.d/03-load_exercises.sh
