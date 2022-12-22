-- -----------------------------------------------------
-- --------------- Solution for Q14 --------------------
-- -----------------------------------------------------

SELECT employees.EMPLOYEE_ID, employees.JOB_ID, employees.DEPARTMENT_ID, departments.DEPARTMENT_NAME 
FROM employees JOIN departments ON (departments.DEPARTMENT_ID = employees.DEPARTMENT_ID) 
WHERE departments.LOCATION_ID IN (
    SELECT LOCATION_ID FROM locations 
    WHERE CITY = 'Seattle'
)