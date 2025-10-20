FROM postgres:18

COPY schema.sql /docker-entrypoint-initdb.d/

COPY test_data.sql /docker-entrypoint-initdb.d/
