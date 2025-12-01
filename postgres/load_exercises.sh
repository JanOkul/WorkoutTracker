#!/bin/bash

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    \COPY exercises(id, description) FROM '/docker-entrypoint-initdb.d/exercises.csv' DELIMITER ',' CSV HEADER;
EOSQL

echo "CSV data imported successfully into the 'exercises' table."
