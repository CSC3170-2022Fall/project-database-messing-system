// This is a SUGGESTED skeleton for a class that parses and executes database
// statements.  Be sure to read the STRATEGY section, and ask us if you have any
// questions about it.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution adds or changes about 50
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static db61b.Utils.*;
import static db61b.Tokenizer.*;

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
        //System.out.println("!!!");
        _input.next(";");
        try{
            Table table=Table.readTable(name);
            _database.put(name,table);
            System.out.println("Loaded "+name+".db");
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }

        //System.out.println("!!");
        //table.print();
        // FILL THIS IN
    }

    /** Parse and execute a store statement from the token stream. */
    void storeStatement() {
        _input.next("store");
        String name = _input.next();
        Table table = tableName();
        _input.next(";");
        try{
            table.writeTable(name);
            System.out.printf("Stored %s.db%n", name);
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }
        // FILL THIS IN

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
        Table selectTable = selectClause();
        _input.next(";");
        System.out.println("Search results:");
        selectTable.print();
        // FILL THIS IN
    }

    /** Parse and execute a table definition, returning the specified
     *  table. */
    Table tableDefinition() {
        Table table;
        if (_input.nextIf("(")) {
            ArrayList<String> columnTitles = new ArrayList<String>();
            ArrayList<String> columnTypes = new ArrayList<String>();
            do{
                columnTitles.add(columnName());
                String type_string = columnName();
                int type = gettype(type_string);
                if (type == -1) throw error("Syntax error, every column needs a data type(int,double or string)");
                columnTypes.add(type_string);
            } while (_input.nextIf(","));
            if(_input.nextIf(")")==false) throw error("Syntax error, insert \") Statement\" to complete CreateStatement.");
            else table = new Table(columnTitles,columnTypes);
        } else {
            table = null;
            if (_input.nextIf(";"))
                throw error("Error: A table must have at least one visible column.");
            else {
                while (!_input.nextIf(";"))
                        _input.next();
                throw error("Syntax error: \'(\' is expected after table name.");
            }
        }
        return table;
    }

    /** Parse and execute a select clause from the token stream, returning the
     *  resulting table. */
    /*select SID, Firstname from students */
    Table selectClause() {
        ArrayList<String> columnTitle = new ArrayList<String>();
        while(!_input.nextIf("from")){
            String colName= columnName();
            columnTitle.add(colName);
            _input.nextIf(",");
        }
        if (columnTitle.size() == 0) {
            throw error("missing argument(s) for statement SELECT: select at least one column.");
        }
        Table Table1 = tableName();
        Table Table2 = null;
        if (_input.nextIf(",")) {
            Table2 = tableName();
        }
        ArrayList<Condition> conditions;
        if (null != Table2) {
            conditions = conditionClause(Table1, Table2);
        } else {
            conditions = conditionClause(Table1);
        }
        if (null != Table2) {
            return Table1.select(Table2, columnTitle, conditions);
        } else {
            //System.out.println("???");
            return Table1.select(columnTitle, conditions);
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
            while(!(_input.peek().equals(";"))){
                try{
                    col1=columnName();
                    relation = _input.next();
                    String tmp = _input.peek();
                    if(tmp.charAt(0) =='\''){
                        tmp=_input.next();
                        val=tmp.substring(1,tmp.length()-1);
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
        }       // REPLACE WITH SOLUTION
    }

    /** Parse and return a Condition that applies to TABLES from the
     *  token stream. */
    Condition condition(Table... tables) {

        return null;        // REPLACE WITH SOLUTION
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
}