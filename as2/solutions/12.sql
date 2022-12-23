load employees;
create table avgs as
select DEPARTMENT_ID, avg SALARY 'avg' from employees
group by DEPARTMENT_ID;
create table maxavg as
select max avg 'maxAvg' from avgs;
create table out as
select EMPLOYEE_ID, SALARY from employees, maxavg
where SALARY > maxAvg;
store out out;