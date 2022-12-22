-- -----------------------------------------------------
-- --------------- Solution for Q10 --------------------
-- -----------------------------------------------------
SELECT DEPARTMENT_ID, AVG(SALARY), COUNT(*) FROM employees
GROUP BY DEPARTMENT_ID
HAVING COUNT(*)>10