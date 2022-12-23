load employees;
create table min1st as
select min SALARY 'min1st' from employees;
create table min2nd as
select min SALARY 'min2nd' from employees, min1st
where SALARY > min1st;
create table min3rd as
select min SALARY 'min3rd' from employees, min2nd
where SALARY > min2nd;
create table min4th as
select min SALARY 'min4th' from employees, min3rd
where SALARY > min3rd;
create table out as
select EMPLOYEE_ID, SALARY from employees, min4th
where SALARY = min4th;
store out out;
