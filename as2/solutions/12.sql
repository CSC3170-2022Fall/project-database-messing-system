-- -----------------------------------------------------
-- --------------- Solution for Q12 --------------------
-- -----------------------------------------------------
SELECT EMPLOYEE_ID, SALARY FROM employees
WHERE  SALARY > ALL(
    SELECT AVG(SALARY) FROM employees
    GROUP BY DEPARTMENT_ID
    )