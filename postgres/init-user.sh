#!/bin/bash
set -e

echo "Starting creation of highly restricted application user: $APP_USER"

psql -v ON_ERROR_STOP=1 \
    --username "$POSTGRES_USER" \
    --dbname "$POSTGRES_DB" <<-EOSQL

    -- 1. Create the application user with an encrypted password
    DO \$\$
    BEGIN
        IF NOT EXISTS (
            SELECT FROM pg_catalog.pg_roles WHERE rolname = '$APP_USER'
        ) THEN
            CREATE USER "$APP_USER" WITH ENCRYPTED PASSWORD '$APP_PASSWORD';
        END IF;
    END
    \$\$;

    -- 2. Grant basic connection permissions
    GRANT CONNECT ON DATABASE "$POSTGRES_DB" TO "$APP_USER";
    GRANT USAGE ON SCHEMA public TO "$APP_USER";
    
    -- 3. Grant core R/W/D permissions on existing tables
    GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO "$APP_USER";
    
    -- 4. Grant permissions on existing sequences
    GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO "$APP_USER";
    
    -- 5. Default privileges for objects created by the superuser
    ALTER DEFAULT PRIVILEGES FOR ROLE "$POSTGRES_USER" IN SCHEMA public 
        GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "$APP_USER";
        
    ALTER DEFAULT PRIVILEGES FOR ROLE "$POSTGRES_USER" IN SCHEMA public 
        GRANT USAGE, SELECT ON SEQUENCES TO "$APP_USER";

    -- 6. Default privileges for objects created by the app user
    ALTER DEFAULT PRIVILEGES FOR ROLE "$APP_USER" IN SCHEMA public
        GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO "$APP_USER";
        
    ALTER DEFAULT PRIVILEGES FOR ROLE "$APP_USER" IN SCHEMA public
        GRANT USAGE, SELECT ON SEQUENCES TO "$APP_USER";
        
EOSQL

echo "User $APP_USER created successfully with restricted privileges."

