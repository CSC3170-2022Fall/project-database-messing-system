# TO-DO list

## Basic Code

### CommandInterpreter.java

- [ ] loadStatement

- [ ] storeStatement

- [ ] printStatement

- [ ] selectStatement

- [ ] tableDefinition  
	- [x] if part-$HTC$
	- [x] else part-$AZH$

- [ ] selectClause

- [ ] conditionClause

- [ ] condition

### Condition.java

- [ ] boolean test(Row... rows)

### Database.java

- [x] Database()-$HTC$

- [x] put-$HTC$

- [ ] get

### Row.java

- [x] size-$AZH$

- [x] get-$AZH$

- [ ] equals

### Table.java

- [x] Table()-$HTC$

- [x] columns-$HTC$

- [x] getTitle-$HTC$

- [x] findColumn-$AZH$

- [x] size-$AZH$

- [ ] add

- [ ] readTable

- [ ] writeTable

- [ ] print

- [ ] Table select(`List<String> columnNames, List<Condition> conditions`)

- [ ] Table select(`Table table2, List<String> columnNames, List<Condition> conditions`)

- [ ] equijoin

## Additional Test

### Duplicate

- [x] table name-$HTC$

- [x] table column title-$HTC$

### Syntax Error

- [x] Bracket (create table)-$HTC$


