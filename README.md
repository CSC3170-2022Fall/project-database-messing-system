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
| 121090001  | 安子航 🚩    | 2284874018@qq.com         | @ i-cookie         |
| 121090184  | 侯天赐       | enderturtle@foxmail.com    | @ EnderturtleOrz   |
| 121020163  | 沈驰皓       | stevenshen3641@outlook.com | @ StevenShen3641   |
| 121090519  | 涂喻钊       | 121090519@link.cuhk.edu.cn | @ tyzzzzzzzzz      |
| 121090628  | 夏禹扬       | 2467925095@qq.com          | @ xqbf             |
| 121090841  | 郑莹琪       | 121090841@link.cuhk.edu.cn | @ Aurora121090841  |

## Project Specification

<!-- You should remove the terms/sentence that is not necessary considering your option/branch/difficulty choice -->

After thorough discussion, our team made the choice and the specification information is listed below:

- Our option choice is: **Option 3**

## Project Abstract

<!-- TODO -->
This project involves writing a miniature relational database management system (DBMS) that stores tables of data, where a table consists of some number of labeled columns of information. Our system will include a very simple query language for extracting information from these tables. For the purposes of this project, we will deal only with very small databases, and therefore will not consider too much about speed and efficiency. But we will still consider part of the efficiency improvement when designing the DBMS. Here's what we implemented in this system:

Basic coding:

- Filling the code templates provided by UCB.

Advance coding:

- Take **data type** into consideration while creating the table;

- The syntax like **“A.x = B.x”** is supported;

- Implement the operations including **inner join, left join, right join, full join**;

- Implement functions include **max(), min(), mid(), having, as, between, where, in, order by,exist**;
	
- Application:**re-implement Assignmnet 2**;

- Using **snapshot** to support **version** and **rollback**.
