-- -----------------------------------------------------
-- ---------------- Solution for Q7 --------------------
-- -----------------------------------------------------
SELECT EMPLOYEE_ID, PHONE_NUMBER FROM employees
WHERE DEPARTMENT_ID = 20 OR DEPARTMENT_ID = 100
ORDER BY DEPARTMENT_ID DESC