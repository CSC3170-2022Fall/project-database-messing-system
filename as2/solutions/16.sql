load employees;
load departments;
create table tep1 as
select DEPARTMENT_ID, DEPARTMENT_NAME, MANAGER_ID from departments;
create table tep2 as
select EMPLOYEE_ID,FIRST_NAME from employees;
create table preout as
select DEPARTMENT_ID, DEPARTMENT_NAME, MANAGER_ID, EMPLOYEE_ID,FIRST_NAME from tep1, tep2
where MANAGER_ID = EMPLOYEE_ID;
create table out as 
select DEPARTMENT_ID, DEPARTMENT_NAME,FIRST_NAME from preout;
store out out;
