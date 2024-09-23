CREATE TABLE employee
(
    id SERIAL PRIMARY KEY ,
    company_id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL ,
    last_name VARCHAR(255) NOT NULL ,
    role VARCHAR(255) NOT NULL ,
    department VARCHAR(255) NOT NULL
);


