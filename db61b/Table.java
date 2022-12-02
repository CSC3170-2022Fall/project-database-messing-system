// This is a SUGGESTED skeleton for a class that represents a single
// Table.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes or adds
// about 100 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static db61b.Utils.*;

/** A single table in a database.
 *  @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /** A new Table whose columns are given by COLUMNTITLES, which may
     *  not contain dupliace names. */
    Table(String[] columnTitles) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                                columnTitles[i]);
                }
            }
        }
        _column_titles = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles) {
        this(columnTitles.toArray(new String[columnTitles.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _column_titles.length;
    }

    /** Return the title of the Kth column.  Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _column_titles[k];
    }

    /** Return the number of the column whose title is TITLE, or -1 if
     *  there isn't one. */
    public int findColumn(String title) {
        for (int i = 0; i < _column_titles.length; i++)
            if (_column_titles[i].equals(title))
                return i;
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size();  // REPLACE WITH SOLUTION
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /** Add ROW to THIS if no equal row already exists.  Return true if anything
     *  was added, false otherwise. */
    public boolean add(Row row) {
        if(!(_rows.contains(row))){
            _rows.add(row);
            return true;
        }
        return false; //duplicate row
    }

    /** Read the contents of the file NAME.db, and return as a Table.
     *  Format errors in the .db file cause a DBException. */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            input = new BufferedReader(new FileReader(name + ".db"));
            String header = input.readLine();
            if (header == null) {
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            table=new Table(columnNames);
            header = input.readLine();
            while(header != null){
                String[] value=header.split(",");
                if(value.length!=columnNames.length){
                    throw error("wrong data in DB file");
                }
                //System.out.println(header+"?????");
                table._rows.add(new Row(value));
                if(input==null) break;
                header = input.readLine();
            }
            input.close();
            // FILL IN
        } catch (FileNotFoundException e) {
            throw error("could not find %s.db", name);
        } catch (IOException e) {
            throw error("problem reading from %s.db", name);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    /* Ignore IOException */
                }
            }
        }
        return table;
    }

    /** Write the contents of TABLE into the file NAME.db. Any I/O errors
     *  cause a DBException. */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            String sep;
            sep = "";
            output = new PrintStream(name + ".db");
            // FILL THIS IN
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /** Print my contents on the standard output. */
    final int MAX_ROW = 100; // The maxinum of output rows
    int[] find_max_length(){
        int[] max_length = new int[_column_titles.length];
        int rows = 0;
        for (int i = 0; i < _column_titles.length; i++)
            max_length[i] = Math.max(4, _column_titles[i].length());
        Iterator<Row> it = _rows.iterator();
        while (it.hasNext() && rows < MAX_ROW) {
            Row tmp = it.next();
            rows += 1;
            for (int i = 0; i < _column_titles.length; i++)
                max_length[i] = Math.max(max_length[i],tmp.get(i).length());
        }        
        return max_length;
    }
    void print_titles(int[] max_length){
        for (int i = 0; i < _column_titles.length; i++)
            System.out.printf("|%" + max_length[i] + "s", _column_titles[i]);
        System.out.println("|");
    }
    void print_separator(int[] max_length){
        for (int i = 0; i < max_length.length; i++){
            System.out.print("+");
            for (int j = 0; j < max_length[i]; j++)
                System.out.print("-");
        }
        System.out.println("+");
    }
    void print_row(int[] max_length){
        Iterator<Row> it = _rows.iterator();
        int rows = 0;
        while (it.hasNext() && rows < MAX_ROW) {
            Row tmp = it.next();
            for (int i = 0; i < _column_titles.length; i++){
                if (tmp.get(i) != null)
                    System.out.printf("|%" + max_length[i] + "s", tmp.get(i));
                else
                    System.out.printf("|%" + max_length[i] + "s", "NULL");
            }
            System.out.println("|");
        }
        if(rows == MAX_ROW)
            System.out.print(" .\n .\n .\n");
    }
    void print() {
        int[] max_length = find_max_length();
        print_separator(max_length);
        print_titles(max_length);
        print_separator(max_length);
        // for (int i = 0; i < _column_titles.length; i++){
        //     System.out.print(_column_titles[i]);
        //     if(i!=_column_titles.length-1)System.out.print(",");
        // }
        // System.out.println("");
        // for(Row s:_rows){
        //     for (int i = 0; i < _column_titles.length; i++){
        //         System.out.print(s.get(i));
        //         if(i!=_column_titles.length-1)System.out.print(",");
        //     }
        //         System.out.println("");
        // }
        print_row(max_length);
        print_separator(max_length);
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected from
     *  rows of this table that satisfy CONDITIONS. */
    Table select(List<String> columnNames, List<Condition> conditions) {
        Table result = new Table(columnNames);
        // FILL IN
        return result;
    }

    /** Return a new Table whose columns are COLUMNNAMES, selected
     *  from pairs of rows from this table and from TABLE2 that match
     *  on all columns with identical names and satisfy CONDITIONS. */
    Table select(Table table2, List<String> columnNames,
                 List<Condition> conditions) {
        Table result = new Table(columnNames);
        // FILL IN
        return result;
    }

    /** Return true if the columns COMMON1 from ROW1 and COMMON2 from
     *  ROW2 all have identical values.  Assumes that COMMON1 and
     *  COMMON2 have the same number of elements and the same names,
     *  that the columns in COMMON1 apply to this table, those in
     *  COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     *  from those tables. */
    private static boolean equijoin(List<Column> common1, List<Column> common2,Row row1, Row row2) {
        return true; // REPLACE WITH SOLUTION
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
    private String[] _column_titles;
}

