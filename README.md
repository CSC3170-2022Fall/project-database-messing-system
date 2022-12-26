[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=9422691&assignment_repo_type=AssignmentRepo)

# CSC3170 Course Project

## Project Overall Description

This is our implementation for the course project of CSC3170, 2022 Fall, CUHK(SZ). For details of the project, you can refer to [project-description.md](project-description.md). In this project, we will utilize what we learned in the lectures and tutorials in the course, and implement either one of the following major job:

- [ ] **Application with Database System(s)**
- [x] **Implementation of a Database System**

## Team Members

Our team consists of the following members, listed in the table below (the team leader is shown in the first row, and is marked with 🚩 behind his/her name):

| Student ID | Student Name | GitHub Account (in Email) | GitHub Username   |
| ---------- | ------------ | ------------------------- | ---------------- |
| 121090001  | 安子航 🚩    | 2284874018@qq.com         | [@i-cookie](https://github.com/i-cookie)         |
| 121090184  | 侯天赐       | enderturtle@foxmail.com    | [@EnderturtleOrz](https://github.com/EnderturtleOrz)   |
| 121020163  | 沈驰皓       | stevenshen3641@outlook.com | [@StevenShen3641](https://github.com/StevenShen3641)   |
| 121090519  | 涂喻钊       | 121090519@link.cuhk.edu.cn | [@tyzzzzzzzzz](https://github.com/tyzzzzzzzzz)      |
| 121090628  | 夏禹扬       | 2467925095@qq.com          | [@xqbf](https://github.com/xqbf)             |
| 121090841  | 郑莹琪       | 121090841@link.cuhk.edu.cn | [@Aurora121090841](https://github.com/Aurora121090841)  |


## Project Specification

After thorough discussion, our team made the choice and the specification information is listed below:

- Our option choice is: **Option 3**

## Project Abstract


This project writes a miniature relational database management system (DBMS) that stores data tables containing labeled information columns. The project consists of the language system and the version control system. In language system, we defined the data definition language (DDL) and data manipulation language (DML) and wrote the DDL interpreter and DML in the java language compiler to interpreting users' input and dealing with data in tables. The version control system is standard practice for maintaining a project and tracking it from inception to finalization. In addition, version control is a software engineering technique to ensure that the same program files edited by different people are synchronized during the software development process, which play an essential role in a such multi-person cooperative project. We will only deal with tiny databases for this project, so we will not  consider too much about speed and efficiency. But we will still consider part of the efficiency improvement when designing the DBMS. Here's what we implemented in this system:

Basic coding:

- Filling the code templates provided by UCB.

Advance coding:

- Take **data type (int/double/string)** into consideration while creating the table and doing other operations;
- Asterisk symbol '*';
- Rename the columns;
- Implement the operations including **commit, rollback**;
- Implement aggregate functions including **max(), min(), avg(), sum(), round(), count()**;
- Implement additional keyword including **as, like, between, where (not) in, order by, group by, primary key**;
- Version Control: Use **snapshot** strategy with **SHA-1** as version name and **trie** as version tree;
- Application: **re-implement Assignment 2**.

## Set Up Instruction

### Prerequisites

- jdk >= 17

- Make >= 4.2.1

### Compile the Project

```bash
$ git clone https://github.com/CSC3170-2022Fall/project-database-messing-system.git
$ cd project-database-messing-system
$ make default
```

### Run the Project

```bash
$ java db61b.Main
```

### Custom Tests

This project is configured with test cases from CSC3170-2022Fall Assignment2.

The solution should be stored as `as2/solutions/x.sql` and the answer should be stored as `as2/answers/x.db`, where `x` is the number of the test cases.

A shell script `tester.sh` is used to judge the `out.db` (except for the test case 3 which needs **order by**) with standard answers. In other word, a sentence like `store <table> out` is always required in your solution file.

`tester.sh` will sort `out.db` and turn it into `out_sorted.db` first, and compare `out_sorted.db` with the standard answer. 

In total, `tester.sh` returns three states **Passed**, **Failed** and **Skipped**.

- **Passed**: Your output, after sorting, agrees with the answer. 
*(Note that for test case 3, no sorting will be done)*

- **Failed**: Your output is not consistent with answers after sorting.

- **Skipped**: Cannot find the solution file of this test case.

For **Failed** test points, `tester.sh` will provide output comparison reports and run logs.

If you need to configure more test cases, just change the loop termination condition in `tester.sh`.

#### Run Custom Tests

```bash
$ bash as2/tester.sh
```

#### GitHub Action Configuration

CI configuration starts with the basic environment (ubuntu or other OS with bash, Make, JDK 17), then run the command `bash as2/tester.sh`. 

The following is the CI configuration of this repository.

```yml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Compile the project
      run: make default
    - name: Run the assignment 2 test cases
      run: bash as2/tester.sh
```


## Database Structure

All the data are stored in the rows of each table. Rows are stored based on **HashSets** in tables, and tables are stored based on **HashMaps** in databases. For each table, it contains information about the name and **data type** of each column, and the rows can be traversed using an iterator.

Since the HashSets are unordered, the clause **order by 'xxx'** has no effect when the column **'xxx'** is not in the result table.

## Basic Syntax
- **create statement** ::= create table \<name> \<table definition>

- **table definition** ::= (\<column name>\<column data type><sup>+</sup>,); | as\<select clause>;

  Example: 

  ![](readme-related files\create.gif)

- **insert statement**::= insert into \<table name> values (\<literal><sup>+</sup>,)<sup>+</sup>,;

  Example: 

  ![](readme-related files\insert.gif)
- **print statement** ::= print \<table name>;

  Example: 

  ![](readme-related files\print.gif)

- **load statement** ::= load \<name>;

- **store statement** ::= store \<file name without extension> \<table name> ;

- **exit statement** ::= quit; | exit ;

- **select statement** ::= \<select clause>;

- **select clause** ::= select \<column name><sup>+</sup>, from \<table name> \<condition clause>;

- **Operator in select clause**: =, \<, \<=, >, >=
## Advanced Syntax

- **Asterisk symbol** ::= select * from \<table name>;

- **Rename columns** ::= select \<column name><sup>+</sup>, '\<another name>'<sup>+</sup>, from \<table name>;

  Example: 

  ![](readme-related files\rename.gif)

- **Aggregated functions (avg, max, min, count, sum) **::= select \<function> \<column name><sup>+</sup>, from \<table name>;

  Example: 

  ![](readme-related files\aggregate.gif)

- **select with round function **::= select round \<column name> \<operator> \<operand> reserve \<number of reserved bits> from \<table name>;

  Example:

  ![](readme-related files\round.gif)

- **select with in condition **::= select \<column name><sup>+</sup>, from \<table name> where \<column name> in \<select clause>;

- **select with order by** ::= select \<column name><sup>+</sup>, from \<table name> order by '\<column name>'<sup>+</sup>,\<order>;

- **select with group by** ::= select \<column name><sup>+</sup>, function \<column name> from \<table name> group by \<column name><sup>+</sup>,;

- **select with between condition** ::= select \<column name><sup>+</sup>, from \<table name> where \<column name> between \<lower bound> and \<upper bound>;

- **select with like condition** ::= select \<column name><sup>+</sup>, from \<table name> where \<column name> like \<pattern>;
  (supported operator: '_' and  '%')
  
  
  
  **Notes**: aggregate functions can be used with "**where**" conditions **only if** there is "**group by**" clause, in that case, only the last argument can be an aggregate function.
  
  "**in**" and "**not in**" can only be applied to the select clause with **one** table.

## Version Control Syntax
- **commit statement** ::= commit \<table name>;
- **rollback to statement** ::= rollback \<table name> to \<version code>;
- **rollback at statement** ::= rollback \<table name> at \<version code>;

## Standard Error Messages

| Error Message   | Explanation                                                  |
| :-------------: | :----------------------------------------------------------- |
| Syntax Error    | <li>Unrecognizable command keywords <li>Too many or too few arguments <li>Divide something by 0 <li>Unterminated literal or comment <li>Wrong usage of commands |
| Format Error    | <li>Wrong data type when inserting or comparing <li>Apply functions to unsupported type of data <li>Wrong data type of arguments for some commands <li>Not using correct utf-8 encoding <li>Invalid SHA-1 code |
| Value Mismatch | <li>Cannot find specified column or table or version or type <li>Index out of range of a container <li>Some lists should have identical length but actually don't. <li>Duplicate names |
| FileFormatError | <li>Unexpected end of input <li>No header or datatype in the .db file <li>Number of columns in a row does not equal that of the table. |
| FileNotFound | <li>Cannot find specified file |
| VersionNotFound | <li>Cannot find specified version <li>More than one table share the same name |

## Re-implement Assignment2
For the specific results, please refer to the pdf file “[**Presentation slides.pdf**](https://github.com/CSC3170-2022Fall/project-database-messing-system/blob/main/presentation-related%20files/Presentation%20slides.pdf)". For code and .db file used in presentation, you can check the "[presentation-related files](https://github.com/CSC3170-2022Fall/project-database-messing-system/tree/main/presentation-related%20files)" directory.

## Hyperlinks
We have posted the presentation video on bilibili: [2022FALL CSC3170 Group2 Database-Messing-System Final Presentation](https://www.bilibili.com/video/BV1dG4y1J7ys).

Presentation slides: [Presentation slides.pdf](https://github.com/CSC3170-2022Fall/project-database-messing-system/blob/main/presentation-related%20files/Presentation%20slides.pdf).

Besides this README.md, we have also set [TODO.md](https://github.com/CSC3170-2022Fall/project-database-messing-system/blob/main/TO-DO.md)
to roughly show the things we have done. 

