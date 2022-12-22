-- -----------------------------------------------------
-- --------------- Solution for Q15 --------------------
-- -----------------------------------------------------

SELECT departments.DEPARTMENT_ID AS 'Department Name', COUNT(employees.EMPLOYEE_ID) AS 'Number of Employees' 
FROM departments JOIN employees ON(departments.DEPARTMENT_ID = employees.DEPARTMENT_ID)
GROUP BY departments.DEPARTMENT_ID

