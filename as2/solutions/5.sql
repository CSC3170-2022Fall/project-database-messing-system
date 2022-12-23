load employees;
create table out as
select round SALARY divided_by 12 reserve 2 from employees;
store out out;