-- -----------------------------------------------------
-- --------------- Solution for Q16 --------------------
-- -----------------------------------------------------
SELECT departments.DEPARTMENT_ID, departments.DEPARTMENT_NAME, employees.FIRST_NAME 
FROM (departments JOIN employees ON (departments.MANAGER_ID = employees.EMPLOYEE_ID ))

