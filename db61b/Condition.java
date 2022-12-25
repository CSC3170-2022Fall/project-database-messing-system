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
    boolean testStr(Row... rows){
            if(_col2==null){
                if(_val2.equals("NULL")) {
                    if(_relation.equals("=")){
                        if(_col1.getFrom(rows).equals("NULL"))return true;
                        return false;
                    }
                    else if(_relation.equals("!=")){
                        if(_col1.getFrom(rows).equals("NULL"))return false;
                        return true;
                    }
                    else return false;
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                switch(_relation){
                    case "like":
                    try{
                        if(!(_col1.getFrom(rows).matches(_val2))){return false;}
                    }catch(java.util.regex.PatternSyntaxException e){
                        throw error("Syntax Error: invalid expression in LIKE clause.");
                    }
                    break;
                    case "=":
                    if(!(_col1.getFrom(rows).equals(_val2))){return false;}
                    break;

                    case "!=":
                    if(_col1.getFrom(rows).equals(_val2)){return false;}
                    break;

                    case ">=":
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
                if(_val2.equals("NULL")) {
                   return false;
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                switch(_relation){
                    case "=":
                    if(!(_col1.getFrom(rows).equals(val2))){return false;}
                    break;

                    case "!=":
                    if(_col1.getFrom(rows).equals(val2)){return false;}
                    break;

                    case ">=":
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
    boolean testInt(Row... rows){
        if(_col2==null){
            int val1,val2;
            try{
                val2=Integer.parseInt(_val2);
                val1=Integer.parseInt(_col1.getFrom(rows));
            }
            catch(DBException e){
                
                if(_val2.equals("NULL")) {
                    if(_relation.equals("=")){
                        if(_col1.getFrom(rows).equals("NULL"))return true;
                        return false;
                    }
                    else if(_relation.equals("!=")){
                        if(_col1.getFrom(rows).equals("NULL"))return false;
                        return true;
                    }
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                throw error("%s", e.getMessage());
            }
            catch (java.lang.NumberFormatException e) {
                if(_val2.equals("NULL")) {
                    if(_relation.equals("=")){
                        if(_col1.getFrom(rows).equals("NULL"))return true;
                        return false;
                    }
                    else if(_relation.equals("!=")){
                        if(_col1.getFrom(rows).equals("NULL"))return false;
                        return true;
                    }
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                throw error("Format Error: cannot convert non-Integer argument(s) into Integer in the contidion.");
            }
            switch(_relation){
                case "like":
                throw error("Syntax Error: cannot apply LIKE to Integer.");

                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":
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
            int val2;
            int val1;
            try{
                val2=Integer.parseInt(_col2.getFrom(rows));
                val1=Integer.parseInt(_col1.getFrom(rows));
            }
            catch(DBException e){
                if(_col1.getFrom(rows).equals("NULL")){
                   return false;
                }
                if(_col2.getFrom(rows).equals("NULL")) return false;
                throw error("%s", e.getMessage());
            }
            catch (java.lang.NumberFormatException e) {
                if(_col1.getFrom(rows).equals("NULL")){ 
                    return false;
                }
                if(_col2.getFrom(rows).equals("NULL")) return false;
                throw error("Format Error: cannot convert non-Integer argument(s) into Integer in the contidion.");
            }
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":
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
    boolean testDouble(Row... rows){
       if(_col2==null){
            double val1,val2;
            try{
                val2=Double.parseDouble(_val2);
                val1=Double.parseDouble(_col1.getFrom(rows));
            }
            catch(DBException e){
                if(_val2.equals("NULL")) {
                    if(_relation.equals("=")){
                        if(_col1.getFrom(rows).equals("NULL"))return true;
                        return false;
                    }
                    else if(_relation.equals("!=")){
                        if(_col1.getFrom(rows).equals("NULL"))return false;
                        return true;
                    }
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                throw error("%s", e.getMessage());
            }
            catch (java.lang.NumberFormatException e) {
                if(_val2.equals("NULL")) {
                    if(_relation.equals("=")){
                        if(_col1.getFrom(rows).equals("NULL"))return true;
                        return false;
                    }
                    else if(_relation.equals("!=")){
                        if(_col1.getFrom(rows).equals("NULL"))return false;
                        return true;
                    }
                }
                if(_col1.getFrom(rows).equals("NULL"))return false;
                throw error("Format Error: cannot convert non-Double argument(s) into Double in the contidion.");
            }
            switch(_relation){
                case "like":
                throw error("Syntax Error: cannot apply LIKE to Double.");
                
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":
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
            double val1,val2;
            try{
                val2=Double.parseDouble(_col2.getFrom(rows));
                val1=Double.parseDouble(_col1.getFrom(rows));
            }
            catch(DBException e){
                if(_col1.getFrom(rows).equals("NULL")){ 
                    return false;
                }
                if(_col2.getFrom(rows).equals("NULL")) return false;
                throw error("%s", e.getMessage());
            }
            catch (java.lang.NumberFormatException e) {
                if(_col1.getFrom(rows).equals("NULL")){ 
                    return false;
                }
                if(_col2.getFrom(rows).equals("NULL")) return false;
                throw error("Format Error: cannot convert non-Double argument(s) into Double in the contidion.");
            }
            switch(_relation){
                case "=":
                if(val1!=val2){return false;}
                break;

                case "!=":
                if(val1==val2){return false;}
                break;

                case ">=":
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
       try{
            if(type==0){
                return testStr(rows);
            }
            else if(type==1){
                return testInt(rows);
            }
            else if(type==2){
                return testDouble(rows);
            }
            else{
                System.out.printf("Value Mismatch: type %d not found.%n", type);
                return false;
            }
        }
        catch(DBException e){
            throw error("%s", e.getMessage());
        }
    }

    /** Return true iff ROWS satisfies all CONDITIONS. */
    static boolean test(List<Condition> conditions, Row... rows) {
        for (Condition cond : conditions) {
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
                    throw error("Value Mismatch: type %s not found.%n", cond._col1.get_type());
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
}
