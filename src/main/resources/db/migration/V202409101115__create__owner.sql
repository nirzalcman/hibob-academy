create table owner
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    company_id BIGINT NOT NULL ,
    employee_id VARCHAR NOT NULL
);


CREATE INDEX idx_owner_company_employee ON owner (company_id, employee_id);