-- -----------------------------------------------------
-- ---------------- Solution for Q1 --------------------
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Meta Data Settings: Leave These Unchanged
-- -----------------------------------------------------
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Drop Schema `as2`: Leave It Unchanged
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `as2`;

-- -----------------------------------------------------
-- Create Schema `as2`: Leave It Unchanged
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `as2` DEFAULT CHARACTER SET utf8;
USE `as2`;

-- -----------------------------------------------------
-- Create Table `regions` here

CREATE TABLE  regions(
    REGION_ID       INT(5),
    REGION_NAME     VARCHAR(25)     NOT NULL,
    PRIMARY KEY (REGION_ID)
);
-- -----------------------------------------------------


-- -----------------------------------------------------
-- Create Table `countries` here
-- -----------------------------------------------------

 
CREATE TABLE countries (
  COUNTRY_ID        CHAR(2),
  COUNTRY_NAME      VARCHAR(30)    NOT NULL,
  REGION_ID         INT (5)        NOT NULL,
  PRIMARY KEY (COUNTRY_ID)
) ;


-- -----------------------------------------------------
-- Create Table `locations` here
-- -----------------------------------------------------
CREATE TABLE locations(
    LOCATION_ID     INT(4),
    STREET_ADDRESS  VARCHAR(40),
    POSTAL_CODE     VARCHAR(12),
    CITY            VARCHAR(30)    NOT NULL,
    STATE_PROVINCE  VARCHAR(25),
    COUNTRY_ID      CHAR(2)        NOT NULL,
    PRIMARY KEY(LOCATION_ID)
);


-- -----------------------------------------------------
-- Create Table `jobs` here
-- -----------------------------------------------------
CREATE TABLE If NOT EXISTS jobs(
    JOB_ID        VARCHAR(10),
    JOB_TITLE     VARCHAR(35)   NOT NULL,
    MIN_SALARY    INT(6),
    MAX_SALARY    INT(6),
    PRIMARY KEY (JOB_ID)
);


-- -----------------------------------------------------
-- Create Table `employees` here
-- -----------------------------------------------------
CREATE TABLE If NOT EXISTS employees(
    EMPLOYEE_ID       INT(6),
    FIRST_NAME        VARCHAR(20),
    LAST_NAME         VARCHAR(25)   NOT NULL,
    EMAIL             VARCHAR(20)   NOT NULL,
    PHONE_NUMBER      VARCHAR(20)   NOT NULL,
    HIRE_DATE         DATE,
    JOB_ID            CHAR(10),
    SALARY            FLOAT(10,2),
    COMMISSION_PCT    DECIMAL(4,2),
    MANAGER_ID        INT(6)        NOT NULL,
    DEPARTMENT_ID     INT(4)        NOT NULL,
    PRIMARY KEY(EMPLOYEE_ID)
);


-- -----------------------------------------------------
-- Create Table `departments` here
-- -----------------------------------------------------
CREATE TABLE If NOT EXISTS departments(
    DEPARTMENT_ID       INT(4), 
    DEPARTMENT_NAME     VARCHAR(20)     NOT NULL,
    MANAGER_ID          INT(6),
    LOCATION_ID         INT(4)          NOT NULL,
    PRIMARY KEY (DEPARTMENT_ID)
);


-- -----------------------------------------------------
-- Create Table `job_history` here
-- -----------------------------------------------------
CREATE TABLE If NOT EXISTS job_history(
    EMPLOYEE_ID       INT(6),
    START_DATE        DATE,
    END_DATE          DATE          NOT NULL,
    JOB_ID            VARCHAR(10),
    DEPARTMENT_ID     INT(4),
    PRIMARY KEY(EMPLOYEE_ID, START_DATE)
);


-- -----------------------------------------------------
-- Recover Meta Data: Leave These Unchanged
-- -----------------------------------------------------
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
