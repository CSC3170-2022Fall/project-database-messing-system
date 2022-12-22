load employees;
create table out as
select max SALARY, min SALARY from employees;
store out out;