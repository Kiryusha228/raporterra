CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'analyst', 'user')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER NOT NULL REFERENCES users(user_id)
);

CREATE TABLE user_groups (
    group_id INTEGER NOT NULL REFERENCES groups(group_id),
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    added_by INTEGER NOT NULL REFERENCES users(user_id),
    PRIMARY KEY (group_id, user_id)
);

CREATE TABLE database_connections (
    connection_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    db_type VARCHAR(50) NOT NULL,
    host VARCHAR(255) NOT NULL,
    port INTEGER NOT NULL,
    database_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    encrypted_password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER NOT NULL REFERENCES users(user_id),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE reports (
    report_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    sql_query TEXT NOT NULL,
    connection_id INTEGER REFERENCES database_connections(connection_id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER NOT NULL REFERENCES users(user_id),
    updated_at TIMESTAMP,
    updated_by INTEGER REFERENCES users(user_id),
    is_public BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE report_parameters (
    parameter_id SERIAL PRIMARY KEY,
    report_id INTEGER NOT NULL REFERENCES reports(report_id),
    name VARCHAR(100) NOT NULL,
    param_type VARCHAR(50) NOT NULL CHECK (param_type IN ('date', 'number', 'string', 'boolean')),
    default_value TEXT,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT unique_param_name_per_report UNIQUE (report_id, name)
);

CREATE TABLE collections (
    collection_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER NOT NULL REFERENCES users(user_id)
);

CREATE TABLE collection_reports (
    collection_id INTEGER NOT NULL REFERENCES collections(collection_id),
    report_id INTEGER NOT NULL REFERENCES reports(report_id),
    PRIMARY KEY (collection_id, report_id)
);

CREATE TABLE collection_access (
    access_id SERIAL PRIMARY KEY,
    collection_id INTEGER NOT NULL REFERENCES collections(collection_id),
    user_id INTEGER REFERENCES users(user_id),
    group_id INTEGER REFERENCES groups(group_id),
    access_level VARCHAR(20) NOT NULL,
    granted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    granted_by INTEGER NOT NULL REFERENCES users(user_id),
    CONSTRAINT check_user_or_group CHECK (
        (user_id IS NOT NULL AND group_id IS NULL) OR 
        (user_id IS NULL AND group_id IS NOT NULL)
    )
);

CREATE INDEX idx_user_groups_user_id ON user_groups(user_id);
CREATE INDEX idx_user_groups_group_id ON user_groups(group_id);
CREATE INDEX idx_reports_created_by ON reports(created_by);
CREATE INDEX idx_reports_connection_id ON reports(connection_id);
CREATE INDEX idx_report_parameters_report_id ON report_parameters(report_id);
CREATE INDEX idx_collection_reports_collection_id ON collection_reports(collection_id);
CREATE INDEX idx_collection_reports_report_id ON collection_reports(report_id);
CREATE INDEX idx_collection_access_collection_id ON collection_access(collection_id);
CREATE INDEX idx_collection_access_user_id ON collection_access(user_id);
CREATE INDEX idx_collection_access_group_id ON collection_access(group_id);