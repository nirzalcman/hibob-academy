
CREATE TABLE pet
(
    id SERIAL PRIMARY KEY,
    name varchar(255) NOT NULL ,
    type varchar NOT NULL ,
    date_of_arrival date DEFAULT CURRENT_DATE,
    company_id BIGINT NOT NULL
);

CREATE INDEX idx_pets_company_id ON pet (company_id);

/*
 insert into pet(name , type , date_of_arrival , company_id)
values ('TimTam', 'Cat', '2017-01-01',3);

insert into pet(name , type , date_of_arrival , company_id)
values ('Dogi', 'Dog', '2023-01-01',2);



select * from pet where type = 'Dog';

delete
from pet
where id = 1;

select * from pet where date_of_arrival <  CURRENT_DATE - INTERVAL '1 YEAR'
 */