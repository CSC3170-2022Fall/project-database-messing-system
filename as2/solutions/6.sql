load departments;
load employees;
create table out as
select MANAGER_ID 'EMPLOYEE ID' from departments
where MANAGER_ID !='NULL' 
and MANAGER_ID not in
select MANAGER_ID from employees;
store out out;