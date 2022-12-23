load employees;
create table tmp as select DEPARTMENT_ID, count EMPLOYEE_ID 'NUMBER' from employees
group by DEPARTMENT_ID;
create table out as
select DEPARTMENT_ID, avg SALARY from employees, tmp
where NUMBER > '10'
group by DEPARTMENT_ID;
store out out;