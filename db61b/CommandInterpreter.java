// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;

import java.util.*;

import static db61b.Utils.*;

/** An object that reads and interprets a sequence of commands from an
 *  input source.
 *  @author */
class CommandInterpreter {

    /* STRATEGY.
     *
     *   This interpreter parses commands using a technique called
     * "recursive descent." The idea is simple: we convert the BNF grammar,
     * as given in the specification document, into a program.
     *
     * First, we break up the input into "tokens": strings that correspond
     * to the "base case" symbols used in the BNF grammar.  These are
     * keywords, such as "select" or "create"; punctuation and relation
     * symbols such as ";", ",", ">="; and other names (of columns or tables).
     * All whitespace and comments get discarded in this process, so that the
     * rest of the program can deal just with things mentioned in the BNF.
     * The class Tokenizer performs this breaking-up task, known as
     * "tokenizing" or "lexical analysis."
     *
     * The rest of the parser consists of a set of functions that call each
     * other (possibly recursively, although that isn't needed for this
     * particular grammar) to operate on the sequence of tokens, one function
     * for each BNF rule. Consider a rule such as
     *
     *    <create statement> ::= create table <table name> <table definition> ;
     *
     * We can treat this as a definition for a function named (say)
     * createStatement.  The purpose of this function is to consume the
     * tokens for one create statement from the remaining token sequence,
     * to perform the required actions, and to return the resulting value,
     * if any (a create statement has no value, just side-effects, but a
     * select clause is supposed to produce a table, according to the spec.)
     *
     * The body of createStatement is dictated by the right-hand side of the
     * rule.  For each token (like create), we check that the next item in
     * the token stream is "create" (and report an error otherwise), and then
     * advance to the next token.  For a metavariable, like <table definition>,
     * we consume the tokens for <table definition>, and do whatever is
     * appropriate with the resulting value.  We do so by calling the
     * tableDefinition function, which is constructed (as is createStatement)
     * to do exactly this.
     *
     * Thus, the body of createStatement would look like this (_input is
     * the sequence of tokens):
     *
     *    _input.next("create");
     *    _input.next("table");
     *    String name = name();
     *    Table table = tableDefinition();
     *    _input.next(";");
     *
     * plus other code that operates on name and table to perform the function
     * of the create statement.  The .next method of Tokenizer is set up to
     * throw an exception (DBException) if the next token does not match its
     * argument.  Thus, any syntax error will cause an exception, which your
     * program can catch to do error reporting.
     *
     * This leaves the issue of what to do with rules that have alternatives
     * (the "|" symbol in the BNF grammar).  Fortunately, our grammar has
     * been written with this problem in mind.  When there are multiple
     * alternatives, you can always tell which to pick based on the next
     * unconsumed token.  For example, <table definition> has two alternative
     * right-hand sides, one of which starts with "(", and one with "as".
     * So all you have to do is test:
     *
     *     if (_input.nextIs("(")) {
     *         _input.next("(");
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * As a convenience, you can also write this as
     *
     *     if (_input.nextIf("(")) {
     *         // code to process "<column name>,  )"
     *     } else {
     *         // code to process "as <select clause>"
     *     }
     *
     * combining the calls to .nextIs and .next.
     *
     * You can handle the list of <column name>s in the preceding in a number
     * of ways, but personally, I suggest a simple loop:
     *
     *     ... = columnName();
     *     while (_input.nextIs(",")) {
     *         _input.next(",");
     *         ... = columnName();
     *     }
     *
     * or if you prefer even greater concision:
     *
     *     ... = columnName();
     *     while (_input.nextIf(",")) {
     *         ... = columnName();
     *     }
     *
     * (You'll have to figure out what do with the names you accumulate, of
     * course).
     */


    /** A new CommandInterpreter executing commands read from INP, writing
     *  prompts on PROMPTER, if it is non-null. */

    // The commandinterpreter receives input and System.out from Main.
    CommandInterpreter(Scanner inp, PrintStream prompter) {
        _input = new Tokenizer(inp, prompter);
        _database = new Database();
    }

