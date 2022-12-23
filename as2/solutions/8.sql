load employees;
create table out as
select FIRST_NAME from employees 
where FIRST_NAME like '_a%';
store out out;