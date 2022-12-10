// This is a SUGGESTED skeleton for a class that describes a single
// Condition (such as CCN = '99776').  You can throw this away if you
// want,  but it is a good idea to try to understand it first.
// Our solution changes or adds about 30 lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

import java.util.List;
import static db61b.Utils.*;
/** Represents a single 'where' condition in a 'select' command.
 *  @author */
class Condition {

    /** A Condition representing COL1 RELATION COL2, where COL1 and COL2
     *  are column designators. and RELATION is one of the
     *  strings "<", ">", "<=", ">=", "=", or "!=". */
    Condition(Column col1, String relation, Column col2) {
        _col1=col1;
        _relation=relation;
        _col2=col2;
        // YOUR CODE HERE
    }

    /** A Condition representing COL1 RELATION 'VAL2', where COL1 is
     *  a column designator, VAL2 is a literal value (without the
     *  quotes), and RELATION is one of the strings "<", ">", "<=",
     *  ">=", "=", or "!=".
     */
    Condition(Column col1, String relation, String val2) {
        this(col1, relation, (Column) null);
        _val2 = val2;
    }

    /** Assuming that ROWS are rows from the respective tables from which
     *  my columns are selected, returns the result of performing the test I
     *  denote. */
    boolean teststr(Row... rows){
            if(_col2==null){
                //System.out.println(_val2);
                switch(_relation){
                    case "like":
                    try{
                        if(!(_col1.getFrom(rows).matches(_val2))){return false;}
                    }catch(java.util.regex.PatternSyntaxException e){
                        throw error("Syntax error in LikeStatement");
                    }
                    break;
                    case "=":
                    if(!(_col1.getFrom(rows).equals(_val2))){return false;}
                    break;

                    case "!=":
                    if(_col1.getFrom(rows).equals(_val2)){return false;}
                    break;

                    case ">=":// try to transfer to integer
                    if(_col1.getFrom(rows).compareTo(_val2)<0)return false;
                    break;

                    case "<=":
                    if(_col1.getFrom(rows).compareTo(_val2)>0)return false;
                    break;

                    case ">":
                    if(_col1.getFrom(rows).compareTo(_val2)<=0)return false;
                    break;

                    case "<":
                    if(_col1.getFrom(rows).compareTo(_val2)>=0)return false;
                    break;
                }
            }

            else{
                String val2=_col2.getFrom(rows);
                switch(_relation){
                    case "=":
                    if(!(_col1.getFrom(rows).equals(val2))){return false;}
                    break;

                    case "!=":
                    if(_col1.getFrom(rows).equals(val2)){return false;}
                    break;

                    case ">=":// try to transfer to integer
                    if(_col1.getFrom(rows).compareTo(val2)<0)return false;
                    break;

                    case "<=":
                    if(_col1.getFrom(rows).compareTo(val2)>0)return false;
                    break;

                    case ">":
                    if(_col1.getFrom(rows).compareTo(val2)<=0)return false;
                    break;

                    case "<":
                    if(_col1.getFrom(rows).compareTo(val2)>=0)return false;
                    break;
                }
            }
        return true;
    }
    boolean testint(Row... rows){
        if(_col2==null){
            int val2=Integer.parseInt(_val2);
            int val1=Integer.parseInt(_col1.getFrom(rows));
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":// try to transfer to integer
                if(val1<val2)return false;
                break;

                case "<=":
                if(val1>val2)return false;
                break;

                case ">":
                if(val1<=val2)return false;
                break;

                case "<":
                if(val1>=val2)return false;
                break;
            }
        }

        else{
            int val2=Integer.parseInt(_col2.getFrom(rows));
            int val1=Integer.parseInt(_col1.getFrom(rows));
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":// try to transfer to integer
                if(val1<val2)return false;
                break;

                case "<=":
                if(val1>val2)return false;
                break;

                case ">":
                if(val1<=val2)return false;
                break;

                case "<":
                if(val1>=val2)return false;
                break;
            }
        }
        return true;
    }
    boolean testdouble(Row... rows){
       if(_col2==null){
            double val2=Double.parseDouble(_val2);
            double val1=Double.parseDouble(_col1.getFrom(rows));
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":// try to transfer to integer
                if(val1<val2)return false;
                break;

                case "<=":
                if(val1>val2)return false;
                break;

                case ">":
                if(val1<=val2)return false;
                break;

                case "<":
                if(val1>=val2)return false;
                break;
            }
        }

        else{
            double val2=Double.parseDouble(_col2.getFrom(rows));
            double val1=Double.parseDouble(_col1.getFrom(rows));
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":// try to transfer to integer
                if(val1<val2)return false;
                break;

                case "<=":
                if(val1>val2)return false;
                break;

                case ">":
                if(val1<=val2)return false;
                break;

                case "<":
                if(val1>=val2)return false;
                break;
            }
        }
        return true;
    }
    boolean test(int type,Row... rows) {
       // System.out.println(type);
        if(type==0){
            return teststr(rows);
        }
        else if(type==1){
            return testint(rows);
        }
        else if(type==2){
            return testdouble(rows);
        }
        else{
            System.out.printf("unknown type %d%n", type);
            return false;
        }
        // REPLACE WITH SOLUTION
    }

    /** Return true iff ROWS satisfies all CONDITIONS. */
    static boolean test(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
            //System.out.println(cond._relation);
            try{
                switch(cond._col1.get_type()){
                    case "int":
                    if (!cond.test(1,rows)) {
                        return false;
                    }
                    break;
                    case "string":
                    if (!cond.test(0,rows)) {
                        return false;
                    }
                    break;
                    case "double":
                    if (!cond.test(2,rows)) {
                        return false;
                    }
                    break;
                    default:
                    throw error("unknown type %d%n", cond._col1.get_type());
                }
            }
            catch(DBException e){
                throw error("%s", e.getMessage());
            }
        }
        return true;
    }

    /** The operands of this condition.  _col2 is null if the second operand
     *  is a literal. */
    private Column _col1, _col2;
    /** Second operand, if literal (otherwise null). */
    private String _val2, _relation;
    // ADD ADDITIONAL FIELDS HERE
}
