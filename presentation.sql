create table students(stu_id string, gender string, luckyNumber int);

insert into students 
values('121090184', 'M', '66'), ('121090001', 'M', '59'), ('121020163', 'M', '60'), ('121090841', 'F', 'X');

load team;

create table intersect as
select name, luckyNumber from students, team
where luckyNumber > '59';

select gender, count name 'people of the gender'
from team, students group by gender;

select name, round score divided_by 3 reserve 2 'scores'
from team order by 'scores' desc;