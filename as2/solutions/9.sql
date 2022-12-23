load employees;
load jobs;
create table out as
select JOB_ID, count EMPLOYEE_ID from employees, jobs 
group by JOB_ID;
store out out;