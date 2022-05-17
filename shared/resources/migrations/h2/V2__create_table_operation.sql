CREATE TABLE operation (
	operation_id serial PRIMARY KEY,
	account VARCHAR(255) NOT NULL,
	protocol VARCHAR(255) NOT NULL,
	type VARCHAR(255) NOT NULL,
	`value` DOUBLE NOT NULL,
	status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);