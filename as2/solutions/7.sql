load employees;
create table out as
select EMPLOYEE_ID, PHONE_NUMBER from employees
where DEPARTMENT_ID not in
select DEPARTMENT_ID from employees
where DEPARTMENT_ID='20' and DEPARTMENT_ID !='100';
store out out;