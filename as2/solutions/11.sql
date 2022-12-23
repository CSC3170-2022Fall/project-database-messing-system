load employees;
load departments;
load locations;
load countries;

create table out as
select FIRST_NAME, LAST_NAME from employees
where MANAGER_ID in
select EMPLOYEE_ID 'MANAGER_ID' from employees
where DEPARTMENT_ID in
select DEPARTMENT_ID from departments
where LOCATION_ID in
select LOCATION_ID from locations
where COUNTRY_ID in
select COUNTRY_ID from countries
where COUNTRY_NAME = '"United States of America"';
store out out;