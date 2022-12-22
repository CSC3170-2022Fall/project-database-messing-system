load employees;
create table out as
select EMPLOYEE _ID, SALARY from employees order by SALARY;
