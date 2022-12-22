-- -----------------------------------------------------
-- --------------- Solution for Q11 --------------------
-- -----------------------------------------------------
SELECT FIRST_NAME, LAST_NAME FROM employees
WHERE MANAGER_ID IN(
    SELECT EMPLOYEE_ID FROM employees
    WHERE DEPARTMENT_ID IN (
        SELECT DEPARTMENT_ID FROM departments 
        WHERE LOCATION_ID IN (
            SELECT LOCATION_ID FROM locations 
            WHERE COUNTRY_ID = 'US'
        )
    )
)