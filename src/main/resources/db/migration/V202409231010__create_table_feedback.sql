CREATE TABLE feedback
(
    id SERIAL PRIMARY KEY ,
    company_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL ,
    content TEXT NOT NULL,
    is_anonymous BOOLEAN ,
    time_submitted DATE DEFAULT CURRENT_DATE ,
    status VARCHAR(255) NOT NULL,
    last_modified_status DATE
);


