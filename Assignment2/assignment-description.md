# CSC3170 Assignment 2 (Modified for Better Understanding)

1. Consider a database schema that stores the following information and create relevant tables:

   * `employees`: This is a table that contains the basic information of any employee:
     * `EMPLOYEE_ID`: The employee's identifier that determines other attributes. $\text{A 6-digit integer}$.
     * `FIRST_NAME`: The first name of an employee (might be empty with only one word for the name information, where the name will be filled in the last name attribute). **At most 20 characters.**
     * `LAST_NAME`: The last name of an employee. It must not be empty. **At most 25 characters.**
     * `EMAIL`: The email of an employee. It must not be empty as a work email will be assigned (and the suffix can be omitted).
     * `PHONE_NUMBER`: The phone number of an employee. Might be empty. **At most 20 characters, and character '.' is allowed for a phone number.**
     * `HIRE_DATE`: The date when the employee was hired for the first time.
     * `JOB_ID`: The current job id of an employee. It should be of the same format as that of `jobs.JOB_ID`. **One must have a current job information. A 10-character string.**
     * `SALARY`: The real annual salary of the employee. Normally should be guided by the minimum and maximum salary information described in the current job description, while some special cases that exceed the limit are allowed. **A float number with at most 8 digits to the left of the decimal point, and 2 to the right of it** (If not pointed out specifically, the "salary" refers to "annual salary").
     * `COMMISSION_PCT`: The commission percentage. A float number from 0 to 100 with 2 digits to the right of the decimal point. It's the attribute usually not used, and the typical value should be 0.00 here.
     * `MANAGER_ID`: Every employee should have a manager that guide him or her in the real work. However, an manager might not have any staff. The attribute should have the same format as the `EMPLOYEE_ID`.
     * `DEPARTMENT_ID`: Describes the department that the employee belongs to. Any employee should register his or her department, while some virtual department might not have any employee registered.
   * `jobs`: This table describes the jobs an employee has currently or in the past.
     * `JOB_ID`: The job identifier that determines other attributes of a job. Use a string no more than 10 characters.
     * `JOB_TITLE`: The string that describes the title of a job. No more than 35 characters and every job should have a title.
     * `MIN_SALARY`: The minimum salary that a specific job could have. Not in high precision and described with a 6-digit integer. It might be empty.
     * `MAX_SALARY`: The maximum salary that a specific job could have. Not in high precision and described with a 6-digit integer. It might be empty.
   * `departments`: The departments of the company.
     * `DEPARTMENT_ID`: The department identifier that determine the other attributes.
     * `DEPARTMENT_NAME`: An attribute that should not be empty. The name of the department.
     * `MANAGER_ID`: Describes the manager of this department. Note that it could be different from the manager of an employee, i.e. the manager of some employee might be or might not be the manager of some department. The format of this attribute should be the same as that of `employees.EMPLOYEE_ID`. A department might not have its manager. If a department has its manager, it should have exactly one manager.
     * `LOCATION_ID`: Help to find the location of the department. Every department should have a location. The format of this attribute is the same as that of `locations.LOCATION_ID`.
   * `locations`: This table describes the location of a department.
     * `LOCATION_ID`: The location identifier that determines other attributes. An integer with 4 digits.
     * `STREET_ADDRESS`: A string that is no more than 40 characters for street address. Might be empty.
     * `POSTAL_CODE`: A string that is no more than 12 characters for postal code. Might be empty.
     * `CITY`: Can not be empty. A string within 30 characters that describe the city of the location information.
     * `STATE_PROVINCE`: Can be empty. A string within 25 characters that describe the state or province of the location information.
     * `COUNTRY_ID`: An attribute having the same format as `countries.COUNTRY_ID` that can not be empty.
   * `countries`: The country that some address (location) locates in.
     * `COUNTRY_ID`: A string with only and exactly two characters that identifies some country.
     * `COUNTRY_NAME`: Should not be empty. The name of some country.
     * `REGION_ID`: Should not be empty. Have the same format as `regions.REGION_ID`.
   * `regions`: The region of some country.
     * `REGION_ID`: A 5-digit integer that identifies a region.
     * `REGION_NAME`: A string no more than 25 characters, which can not be empty.
   * `job_history`: An employee might have multiple job history records, while every job history should belong to only one employee.
     * `EMPLOYEE_ID`: The attribute having the same format as `employees.EMPLOYEE_ID`.
     * `START_DATE`: The start date of a job history record. Determines other attributes together with `EMPLOYEE_ID` in this table.
     * `END_DATE`: The end date of a job history record. Should not be empty.
     * `JOB_ID`: Describe the job type of a job history record. Have the same format as `jobs.JOB_ID`.
     * `DEPARTMENT_ID`: Help to describe the department information of a job history record. Have the same format as `departments.DEPARTMENT_ID`.

2. Show the first_name and last_name of all employees using alias name "First Name", "Last Name".

3. Write a query to show the employee_id and the salary of all employees in ascending order of salary.

4. Write a query to calculate the maximum and minimum salary of all employees.

5. Show the employee_id and the monthly salary (round 2 decimal places) of all employees.

6. Show the employee(s) that is the manager of some department, but doesn't manages any other employees.

7. Write a query that selects employee_id, and phone_number of those employees who are in departments 20 or 100. The results should be descendingly ordered by the department_id.

8. Write a query that selects the first_name of employees having 'a' as the second character.

9. Write a query that get the number of employees who have the same job. The results should contain job_id and its number of employees.

10. Write a query to calculate the average salary for all departments that have over 10 employees. The results should contain department_id, the corresponding average salary, and the number of employees.

11. Write a query to get the first_name and last_name of the employees who have a manager that is currently working in a USA based department.

12. Write a query to get the employee_id and salary of the employees whose salary is larger than the average salary of all departments.

13. Write a query to find the 4th lowest salary of employees. The results should also contain employee_id and salary.

14. Write a query to find the "employee_id", "job", "department’s id and name" of the employees working in Seattle.

15. Write a query to get the department’s id and the number of employees in the department. The results should contain two keys: "Department Name" and "Number of Employees" (Hint: here we adopt the "weird renaming" here intentionally, and that there could be records in the table `employees` that point to some deprecated department so we should stick to those existed departments in the table `departments`).

16. Write a query to get the department_id, department_name, and manager's first_name for departments.
