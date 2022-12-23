load employees;
load departments;
load locations;
create table tep as
select DEPARTMENT_ID, DEPARTMENT_NAME from departments
where DEPARTMENT_ID in
select DEPARTMENT_ID from departments
where LOCATION_ID in
select LOCATION_ID from locations
where CITY ='Seattle';
create table out as
select EMPLOYEE_ID, JOB_ID, DEPARTMENT_ID, DEPARTMENT_NAME
from employees, tep;
store out out;