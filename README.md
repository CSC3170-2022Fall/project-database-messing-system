[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=9422691&assignment_repo_type=AssignmentRepo)
# CSC3170 Course Project

## Project Overall Description

This is our implementation for the course project of CSC3170, 2022 Fall, CUHK(SZ). For details of the project, you can refer to [project-description.md](project-description.md). In this project, we will utilize what we learned in the lectures and tutorials in the course, and implement either one of the following major job:

<!-- Please fill in "x" to replace the blank space between "[]" to tick the todo item; it's ticked on the first one by default. -->

- [ ] **Application with Database System(s)**
- [x] **Implementation of a Database System**

## Team Members

Our team consists of the following members, listed in the table below (the team leader is shown in the first row, and is marked with 🚩 behind his/her name):

<!-- change the info below to be the real case -->

| Student ID | Student Name | GitHub Account (in Email) | GitHub Username   |
| ---------- | ------------ | ------------------------- | ---------------- |
| 121090001  | 安子航 🚩    | 2284874018@qq.com         | [@i-cookie](https://github.com/i-cookie)         |
| 121090184  | 侯天赐       | enderturtle@foxmail.com    | [@EnderturtleOrz](https://github.com/EnderturtleOrz)   |
| 121020163  | 沈驰皓       | stevenshen3641@outlook.com | [@StevenShen3641](https://github.com/StevenShen3641)   |
| 121090519  | 涂喻钊       | 121090519@link.cuhk.edu.cn | [@tyzzzzzzzzz](https://github.com/tyzzzzzzzzz)      |
| 121090628  | 夏禹扬       | 2467925095@qq.com          | [@xqbf](https://github.com/xqbf)             |
| 121090841  | 郑莹琪       | 121090841@link.cuhk.edu.cn | [@Aurora121090841](https://github.com/Aurora121090841)  |

## Project Specification

<!-- You should remove the terms/sentence that is not necessary considering your option/branch/difficulty choice -->

After thorough discussion, our team made the choice and the specification information is listed below:

- Our option choice is: **Option 3**

## Project Abstract

<!-- TODO -->
This project writes a miniature relational database management system (DBMS) that stores data tables containing labeled information columns. The project consists of the language system and the version control system. In language system, we defined the data definition language (DDL) and data manipulation language (DML) and wrote the DDL interpreter and DML in the java language compiler to interpreting users' input and dealing with data in tables. The version control system is standard practice for maintaining a project and tracking it from inception to finalization. In addition, version control is a software engineering technique to ensure that the same program files edited by different people are synchronized during the software development process, which play an essential role in a such multi-person cooperative project. We will only deal with tiny databases for this project, so we will not  consider too much about speed and efficiency. But we will still consider part of the efficiency improvement when designing the DBMS. Here's what we implemented in this system:

Basic coding:

- Filling the code templates provided by UCB.

Advance coding:

- Take **data type (int/double/string)** into consideration while creating the table and doing other operations;

- Implement the operations including **commit, rollback**;

- Implement aggregate functions including **max(), min(), avg(), sum(), round(), count()**;

- Implement additional keyword including **as, like, between, where (not) in, order by, group by, primary key**;

- Version Control: Use **snapshot** strategy with **SHA-1** as version name and **trie** as version tree;
	
- Application: **re-implement Assignment 2**.

## Database Structure

All the data are stored in the rows of each table. Rows are stored based on hashsets in tables, and tables are stored based on hashmaps in databases. For each table, it contains information about the name and data type of each column, and the rows can be traversed using an iterator.

## Basic Syntax
- **create statement** ::= create table **name** **table definition**  ;
- **table definition** ::= ( **column name** +, ) | as **select clause**
- **print statement** ::= print **table name** ;
- **insert statement**::= insert into **table name** values **literal**+,; 
- **load statement** ::= load **name** ;
- **store statement** ::= **store file name (no suffix)** **table name** ;
- **exit statement** ::= quit ; | exit ;
- **select statement** ::= **select clause**;
- **select clause** ::= select **column name**+, from **tables** **condition clause**;
- **Operator in select clause**: =, <, <=, >, >=
	
## Advanced Syntax
- **Aggregated functions(avg, max, min, count, sum)** ::= select **function** **column name**+, from **table name**
- **Round function**::= select round **column name**  **operator**  **operand** reserve **number of bits reserved** from **table name**
- **in condition**::= select **column name**+, from **table name** where **column name** in **select clause**
- **order by condition**::= select **column name**+, from **table name** order by (asc/desc) **column name**+,
- **group by condition**::= select **column name**+, **function**  **column name**  from **table name** group by **column name**+,
- **between condition**::= select **column name**+, from  **table name**  where  **column name**  between  **operand A**  and **operand B**
- **like condition**::= select **column name**+, from **table name** where **column name** like **sample value**;
  (supported operator: ‘_’, ‘%’)

## Version Control Syntax
- **commit statement** ::= commit **table name**;
- **rollback to statement** ::= rollback **table name** to **literal**;
- **rollback at statement** ::= rollback **table name** at **literal**;

## Standard Error Messages

## Re-implement Assignment2
For the specific results, please refer to the pdf file “Presentation PPT.pdf" transformed by our presentation PPT.
