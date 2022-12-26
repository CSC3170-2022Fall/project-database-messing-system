load employees;
create table out as
select FIRST_NAME 'First Name', LAST_NAME 'Last Name'
from employees;
store out out;