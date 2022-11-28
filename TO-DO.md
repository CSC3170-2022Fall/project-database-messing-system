# TO-DO list

## Basic Code

### CommandInterpreter.java

- [x] loadStatement-**XYY**

- [ ] storeStatement

- [ ] printStatement

- [ ] selectStatement

- [ ] tableDefinition  
	- [x] if part-**HTC**
	- [x] else part-**AZH**

- [ ] selectClause

- [ ] conditionClause

- [ ] condition

### Condition.java

- [ ] boolean test(Row... rows)

### Database.java

- [x] Database()-**HTC**

- [x] put-**HTC**

- [ ] get

### Row.java

- [x] size-**AZH**

- [x] get-**AZH**

- [ ] equals

### Table.java

- [x] Table()-**HTC**

- [x] columns-**HTC**

- [x] getTitle-**HTC**

- [x] findColumn-**AZH**

- [x] size-**AZH**

- [x] add-**HTC** **BUGS: equal(row) is true and with same hashCode but hashSet thinks they are different.**

- [x] readTable-**XYY**

- [ ] writeTable

- [x] print-**XYY** **HTC**

- [ ] Table select(`List<String> columnNames, List<Condition> conditions`)

- [ ] Table select(`Table table2, List<String> columnNames, List<Condition> conditions`)

- [ ] equijoin

## Additional Test

### Duplicate

- [x] table name-**HTC**

- [x] table column title-**HTC**

- [ ] insert values-**HTC** **Failed**

### Syntax Error

- [x] Bracket (create table)-**HTC**

## Advance

### Operation

- [x] insert rows with mulit columns-**HTC**

### Beautify

- [x] print (MySQL style)-**HTC**


