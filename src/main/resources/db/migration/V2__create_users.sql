CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    company_email VARCHAR(255),
    address VARCHAR(255),
    transport_mode VARCHAR(255),
    points INT NOT NULL DEFAULT 0,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    phone VARCHAR(20) NOT NULL
);

