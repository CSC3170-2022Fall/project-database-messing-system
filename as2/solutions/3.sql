load employees;
create table out as
select EMPLOYEE_ID, SALARY from employees order by SALARY;
store out out;