    /** Parse and execute one statement from the token stream.  Return true
     *  iff the command is something other than quit or exit. */
    boolean statement() {
        switch (_input.peek()) {
        case "create":
            createStatement();
            break;
        case "load":
            loadStatement();
            break;
        case "exit": case "quit":
            exitStatement();
            return false;
        case "*EOF*":
            return false;
        case "insert":
            insertStatement();
            break;
        case "print":
            printStatement();
            break;
        case "select":
            selectStatement();
            break;
        case "store":
            storeStatement();
            break;
        case "commit":
            commitStatement();
            break;
        case "rollback":
            rollbackStatement();
            break;
        case ";":
            _input.next();
            break;
        default:
            throw error("unrecognizable command");
        }
        return true;
    }

    /** Parse and execute a create statement from the token stream. */
    void createStatement() {
        _input.next("create");
        _input.next("table");
        String name = name();
        Table table = tableDefinition();
        _database.put(name,table);
        _input.next(";");
    }

    /** Parse and execute an exit or quit statement. Actually does nothing
     *  except check syntax, since statement() handles the actual exiting. */
    void exitStatement() {
        if (!_input.nextIf("quit")) {
            _input.next("exit");
        }
        _input.next(";");
    }

    /** Parse and execute an insert statement from the token stream. */
    void insertStatement() {
        _input.next("insert");
        _input.next("into");
        Table table = tableName();
        _input.next("values");

        // ArrayList<String> values = new ArrayList<>();
        // values.add(literal());
        // while (_input.nextIf(",")) {
        //     values.add(literal());
        // }

        // table.add(new Row(values.toArray(new String[values.size()])));
        do{
            if (_input.nextIf("(")) {
                ArrayList<String> values = new ArrayList<>();
                int times = 0;
                do {
                    values.add(literal());
                    times++;
                } while (_input.nextIf(","));
                if (times > table.columns())
                    throw error("Too many arguments.");
                else if (times < table.columns())
                    throw error("Too few arguments.");
                else if (_input.nextIf(")") == false)
                    throw error("Syntax error, insert \") Statement\" to complete InsertionStatement.");
                else
                    table.add(new Row(values.toArray(new String[values.size()])));
            }
        } while (_input.nextIf(","));
        _input.next(";");
    }

    /** Parse and execute a load statement from the token stream. */
    void loadStatement() {
        _input.next("load");
        String name=name();
        if(!_input.peek().equals(";"))
            throw error("Too many arguments");
        try{
            Table table=Table.readTable(name);
            _database.put(name,table);
            System.out.println("Loaded "+name+".db");
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }
        _input.nextIf(";");
    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String name = _input.next();
        Table table = tableName();
        if (!_input.peek().equals(";"))
            throw error("Too many arguments");
        try{
            table.writeTable(name);
            System.out.printf("Stored %s.db%n", name);
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }
        _input.nextIf(";");
        //System.out.println("???");
        //System.out.println(_input.peek());
    }

    /** Parse and execute a commit statement from the token stream. */
    void commitStatement() {
        _input.next("commit");
        String name = _input.peek();
        Table table = tableName();
        if (!_input.peek().equals(";"))
            throw error("Too many arguments");
        try{
            //update snapshots
            String version_name = table.updateSnapshots(name);

            //update logs
            table.updateLogs(name, version_name);

            // update versions
            table.removeCurrentVersion(name, version_name);
            if (_database.version_tree.Find(version_name).equals("Version Not Found")) {
                table.addVersion(name, version_name);
                // System.out.println("new version");
            }
            table.addVersion(name, version_name);
            // System.out.println("current version");

            // update version_tree
            if (_database.version_tree.Find(version_name).equals("Version Not Found")) {
                _database.version_tree.Insert(version_name);
            }

            System.out.printf("Committed %s.db%n", name);
            System.out.println("Snapshot version name:" + version_name);
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }
        _input.next(";");
    }

    /** Parse and execute a rollback statement from the token stream.
     *  rollback TABLE to 'VERSION_PREFIX'
     *  rollback TABLE at 'TIME'
     */
    void rollbackStatement() {
        _input.next("rollback");
        String table_name = name();
        String version_name = "";
        if (_input.nextIf("to")) {
            String version_prefix = literal();
            version_name = _database.version_tree.Find(version_prefix);
        } else {
            _input.next("at");
            String time = literal();
            version_name = Table.findVersionAt(time, table_name);
        }
        if (version_name.equals("Invalid time") || version_name.equals("Version Not Found") || version_name.equals("More than one version shares the same name")) {
            throw error(version_name);
        } else {
            try {
                version_name = "snapshots/" + version_name;
                Table table=Table.readTable(version_name);
                _database.put(table_name,table);
                System.out.println("Rollbacked "+table_name+".db");
            }
            catch(DBException e){
                throw error("%s", e.getMessage());
            }
        }
        _input.nextIf(";");
    }

