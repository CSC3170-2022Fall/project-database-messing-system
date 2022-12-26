load employees;
create table out as
select DEPARTMENT_ID 'Department Name',count EMPLOYEE_ID 'Number of Employees'
from employees group by DEPARTMENT_ID;
store out out;