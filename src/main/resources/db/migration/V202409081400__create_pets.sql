
CREATE TABLE pets
(
    id SERIAL PRIMARY KEY,
    name varchar(255),
    type varchar,
    date_of_arrival date,
    company_id UUID
);


CREATE INDEX idx_company ON pets (company_id);

/*

insert into pets(name , type , date_of_arrival , company_id)
values ('TimTam', 'Cat', '2017-01-01','6dbd20a1-e09a-474c-94dc-038119146a30');

insert into pets(name , type , date_of_arrival , company_id)
values ('Dogi', 'Dog', '2023-01-01','f1c34a5a-d43d-4722-83e3-703b0584c0cc');



select * from pets where type = 'Dog';

delete
from pets
where id = 1;

select * from pets where date_of_arrival <  '2023-09-07'
 */