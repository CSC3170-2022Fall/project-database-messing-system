# TO-DO list

## Basic Code

### CommandInterpreter.java

- [ ] loadStatement

- [ ] storeStatement

- [ ] printStatement

- [ ] selectStatement

- [ ] tableDefinition  
	- [x] if part
	- [ ] else part

- [ ] selectClause

- [ ] conditionClause

- [ ] condition

### Condition.java

- [ ] boolean test(Row... rows)

### Database.java

- [x] Database()

- [x] put

- [ ] get

### Row.java

- [ ] size

- [ ] get

- [ ] equals

### Table.java

- [x] Table()

- [x] columns

- [x] getTitle

- [ ] findColumn

- [ ] size

- [ ] add

- [ ] readTable

- [ ] writeTable

- [ ] print

- [ ] Table select(`List<String> columnNames, List<Condition> conditions`)

- [ ] Table select(`Table table2, List<String> columnNames, List<Condition> conditions`)

- [ ] equijoin

## Additional Test

### Duplicate

- [x] table name

- [x] table column title

### Syntax Error

- [x] Bracket (create table)


