
CREATE TABLE pets
(
    id SERIAL PRIMARY KEY,
    name varchar(255) NOT NULL ,
    type varchar NOT NULL ,
    date_of_arrival date DEFAULT CURRENT_DATE,
    company_id UUID NOT NULL
);


CREATE INDEX idx_pets_company_id ON pets (company_id);


/*
insert into pets(name , type , date_of_arrival , company_id)
values ('TimTam', 'Cat', '2017-01-01','6dbd20a1-e09a-474c-94dc-038119146a30');

insert into pets(name , type , date_of_arrival , company_id)
values ('Dogi', 'Dog', '2023-01-01','f1c34a5a-d43d-4722-83e3-703b0584c0cc');



select * from pets where type = 'Dog';

delete
from pets
where id = 1;

select * from pets where date_of_arrival <  CURRENT_DATE - INTERVAL '1 YEAR'
*/