    /** Parse and execute a print statement from the token stream. */
    void printStatement() {
        _input.next("print");
        String s = _input.peek();
        Table table = tableName();
        _input.next(";");
        System.out.println("Table " + s + ":");
        table.print();

    }

    /** Parse and execute a select statement from the token stream. */

    void selectStatement() {
        _input.next("select");
        _input.setPos();
        Table selectTable = selectClause();
        if(_input.nextIs("order")) {
            ArrayList<String> Order;
            Order = orderByClause(selectTable);
            List<Row> result = selectTable.order(Order);
            _input.next(";");
            System.out.println("Search results:");
            selectTable.print(result);
        } else {
            _input.next(";");
            System.out.println("Search results:");
            selectTable.print();
        }
    }

    /** Parse and execute a table definition, returning the specified
     *  table. */
    Table tableDefinition() {
        Table table;
        if (_input.nextIf("(")) {
            ArrayList<String> columnTitles = new ArrayList<String>();
            ArrayList<String> columnTypes = new ArrayList<String>();
            String primary_key = null;
            do{
                if(_input.nextIf("primary")){
                    _input.next("key");
                    primary_key = columnName();
                    break;
                }
                columnTitles.add(columnName());
                String type_string = columnName();
                int type = gettype(type_string);
                if (type == -1) throw error("Syntax error, every column needs a data type(int,double or string)");
                columnTypes.add(type_string);
            } while (_input.nextIf(","));
            if(_input.nextIf(")")==false) throw error("Syntax error, insert \") Statement\" to complete CreateStatement.");
            else table = new Table(columnTitles,columnTypes);
            if(primary_key!=null) table.primary_key(primary_key);
        } else {
            table = null;
            if (_input.nextIf("as")) {
                _input.next("select");
                table = selectClause();
            }
            else {
                throw error("Error: A table must have at least one visible column.");
            }
        }
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table. */
    /*select SID, Firstname from students */
    /*Grammar for integrate function: select avg columnName from table */
    
    Table selectClause() {
        /* for the aggregated functions (except ROUND), we do not allow nonaggregated columns
         * selected together. e.g. SELECT avg score, name from students; is
         * not allowed.
         * 
         * for the ROUND function, we treat it as a normal column, so it is not allowed
         * to do SELECT round score plus 100 3, avg age from students;
         * But it is allowed to do SELECT round score plus 100 3, age from students;
        */

        // 3 lists used for ROUND columnName operator operand reservedDigit
        ArrayList<String> operator = new ArrayList<String>();
        ArrayList<String> operand = new ArrayList<String>();
        ArrayList<String> reservedDigit = new ArrayList<String>();
        // columnTitles used to direct the select function
        ArrayList<String> columnTitle = new ArrayList<String>();
        // for columns with aggregated functions, we have to change the columnTitles and types.
        ArrayList<String> changedTitle = new ArrayList<String>();
        ArrayList<String> changedType = new ArrayList<String>();
        /* for one-row aggregated functions, return a row with all functions results
         * e.g. select avg score, max age from students;
         * the computed list contains a row with data[avg(score), max(age)].
         * Then it is inserted into a new table called res.
        */
        ArrayList<String> computed = new ArrayList<String>();
        // used to contain the functions used. Functions are stored as integers.
        // Note: the ROUND function is not stored in this list.
        ArrayList<Integer> functions = new ArrayList<Integer>();
        // used to contain whether a column needs to be rounded. 0 as not, 1 as yes.
        ArrayList<Integer> rounds = new ArrayList<Integer>();
        //for each function stored in the "functions" list, associate it with the columnName.
        ArrayList<String> funcToColName = new ArrayList<String>();
        // flag is used for SELECT *, 
        // and countStar is used to get the columnId to which the COUNT * corresponds.
        int flag=0, countStar = -1;
        boolean haveFunc = true, special_round;
        while(!_input.nextIf("from")){
            if(_input.nextIf("*")){
                flag=1;
            }
            else{
                haveFunc = true;
                special_round = false;
                switch (_input.peek()){
                    case "avg":
                        functions.add(1);
                        _input.next();
                        break;
                    case "max":
                        functions.add(2);
                        _input.next();
                        break;
                    case "count":
                        functions.add(3);
                        _input.next();
                        if (_input.nextIs("*"))
                            countStar = functions.size()-1;
                        break;
                    case "min":
                        functions.add(4);
                        _input.next();
                        break;
                    case "sum":
                        functions.add(5);
                        _input.next();
                        break;
                    case "round":
                        rounds.add(1); // this column needs ROUND
                        special_round = true; // for this column, the user uses ROUND function.
                        haveFunc = false;
                        _input.next();
                        break;
                    default:
                        rounds.add(0); // this column does not need ROUND
                        haveFunc = false;
                }
                if (!haveFunc && functions.size() > 0) {
                    throw error("aggregated SELECT list contains nonaggregated column, which is incompatible.");
                }
                String colName = columnName();
                if ((!haveFunc || !columnTitle.contains(colName)) && !colName.equals("*"))
                    columnTitle.add(colName);
                if (special_round) {
                    String temp1 = _input.next();
                    operator.add(temp1);
                    String temp2 = _input.next();
                    operand.add(temp2);
                    _input.nextIf("reserve");
                    String temp3 = _input.next();
                    reservedDigit.add(temp3);
                    switch(temp1) {
                        case "plus": temp1 = "+"; break;
                        case "minus": temp1 = "-"; break;
                        case "times": temp1 = "*"; break;
                        case "divided_by": temp1 = "/"; break;
                        default:
                            throw error("invalid operator \'%s\'.", temp1);
                    }
                    changedTitle.add("ROUND(" + colName + temp1 + temp2 + "~" + temp3 + ")");
                }
                if (haveFunc) {
                    funcToColName.add(colName);
                    switch(functions.get(functions.size()-1)) {
                        case 1: colName = "AVG("+colName+")";
                            break;
                        case 2: colName = "MAX("+colName+")";
                            break;
                        case 3: colName = "COUNT("+colName+")";
                            break;  
                        case 4: colName = "MIN("+colName+")";
                            break;
                    }
                }
                if (_input.nextIs(Tokenizer.LITERAL)) {
                    colName = _input.peek();
                    _input.next();
                    colName = colName.substring(1, colName.length() - 1).trim();
                    if (special_round) {
                        changedTitle.remove(changedTitle.size()-1);
                        changedTitle.add(colName);
                    }
                }
                if (!special_round)
                    changedTitle.add(colName);
                changedType.add("double");
            }
            _input.nextIf(",");
        }
        if (columnTitle.size() == 0 && flag==0 && countStar == -1){
            throw error("missing argument(s) for statement SELECT: select at least one column.");
        }
        Table Table1 = tableName();
        Table Table2 = null;
        if (_input.nextIf(",")) {
            Table2 = tableName();
        }
        ArrayList<Condition> conditions;
        ArrayList<String> swap = new ArrayList<String>();
        _swap = null;
        if (null != Table2) {
            conditions = conditionClause(Table1, Table2);
        } else {
            conditions = conditionClause(Table1);
        }
        // In statement
        if(conditions == null  &&  _swap != null){
            if (_input.nextIf("not")) {
                Table1.change_to_complement();
            }
            _input.next("in");
            for(String s: _swap){
                swap.add(s);
            }
            _swap = null;
            if(!_input.nextIf("select")) throw error("Syntax Error. Need a \"select\" after \"in\"");
            Table2 = selectClause();
            Table2 = Table2.select(Table2,swap,conditions);
        }
        if (null != Table2) {
            ArrayList<String> tempTitle = new ArrayList<String>();
            if(flag==1){
                for(int i=0;i<Table1.columns();i++){
                    columnTitle.add(Table1.getTitle(i));
                    changedTitle.add(Table1.getTitle(i));
                }
                for(int i=0;i<Table2.columns();i++){
                    if(columnTitle.contains(Table2.getTitle(i)))continue;
                    columnTitle.add(Table2.getTitle(i));
                    changedTitle.add(Table1.getTitle(i));
                }
            }

            for(int i=0;i<Table1.columns();i++){
                tempTitle.add(Table1.getTitle(i));
            }
            for(int i=0;i<Table2.columns();i++){
                if(tempTitle.contains(Table2.getTitle(i)))continue;
                tempTitle.add(Table2.getTitle(i));
            }
            // contain the result of select.
            Table selectRes = Table1.select(Table2, columnTitle, conditions);

            /* group by clause */
            if (_input.nextIf("group") && _input.nextIf("by")) {
                selectRes = groupByClause(selectRes, haveFunc);
                return selectRes;
            }

            /* the non-repetitive feature of select statement can influence
             * the result of aggregated functinos.
             * To avoid this, we need an "original big table" named joinRes.
            */
            Table joinRes = Table1.select(Table2, tempTitle, conditions);
            if (functions.size() > 0) {
                computed = joinRes.conductFunctions(functions, funcToColName);
            }
            if (functions.size() > 0) {
                Table res = new Table(changedTitle, changedType);
                res.add(new Row(computed.toArray(new String[computed.size()])));
                return res;
            }
            // operator.size > 0 means the ROUND function exists.
            if (operator.size() > 0) {
                Table res = selectRes.conductRound(rounds, operand, operator, reservedDigit);
                return res.changeTitle(changedTitle);
            }
            return selectRes.changeTitle(changedTitle);
        } else {
            if (flag == 1){
                for(int i=0;i<Table1.columns();i++){
                    columnTitle.add(Table1.getTitle(i));
                    changedTitle.add(Table1.getTitle(i));
                }
            }
            //System.out.println("???");
            
            if (columnTitle.size() == 0) {
                // for COUNT *, we do not store "*" as columnTitle,
                // so we need to specify a column of the given table for COUNT
                // if there is no other functions.
                funcToColName.set(countStar, Table1.getTitle(0));
                columnTitle.add(Table1.getTitle(0));
            }
            else if (countStar != -1){
                // if there are other functions, just let COUNT deal with
                // the same column.
                funcToColName.set(countStar, columnTitle.get(0));
            }
            Table selectRes = Table1.select(columnTitle, conditions);

            /* group by clause */
            if (_input.nextIf("group") && _input.nextIf("by")) {
                selectRes = groupByClause(selectRes, haveFunc);
                return selectRes;
            }

            if (functions.size() > 0) {
                computed = Table1.conductFunctions(functions, funcToColName);
                Table res = new Table(changedTitle, changedType);
                res.add(new Row(computed.toArray(new String[computed.size()])));
                return res;
            }
            if (operator.size() > 0) {
                Table res = selectRes.conductRound(rounds, operand, operator, reservedDigit);
                return res.changeTitle(changedTitle);
            }
            return selectRes.changeTitle(changedTitle);
        }
    }

    /** Parse and execute a group by clause from the token stream, returning the
     *  resulting table containing grouped information. */
    Table groupByClause(Table table, boolean haveFunc) {
        if (!haveFunc) throw error("no aggregation functions for group by statement");
        ArrayList<String> groupByColumns = new ArrayList<>();

        _input.returnPos();
        ArrayList<String> Order = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();
        Order.add(_input.peek());
        columnNames.add(_input.next());
        Order.add("asc");
        while (_input.nextIf(",")) {
            if (!_input.nextIs("count") &&
                    !_input.nextIs("avg") &&
                    !_input.nextIs("max") &&
                    !_input.nextIs("min") &&
                    !_input.nextIs("sum") &&
                    !_input.nextIs("round")) {
                Order.add(_input.peek());
                columnNames.add(_input.next());
                Order.add("asc");
            } else break;
        }

        String agg = _input.next();
        String col = columnName();
        ArrayList<String> colNames = new ArrayList<>();
        colNames.add(col);

        while (!((_input.nextIf("group") && _input.nextIf("by")))) {
            _input.next();
        }

        String columnName = literal();
        int id = table.findColumn(columnName);
        if (id == -1) {
            throw error("unknown column: %s", columnName);
        } else groupByColumns.add(columnName);

        while (_input.nextIf(",")) {
            columnName = literal();
            id = table.findColumn(columnName);
            if (id == -1) {
                throw error("unknown column: %s", columnName);
            } else groupByColumns.add(columnName);
        }

        id = table.findColumn(columnName);
        if (id == -1) {
            throw error("unknown column: %s", columnName);
        }

        if (groupByColumns.size() != columnNames.size()) throw error("columns mismatch in group by statement");
        else {
            for (String s : groupByColumns) {
                if (!columnNames.contains(s)) throw error("columns mismatch in group by statement");
            }
        }
        List<Integer> index = new ArrayList<>();
        List<Row> ordered = table.order(Order);
        for (int i = 1; i < ordered.size(); i++) {
            Row temp = ordered.get(i);
            Row prev = ordered.get(i - 1);
            for (int j = 0; j < columnNames.size(); j++) {
                if (!Objects.equals(temp.get(j), prev.get(j))) {
                    index.add(i);
                    break;
                }
            }
        }
        ArrayList<Integer> aggFunc = new ArrayList<>();
        String[] titles = Arrays.copyOf(table.getTitles(), table.getTitles().length);
        switch (agg) {
            case "avg" -> {
                titles[titles.length - 1] = "AVG(" + col + ")";
                aggFunc.add(1);
            }
            case "max" -> {
                titles[titles.length - 1] = "MAX(" + col + ")";
                aggFunc.add(2);
            }
            case "sum" -> {
                aggFunc.add(5);
            }
            case "min" -> {
                titles[titles.length - 1] = "MIN(" + col + ")";
                aggFunc.add(4);
            }
            case "count" -> {
                titles[titles.length - 1] = "COUNT(" + col + ")";
                aggFunc.add(3);
            }
            case "round" -> throw error("round function does not support group by statement");
        }

        Table result = new Table(titles, table.get_types());
        int ind = 0;
        int size = index.size();
        for (int i = 0; i < size + 1; i++) {
            String[] row;
            Table temp = new Table(table.getTitles(), table.get_types());
            if (index.isEmpty()) {
                for (int j = ind; j < ordered.size(); j++) {
                    temp.add(ordered.get(j));
                }
                row = ordered.get(ind).get();
            } else {
                for (int j = ind; j < index.get(0); j++) {
                    temp.add(ordered.get(j));
                }
                row = ordered.get(ind).get();
                ind = index.remove(0);
            }
            ArrayList<String> aggRes = temp.conductFunctions(aggFunc, colNames);
            row[row.length - 1] = aggRes.get(0);
            result.add(new Row(row));
        }
        return result;
    }


    /** Parse and execute an order by clause from the token stream, returning the
     * resulting list containing the order information.
     * grammar: select <column names> from <tables> where <conditions> order by '<column name>';
     */
    ArrayList<String> orderByClause(Table table) {
        ArrayList<String> Order = new ArrayList<String>();
        if (_input.nextIf("order") && _input.nextIf("by")) {
            String columnName = literal();
            int id = table.findColumn(columnName);
            if (id == -1) {
                throw error("unknown column: %s", columnName);
            } else {
                Order.add(columnName);
                if (_input.nextIs("desc") || _input.nextIs("asc")) {
                    Order.add(name());
                } else {
                    Order.add("asc");
                }
            }
            while (_input.nextIf(",")) {
                columnName = literal();
                id = table.findColumn(columnName);
                if (id == -1) {
                    throw error("unknown column: %s", columnName);
                } else {
                    Order.add(columnName);
                    if (_input.nextIs("desc") || _input.nextIs("asc")) {
                        Order.add(name());
                    } else {
                        Order.add("asc");
                    }
                }
            }
            return Order;
        } else {
            return null;
        }
    }

    /** Parse and return a valid name (identifier) from the token stream. */
    String name() {
        return _input.next(Tokenizer.IDENTIFIER);
    }

    /** Parse and return a valid column name from the token stream. Column
     *  names are simply names; we use a different method name to clarify
     *  the intent of the code. */
    String columnName() {
        return name();
    }

    /** Parse a valid table name from the token stream, and return the Table
     *  that it designates, which must be loaded. */
    Table tableName() {
        String name = name();
        Table table = _database.get(name);
        if (table == null) {
            throw error("unknown table: %s", name);
        }
        return table;
    }

    /** Parse a literal and return the string it represents (i.e., without
     *  single quotes). */
    String literal() {
        String lit = _input.next(Tokenizer.LITERAL);
        return lit.substring(1, lit.length() - 1).trim();
    }

    /** Parse and return a list of Conditions that apply to TABLES from the
     *  token stream.  This denotes the conjunction (`and') zero
     *  or more Conditions. */
    ArrayList<Condition> conditionClause(Table... tables) {
        ArrayList<Condition> alfa=new ArrayList<Condition>();
        if(_input.nextIf("where")){
            String col1,relation,col2,val;
            while((!(_input.peek().equals(";"))) && (!(_input.peek().equals("order"))) &&
                    (!(_input.peek().equals("group")))){
                try{
                    col1 = columnName();

                    /* BETWEEN clause.
                     * between 'left_bound' and 'right_bound'*/
                    if (_input.nextIf("between")) {

                        // add Condition(col1, '>=', 'left_bound')
                        relation = ">=";
                        String bound = _input.next();
                        val = bound.substring(1, bound.length() - 1);
                        try{
                            alfa.add(new Condition(new Column(col1, 0, tables[0]), relation, val));
                        }
                        catch(DBException e){
                            if (tables.length != 1)
                                alfa.add(new Condition(new Column(col1,1,tables[0],tables[1]),relation,val));
                            else
                                throw error("column '%s' does not exist.", col1);
                        }

                        // add Condition(col1, '<=', 'right_bound')
                        if(!_input.nextIf("and")){
                            throw error("Incorrect 'between' clause");
                        }

                        relation = "<=";
                        bound = _input.next();
                        val = bound.substring(1, bound.length() - 1);
                        try{
                            alfa.add(new Condition(new Column(col1, 0, tables[0]), relation, val));
                        }
                        catch(DBException e){
                            if (tables.length != 1)
                                alfa.add(new Condition(new Column(col1,1,tables[0],tables[1]),relation,val));
                            else
                                throw error("column '%s' does not exist.", col1);
                        }

                        continue;
                    }
                    // Like RELATION cause
                    if (_input.nextIf("like")) {
                        relation = "like";
                        String tmp_val = literal();
                        val = tmp_val.replaceAll("%", ".*");
                        val = val.replaceAll("_", ".");
                        try{
                            alfa.add(new Condition(new Column(col1, 0, tables[0]), relation, val));
                        } catch(DBException e){
                            throw error("column '%s' does not exist.", col1);
                        }
                        continue;

                    }

                    // IN cause
                    if (_input.peek().equals(",") || _input.peek().equals("in") || _input.peek().equals("not")){
                        ArrayList<String> swap = new ArrayList<String>();
                        swap.add(col1);
                        while(_input.nextIf(",")){
                            String col = _input.next();
                            swap.add(col);
                        }
                        _swap = swap;
                        //if(!tmp.next("in")) throw error("too few argument to satisfy the statement WHERE: please include \"in\" or other relation symbols.");
                        return null;
                    }

                    // single RELATION cause
                    relation = _input.next();
                    if(_input.nextIs(Tokenizer.LITERAL)){
                        val = _input.next();
                        val = val.substring(1, val.length() - 1).trim();
                        try{
                            alfa.add(new Condition(new Column(col1,0,tables[0]),relation,val));
                        }
                        catch(DBException e){
                            //System.out.println(tables[1].size());
                            if (tables.length != 1)
                                alfa.add(new Condition(new Column(col1,1,tables[0],tables[1]),relation,val));
                            else
                                throw error("column '%s' does not exist.", col1);
                        }
                    }
                    else{
                        col2=columnName();
                        try{
                            alfa.add(new Condition(new Column(col1,0,tables[0]),relation,new Column(col2,1,tables[0],tables[1])));
                        }
                        catch(DBException e){
                            if (tables.length >= 2)
                                alfa.add(new Condition(new Column(col1,1,tables[0],tables[1]),relation,new Column(col2,0,tables[0])));
                            else
                                throw error("too few tables to satisfy the statement WHERE: please include two tables.");
                        }
                    }
                }
                catch(DBException e){
                    throw error("%s", e.getMessage());
                }
                _input.nextIf("and");
                //System.out.println(_input.peek());
            }
            return alfa;
        }
        else{
            return null;
        }
    }

    /** Parse and return a Condition that applies to TABLES from the
     *  token stream. */
    Condition condition(Table... tables) {
        return null;
    }

    /** Advance the input past the next semicolon. */
    void skipCommand() {
        while (true) {
            try {
                while (!_input.nextIf(";") && !_input.nextIf("*EOF*")) {
                    _input.next();
                }
                return;
            } catch (DBException excp) {
                /* No action */
            }
        }
    }

    /** The command input source. */
    private Tokenizer _input;
    /** Database containing all tables. */
    private Database _database;
    /** _swap is used to temporarily store the "select where in" keywords */
    private ArrayList<String> _swap;
}
