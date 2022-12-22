-- -----------------------------------------------------
-- ---------------- Solution for Q6 --------------------
-- -----------------------------------------------------
 SELECT EMPLOYEE_ID FROM employees
 WHERE EMPLOYEE_ID IN (
    SELECT MANAGER_ID FROM departments
    WHERE MANAGER_ID NOT IN(
        SELECT MANAGER_ID FROM employees
        )
    ) 
