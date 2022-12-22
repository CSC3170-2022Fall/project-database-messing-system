-- -----------------------------------------------------
-- --------------- Solution for Q13 --------------------
-- -----------------------------------------------------

SELECT DISTINCT EMPLOYEE_ID, SALARY
FROM employees e1 
WHERE 4 = (SELECT COUNT(DISTINCT SALARY) 
FROM employees  e2 
WHERE e2.SALARY <= e1.SALARY);