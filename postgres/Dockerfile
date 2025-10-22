FROM postgres:18.0-alpine3.22

COPY schema.sql /docker-entrypoint-initdb.d/

COPY test_data.sql /docker-entrypoint-initdb.d/
