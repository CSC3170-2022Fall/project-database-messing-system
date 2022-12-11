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
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.stream.Collectors;
import java.text.NumberFormat;
import java.lang.NumberFormatException;

import static db61b.Utils.*;

/**
 * A single table in a database.
 *
 * @author P. N. Hilfinger
 */
class Table implements Iterable<Row> {
    /**
     * A new Table whose columns are given by COLUMNTITLES, which may
     * not contain dupliace names.
     */
    Table(String[] columnTitles, String[] columnTypes) {
        for (int i = columnTitles.length - 1; i >= 1; i -= 1) {
            for (int j = i - 1; j >= 0; j -= 1) {
                if (columnTitles[i].equals(columnTitles[j])) {
                    throw error("duplicate column name: %s",
                            columnTitles[i]);
                }
            }
            if (gettype(columnTypes[i]) == -1)
                throw error("wrong data type");
        }
        _column_types = columnTypes;
        _column_titles = columnTitles;
    }

    /** A new Table whose columns are give by COLUMNTITLES. */
    Table(List<String> columnTitles, List<String> columnTypes) {
        this(columnTitles.toArray(new String[columnTitles.size()]),
                columnTypes.toArray(new String[columnTypes.size()]));
    }

    /** Return the number of columns in this table. */
    public int columns() {
        return _column_titles.length;
    }

    public Double maxColumn(Table table, int colId) {
        if (table._column_types[colId].equals("string")) {
            throw error("cannot apply MAX to column \'%s\' with type string.", table.getTitle(colId));
        }
        int flag = 0;
        Double max = 0.0d;
        for (Row row : _rows) {
            try{
                double temp = Double.parseDouble(row.get(colId));
                if (flag == 0) {
                    max = temp;
                    flag = 1;
                } else if (temp > max)
                    max = temp;
            }catch (java.lang.NumberFormatException e) {
                throw error("Data type error. Cannot do the operation \"max\" on it.");
            }
        }
        return max;
    }

    public Double minColumn(Table table, int colId) {
        if (table._column_types[colId].equals("string")) {
            throw error("cannot apply MIN to column \'%s\' with type string.", table.getTitle(colId));
        }
        int flag = 0;
        Double min = 0.0d;
        for (Row row : _rows) {
            try{
                double temp = Double.parseDouble(row.get(colId));
                if (flag == 0) {
                    min = temp;
                    flag = 1;
                } else if (temp < min)
                    min = temp;
            } catch (java.lang.NumberFormatException e) {
                throw error("Data type error. Cannot do the operation \"min\" on it.");
            }
        }
        return min;
    }

    public Double avgColumn(Table table, int colId) {
        if (table._column_types[colId].equals("string")) {
            throw error("cannot apply AVG to column \'%s\' with type string.", table.getTitle(colId));
        }
        double avg = 0;
        for (Row row : _rows) {
            try{
                avg += Double.parseDouble(row.get(colId));
            } catch (java.lang.NumberFormatException e) {
                throw error("Data type error. Cannot do the operation \"average\" on it.");
            }
        }
        avg /= _rows.size();
        return avg;
    }

