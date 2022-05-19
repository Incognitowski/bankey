CREATE TABLE operation_event (
	operation_event_id serial PRIMARY KEY,
	operation_id VARCHAR(255) NOT NULL,
	type VARCHAR(255) NOT NULL,
	payload VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);