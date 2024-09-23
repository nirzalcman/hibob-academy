CREATE TABLE feedback_response
(
    id SERIAL PRIMARY KEY ,
    company_id BIGINT NOT NULL,
    feedback_id BIGINT NOT NULL ,
    content TEXT NOT NULL,
    time_submitted DATE DEFAULT CURRENT_DATE ,
    response_by BIGINT
);