    public ArrayList<String> conductFunctions(ArrayList<Integer> functions, ArrayList<String> names, Table table) {
        // since for SELECT with aggregated functions, the result can only be one row,
        // we just need to store all the data in one list and return.
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < functions.size(); ++i) {
            switch (functions.get(i)) {
                case 1:
                    res.add(Double.toString(avgColumn(table, table.findColumn(names.get(i)))));
                    break;
                case 2:
                    res.add(Double.toString(maxColumn(table, table.findColumn(names.get(i)))));
                    break;
                case 3:
                    res.add(Integer.toString(table.size()));
                    break;
                case 4:
                    res.add(Double.toString(minColumn(table, table.findColumn(names.get(i)))));
                    break;
            }
        }
        return res;
    }

    public Table conductRound(ArrayList<Integer> rounds, Table table, ArrayList<String> q1, ArrayList<String> q2,
            ArrayList<String> q3) {
        ArrayList<String> newColTitle = new ArrayList<String>();
        for (int i = 0; i < table._column_titles.length; ++i) {
            if (rounds.get(i) == 1)
                newColTitle.add("ROUND(" + table._column_titles[i] + ")");
            else
                newColTitle.add(table._column_titles[i]);
        }

        Table res = new Table(newColTitle.toArray(new String[newColTitle.size()]), table._column_types);

        // insert result rows into the table "res" one by one.
        for (Row row : table._rows) {            
            ArrayList<String> newRow = new ArrayList<String>();
            for (int i = 0, index = 0; i < row.size(); i++) {
                if (rounds.get(i) == 0) {
                    newRow.add(row.get(i));
                }
                else{
                    if (table._column_types[i].equals("string")) {
                        throw error("cannot apply ROUND to column \'%s\' with type string.", table.getTitle(i));
                    }
                    int limit = Integer.parseInt(q3.get(index));
                    double num = Double.parseDouble(q1.get(index));
                    double calc = 0.0d;
                    String op = q2.get(index);
                    switch (op) {
                        case "plus":
                            try {
                                calc = Double.parseDouble(row.get(i)) + num;
                                break;
                            } catch (java.lang.NumberFormatException e) {
                                throw error("Data type error. Cannot do the operation \"%s\" on it.", op);
                            }
                        case "minus":
                            try {
                                calc = Double.parseDouble(row.get(i)) - num;
                                break;
                            } catch (java.lang.NumberFormatException e) {
                                throw error("Data type error. Cannot do the operation \"%s\" on it.", op);
                            }
                        case "times":
                            try {
                                calc = Double.parseDouble(row.get(i)) * num;
                                break;
                            } catch (java.lang.NumberFormatException e) {
                                throw error("Data type error. Cannot do the operation \"%s\" on it.", op);
                            }
                        case "divided_by":
                            try {
                                calc = Double.parseDouble(row.get(i)) / num;
                                break;
                            } catch (java.lang.NumberFormatException e) {
                                throw error("Data type error. Cannot do the operation \"%s\" on it.", op);
                            }
                        default:
                            throw error("invalid operator \'%s\'.", op);
                    }
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    nf.setMaximumFractionDigits(limit);
                    newRow.add(nf.format(calc));
                    index++;
                }
            }
            res.add(new Row(newRow.toArray(new String[newRow.size()])));

        }
        return res;
    }

    /** Return the title of the Kth column. Requires 0 <= K < columns(). */
    public String getTitle(int k) {
        return _column_titles[k];
    }

    /**
     * Return the number of the column whose title is TITLE, or -1 if
     * there isn't one.
     */
    public int findColumn(String title) {
        for (int i = 0; i < _column_titles.length; i++)
            if (_column_titles[i].equals(title))
                return i;
        return -1;
    }

    /** Return the number of Rows in this table. */
    public int size() {
        return _rows.size(); // REPLACE WITH SOLUTION
    }

    /** Returns an iterator that returns my rows in an unspecfied order. */
    @Override
    public Iterator<Row> iterator() {
        return _rows.iterator();
    }

    /**
     * Add ROW to THIS if no equal row already exists. Return true if anything
     * was added, false otherwise.
     */
    public boolean add(Row row) {
        if (!(_rows.contains(row))) {
            if (_primary_key != -1) {
                if (_primary_key_set.contains(row.get(_primary_key))) {
                    return false; // duplicate primary key
                }
                _primary_key_set.add(row.get(_primary_key));
            }
            _rows.add(row);
            return true;
        }
        return false; // duplicate row
    }

    /**
     * Read the contents of the file NAME.db, and return as a Table.
     * Format errors in the .db file cause a DBException.
     */
    static Table readTable(String name) {
        BufferedReader input;
        Table table;
        input = null;
        table = null;
        try {
            File file = new File(name + ".db");
            input = new BufferedReader(new FileReader(file));
            String header = input.readLine();
            if (header == null) {
                input.close();
                throw error("missing header in DB file");
            }
            String[] columnNames = header.split(",");
            String types = input.readLine();
            if (types == null) {
                input.close();
                throw error("missing data types in DB file");
            }
            String[] columnTypes = types.split(",");
            table = new Table(columnNames, columnTypes);
            header = input.readLine();
            while (header != null) {
                String[] value = header.split(",");
                if (value.length != columnNames.length) {
                    input.close();
                    throw error("wrong data in DB file");
                }
                // System.out.println(header+"?????");
                table._rows.add(new Row(value));
                if (input == null)
                    break;
                header = input.readLine();
            }
            input.close();
            // FILL IN
        } catch (FileNotFoundException e) {
            // System.out.println("???");
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

    /**
     * Write the contents of TABLE into the file NAME.db. Any I/O errors
     * cause a DBException.
     */
    void writeTable(String name) {
        PrintStream output;
        output = null;
        try {
            output = new PrintStream(name + ".db");
            for (int i = 0; i < _column_titles.length; i++) {
                output.print(_column_titles[i]);
                if (i != _column_titles.length - 1)
                    output.print(",");
            }
            output.println("");
            for (int i = 0; i < _column_titles.length; i++) {
                output.print(_column_types[i]);
                if (i != _column_titles.length - 1)
                    output.print(",");
            }
            output.println("");
            for (Row s : _rows) {
                for (int i = 0; i < _column_titles.length; i++) {
                    output.print(s.get(i));
                    if (i != _column_titles.length - 1)
                        output.print(",");
                }
                output.println("");
            }
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    String updateSnapshots(String name) {
        // get version name
        String str = "";
        for (int i = 0; i < _column_titles.length; i++) {
            str += _column_titles[i];
        }
        for (Row s : _rows) {
            for (int i = 0; i < _column_titles.length; i++) {
                str += s.get(i);
            }
        }
        str = Trie.encrypt_sha_1(str);

        // update Snapshots folder
        PrintStream output;
        output = null;
        try {
            output = new PrintStream("snapshots/" + str + ".db");
            for (int i = 0; i < _column_titles.length; i++) {
                output.print(_column_titles[i]);
                if (i != _column_titles.length - 1)
                    output.print(",");
            }
            output.println("");
            for (int i = 0; i < _column_titles.length; i++) {
                output.print(_column_types[i]);
                if (i != _column_titles.length - 1)
                    output.print(",");
            }
            output.println("");
            for (Row s : _rows) {
                for (int i = 0; i < _column_titles.length; i++) {
                    output.print(s.get(i));
                    if (i != _column_titles.length - 1)
                        output.print(",");
                }
                output.println("");
            }
            // FILL THIS IN
        } catch (IOException e) {
            throw error("trouble writing to %s.db", name);
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return str;
    }

    /** Removes the last line of the version file. */
    void removeCurrentVersion(String name, String version_name) {

        // If NAME doesn't have a version list, create one.
        File file = new File("versions/" + name + ".db");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw error("Cannot initialize a version file for %s", name);
            }
            return;
        }
        try {
            RandomAccessFile f = new RandomAccessFile("versions/" + name + ".db", "rw");
            long fileLength = f.length();
            f.seek(fileLength);
            f.writeBytes(version_name);
            f.close();
        } catch (IOException e) {
            throw error("trouble adding version to %s", name);
        }

        // If NAME already has a version list, delete the last line.
        try {
            RandomAccessFile f = new RandomAccessFile("versions/" + name + ".db", "rw");
            // System.out.println(f.length());
            long length = f.length() - 1;
            byte b = 0;
            do {
                length -= 1;
                f.seek(length);
                b = f.readByte();
            } while (b != 10 && length > 0);
            f.setLength(length);
            f.close();
        } catch (IOException e) {
            throw error("trouble removing version from %s", name);
        }
    }

    void addVersion(String name, String version_name) {
        try {
            RandomAccessFile f = new RandomAccessFile("versions/" + name + ".db", "rw");
            long fileLength = f.length();
            f.seek(fileLength);
            f.writeBytes("\n");
            f.writeBytes(version_name);

            f.close();
        } catch (IOException e) {
            throw error("trouble adding version to %s", name);
        }
    }

    void updateLogs(String name, String version_name) {
        // PrintStream output;
        // output = null;
        // try {
        // output = new PrintStream("logs/" + name + ".db");
        // Date date = new Date();
        // output.println(date.toString() + "," + version_name);

        // } catch (IOException e) {
        // throw error("trouble updating %s.db", name);
        // } finally {
        // if (output != null) {
        // output.close();
        // }
        // }
        // If NAME doesn't have a log, create one.
        File file = new File("logs/" + name + ".log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw error("Cannot initialize a log for %s", name);
            }
        }
        try {
            RandomAccessFile f = new RandomAccessFile("logs/" + name + ".log", "rw");
            long fileLength = f.length();
            f.seek(fileLength);
            Date date = new Date();
            f.writeBytes(date.toString() + "," + version_name);
            f.writeBytes("\n");
            f.close();
        } catch (IOException e) {
            throw error("trouble updating %s.log", name);
        }
    }

    /** Return the version_name of table NAME at TIME */
    static String findVersionAt(String time, String name) {
        String version_name = "Invalid time";
        File file = new File("logs/" + name + ".log");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(time)) {
                    version_name = line.substring(time.length() + 1);
                    reader.close();
                    return version_name;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version_name;
    }

    /** Print my contents on the standard output. */
    final int MAX_ROW = 100; // The maxinum of output rows

    int[] find_max_length() {
        int[] max_length = new int[_column_titles.length];
        int rows = 0;
        for (int i = 0; i < _column_titles.length; i++)
            max_length[i] = Math.max(4, _column_titles[i].length());
        Iterator<Row> it = _rows.iterator();
        while (it.hasNext() && rows < MAX_ROW) {
            Row tmp = it.next();
            rows += 1;
            for (int i = 0; i < _column_titles.length; i++)
                max_length[i] = Math.max(max_length[i], tmp.get(i).length());
        }
        return max_length;
    }

    void print_titles(int[] max_length) {
        for (int i = 0; i < _column_titles.length; i++)
            System.out.printf("|%" + max_length[i] + "s", _column_titles[i]);
        System.out.println("|");
    }

    void print_separator(int[] max_length) {
        for (int i = 0; i < max_length.length; i++) {
            System.out.print("+");
            for (int j = 0; j < max_length[i]; j++)
                System.out.print("-");
        }
        System.out.println("+");
    }

    void print_row(int[] max_length) {
        Iterator<Row> it = _rows.iterator();
        int rows = 0;
        while (it.hasNext() && rows < MAX_ROW) {
            Row tmp = it.next();
            for (int i = 0; i < _column_titles.length; i++) {
                if (tmp.get(i) != null)
                    System.out.printf("|%" + max_length[i] + "s", tmp.get(i));
                else
                    System.out.printf("|%" + max_length[i] + "s", "NULL");
            }
            System.out.println("|");
        }
        if (rows == MAX_ROW)
            System.out.print(" .\n .\n .\n");
    }

    void print_row(int[] max_length, List<Row> list) {
        int rows = 0;
        while (rows < list.size() && rows < MAX_ROW) {
            Row tmp = list.get(rows);
            for (int i = 0; i < _column_titles.length; i++) {
                if (tmp.get(i) != null)
                    System.out.printf("|%" + max_length[i] + "s", tmp.get(i));
                else
                    System.out.printf("|%" + max_length[i] + "s", "NULL");
            }
            System.out.println("|");
            rows++;
        }
        if (rows == MAX_ROW)
            System.out.print(" .\n .\n .\n");
    }

    void print() {
        int[] max_length = find_max_length();
        print_separator(max_length);
        print_titles(max_length);
        print_separator(max_length);

        print_row(max_length);
        print_separator(max_length);
    }

    void print(List<Row> list) {
        int[] max_length = find_max_length();
        print_separator(max_length);
        print_titles(max_length);
        print_separator(max_length);

        print_row(max_length, list);
        print_separator(max_length);
    }

    public String get_type(int i) {
        return _column_types[i];
    }

    public void primary_key(String s) {
        for (int i = 0; i < _column_titles.length; i++) {
            if (_column_titles[i].equals(s)) {
                _primary_key = i;
                break;
            }
        }
    }

    /**
     * Return a new Table whose columns are COLUMNNAMES, selected from
     * rows of this table that satisfy CONDITIONS.
     */
    Table select(List<String> columnNames, List<Condition> conditions) {
        List<String> columnTypes = new ArrayList<String>();
        for (int i = 0; i < columnNames.size(); i++) {
            int id = findColumn(columnNames.get(i));
            if (id == -1) {
                throw error("column \"" + columnNames.get(i) + "\" does not exist.");
            }
            String type = this.get_type(id);
            columnTypes.add(type);
        }
        // System.out.println(columnTypes);
        Table result = new Table(columnNames, columnTypes);
        // System.out.println("????");
        for (Row row : _rows) {
            int flag = 1;
            if (conditions != null) {
                if (!Condition.test(conditions, row)) {
                    flag = 0;
                }
            }
            if (flag == 1) {
                String[] data = new String[columnNames.size()];
                for (int i = 0; i < columnNames.size(); i++) {
                    int id = findColumn(columnNames.get(i));
                    if (id == -1) {
                        throw error("column \"" + columnNames.get(i) + "\" does not exist.");
                    }
                    data[i] = row.get(id);
                }
                result.add(new Row(data));
            }
        }
        // FILL IN
        return result;
    }

    /**
     * Return a new Table whose columns are COLUMNNAMES, selected
     * from pairs of rows from this table and from TABLE2 that match
     * on all columns with identical names and satisfy CONDITIONS.
     */
    Table select(Table table2, List<String> columnNames,
            List<Condition> conditions) {
        // System.out.println("????");
        List<String> columnTypes = new ArrayList<String>();
        for (int i = 0; i < columnNames.size(); i++) {
            int id = findColumn(columnNames.get(i));
            if (id != -1) {
                String type = this.get_type(id);
                columnTypes.add(type);
            } else {
                id = table2.findColumn(columnNames.get(i));
                if (id == -1) {
                    throw error("column \"" + columnNames.get(i) + "\" does not exist.");
                } else {
                    String type = table2.get_type(id);
                    columnTypes.add(type);
                }
            }
        }
        Table result = new Table(columnNames, columnTypes);
        List<Column> column1 = new ArrayList<Column>();
        List<Column> column2 = new ArrayList<Column>();
        for (int i = 0; i < columns(); i++) {
            for (int j = 0; j < table2.columns(); j++) {
                if (getTitle(i).equals(table2.getTitle(j))) {
                    column1.add(new Column(getTitle(i), 0, this, table2));
                    column2.add(new Column(table2.getTitle(j), 1, this, table2));
                }
            }
        }

        for (Row row1 : _rows) {
            for (Row row2 : table2._rows) {
                int flag = 1;
                if (conditions != null) {
                    if (!Condition.test(conditions, row1, row2)) {
                        flag = 0;
                    }
                }
                if (flag == 1) {

                    if (!equijoin(column1, column2, row1, row2)) {
                        // System.out.println("????");
                        continue;
                    }
                    String[] data = new String[columnNames.size()];
                    for (int i = 0; i < columnNames.size(); i++) {
                        int id = findColumn(columnNames.get(i));
                        if (id != -1) {
                            data[i] = row1.get(id);
                        } else {
                            id = table2.findColumn(columnNames.get(i));
                            if (id == -1) {
                                throw error("column \"" + columnNames.get(i) + "\" does not exist.");
                            }
                            data[i] = row2.get(id);
                        }
                    }
                    Row tmp = new Row(data);
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    /**
     * Return a list from the original table, whose order is changed
     * by ORDER. Because _row uses hashset to store data, thus we cannot
     * directly change the order of a table.
     */
    List<Row> order(List<String> order) {
        List<Row> result = new ArrayList<Row>();
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> directions = new ArrayList<String>();
        for (int i = 0; i < order.size(); i++) {
            if (i % 2 == 0)
                columns.add(order.get(i));
            else
                directions.add(order.get(i));
        }
        int index = 0;

        ArrayList<Row> records = new ArrayList<Row>();
        String currentColumn = columns.get(index);
        String columnOrder = directions.get(index++);
        int columnIndex = findColumn(currentColumn);
        Map<Integer, String> map = new HashMap<>();

        int ind = 0;
        for (Row row : _rows) {
            map.put(ind++, row.get(columnIndex));
            records.add(row);
        }
        if (Objects.equals(columnOrder, "asc")) {
            map = map.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        } else {
            map = map.entrySet().stream().sorted((Map.Entry.<Integer, String>comparingByValue().reversed())).collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        for (int i : map.keySet())
            result.add(records.get(i));

        while (index != columns.size()) {
            currentColumn = columns.get(index);
            columnIndex = findColumn(currentColumn);
            columnOrder = directions.get(index++);

            int[] indexSet = new int[index - 1];
            for (int i = 0; i <= index - 2; i++) {
                indexSet[i] = findColumn(columns.get(i));
            }
            ArrayList<String[]> stringSet = new ArrayList<>();
            for (Row row : result) {
                String[] s = new String[index - 1];
                for (int j = 0; j <= index - 2; j++) {
                    s[j] = row.get(indexSet[j]);
                }
                stringSet.add(s);
            }
            ind = 0;
            while (ind != result.size()) {
                Row currentRow = result.remove(0);
                if (ind == result.size()) {
                    result.add(currentRow);
                    ind++;
                    continue;
                }
                if (!Arrays.equals(stringSet.get(ind), stringSet.get(ind + 1))) {
                    result.add(currentRow);
                    ind++;
                } else {
                    ArrayList<Row> sameValue = new ArrayList<Row>();
                    Map<Integer, String> m = new HashMap<>();
                    int localInd = 0;
                    String[] v = stringSet.get(ind);
                    sameValue.add(currentRow);
                    m.put(localInd++, currentRow.get(columnIndex));
                    ind++;
                    while ((ind != stringSet.size()) && (!result.isEmpty()) && (Arrays.equals(v, stringSet.get(ind)))) {
                        currentRow = result.remove(0);
                        sameValue.add(currentRow);
                        m.put(localInd++, currentRow.get(columnIndex));
                        ind++;
                    }
                    if (Objects.equals(columnOrder, "asc")) {
                        m = m.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors
                                .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                    } else {
                        m = m.entrySet().stream().sorted((Map.Entry.<Integer, String>comparingByValue().reversed()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                        LinkedHashMap::new));
                    }
                    for (int i : m.keySet())
                        result.add(sameValue.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Return true if the columns COMMON1 from ROW1 and COMMON2 from
     * ROW2 all have identical values. Assumes that COMMON1 and
     * COMMON2 have the same number of elements and the same names,
     * that the columns in COMMON1 apply to this table, those in
     * COMMON2 to another, and that ROW1 and ROW2 come, respectively,
     * from those tables.
     */
    private static boolean equijoin(List<Column> common1, List<Column> common2, Row row1, Row row2) {
        if (common1.size() == 0)
            return true;
        for (Column c1 : common1) {
            int flag = 0;
            for (Column c2 : common2) {
                if (c1.getName().equals(c2.getName())) {
                    flag = 1;
                    if (!(c1.getFrom(row1, row2).equals(c2.getFrom(row1, row2)))) {
                        return false;
                    }
                    break;
                }
            }
            if (flag == 0)
                return false;
        }

        return true; // REPLACE WITH SOLUTION
    }

    /** My rows. */
    private HashSet<Row> _rows = new HashSet<>();
    private String[] _column_titles;
    private String[] _column_types;
    // int
    // double
    // string
    private int _primary_key = -1;
    private HashSet<String> _primary_key_set = new HashSet<>();
}
