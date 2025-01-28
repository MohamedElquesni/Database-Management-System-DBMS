
/** * @author Wael Abouelsaadat */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;


public class DBApp {
    Hashtable<String, Table> tables = new Hashtable<>();


    public DBApp( ){

    }

    // this does whatever initialization you would like
    // or leave it empty if there is no code you want to
    // execute at application startup
    public void init( ){


    }


    // following method creates one table only
    // strClusteringKeyColumn is the name of the column that will be the primary
    // key and the clustering column as well. The data type of that column will
    // be passed in htblColNameType
    // htblColNameValue will have the column name as key and the data
    // type as value
    public void createTable(String strTableName,
                            String strClusteringKeyColumn,
                            Hashtable<String,String> htblColNameType) throws DBAppException{
        if (tables.containsKey(strTableName))
            throw new DBAppException("This table name already exists.");
        if (htblColNameType.get(strClusteringKeyColumn) == null)
            throw new DBAppException("Clustering key doesn't exist in hashtable.");

        tables.put(strTableName, new Table(strTableName, strClusteringKeyColumn, htblColNameType));


    }


    // following method creates a B+tree index
    public void createIndex(String   strTableName,
                            String   strColName,
                            String   strIndexName) throws DBAppException{


        Table tableToUpdateIn = tables.get(strTableName);
        if(tableToUpdateIn.isEmpty()){
            throw new DBAppException("Table is Empty.");
        }
        tableToUpdateIn.creatIndexOnTable(strColName);
    }


    // following method inserts one row only.
    // htblColNameValue must include a value for the primary key
    public void insertIntoTable(String strTableName,
                                Hashtable<String,Object>  htblColNameValue) throws DBAppException{

        //use binary search for pages then use binary search inside the page to find the index to insert in
        if (!tables.containsKey(strTableName))
            throw new DBAppException("Table does not exist.");

        Table targetTable = tables.get(strTableName);
        Tuple newTuple = new Tuple();
        String primary = targetTable.getPrimaryKey();

        newTuple.tuple.add(htblColNameValue.get(primary));
ArrayList<String>coulumnNames = new ArrayList<>();
        for(String key : htblColNameValue.keySet()){
            Object value = htblColNameValue.get(key);
            coulumnNames.add(key);
            if (!(key.equals(primary)))
                newTuple.tuple.add(value);
        }
        targetTable.insertNew(newTuple,coulumnNames);
    }


    // following method updates one row only
    // htblColNameValue holds the key and new value
    // htblColNameValue will not include clustering key as column name
    // strClusteringKeyValue is the value to look for to find the row to update.
    public void updateTable(String strTableName,
                            String strClusteringKeyValue,
                            Hashtable<String,Object> htblColNameValue   )  throws DBAppException{

        Table tableToUpdateIn = tables.get(strTableName);
        if(tableToUpdateIn.isEmpty()){
            throw new DBAppException("Table is Empty.");
        }
        tableToUpdateIn.updateInsideTable(strClusteringKeyValue, htblColNameValue);


    }


    // following method could be used to delete one or more rows.
    // htblColNameValue holds the key and value. This will be used in search
    // to identify which rows/tuples to delete.
    // htblColNameValue enteries are ANDED together
    public void deleteFromTable(String strTableName,
                                Hashtable<String,Object> htblColNameValue) throws DBAppException{

        Table tableToSelectFrom = tables.get(strTableName);
        if(tableToSelectFrom.isEmpty()){
            throw new DBAppException("Table is Empty.");
        }
        tableToSelectFrom.deleteFromTable( htblColNameValue);
    }


    public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
                                    String[]  strarrOperators) throws DBAppException{
        Table tableToSelectFrom = tables.get(arrSQLTerms[0]._strTableName);
        if(tableToSelectFrom.isEmpty()){
            throw new DBAppException("Table is Empty.");
        }
        return tableToSelectFrom.selectNew( arrSQLTerms,strarrOperators);

    }


    /*public static void main( String[] args ){

        try{
            String strTableName = "Student.ser";
            DBApp	dbApp = new DBApp( );

            Hashtable htblColNameType = new Hashtable( );
            htblColNameType.put("id", "java.lang.Integer");
            htblColNameType.put("name", "java.lang.String");
            htblColNameType.put("gpa", "java.lang.double");
            dbApp.createTable( strTableName, "id", htblColNameType );
            dbApp.createIndex( strTableName, "gpa", "gpaIndex" );

            Hashtable htblColNameValue = new Hashtable( );
            htblColNameValue.put("id", new Integer( 2343432 ));
            htblColNameValue.put("name", new String("Ahmed Noor" ) );
            htblColNameValue.put("gpa", new Double( 0.95 ) );
            dbApp.insertIntoTable( strTableName , htblColNameValue );

            htblColNameValue.clear( );
            htblColNameValue.put("id", new Integer( 453455 ));
            htblColNameValue.put("name", new String("Ahmed Noor" ) );
            htblColNameValue.put("gpa", new Double( 0.95 ) );
            dbApp.insertIntoTable( strTableName , htblColNameValue );

            htblColNameValue.clear( );
            htblColNameValue.put("id", new Integer( 5674567 ));
            htblColNameValue.put("name", new String("Dalia Noor" ) );
            htblColNameValue.put("gpa", new Double( 1.25 ) );
            dbApp.insertIntoTable( strTableName , htblColNameValue );

            htblColNameValue.clear( );
            htblColNameValue.put("id", new Integer( 23498 ));
            htblColNameValue.put("name", new String("John Noor" ) );
            htblColNameValue.put("gpa", new Double( 1.5 ) );
            dbApp.insertIntoTable( strTableName , htblColNameValue );

            htblColNameValue.clear( );
            htblColNameValue.put("id", new Integer( 78452 ));
            htblColNameValue.put("name", new String("Zaky Noor" ) );
            htblColNameValue.put("gpa", new Double( 0.88 ) );
            dbApp.insertIntoTable( strTableName , htblColNameValue );


            SQLTerm[] arrSQLTerms;
            arrSQLTerms = new SQLTerm[2];
            arrSQLTerms[0]._strTableName =  "Student.ser";
            arrSQLTerms[0]._strColumnName=  "name";
            arrSQLTerms[0]._strOperator  =  "=";
            arrSQLTerms[0]._objValue     =  "John Noor";

            arrSQLTerms[1]._strTableName =  "Student.ser";
            arrSQLTerms[1]._strColumnName=  "gpa";
            arrSQLTerms[1]._strOperator  =  "=";
            arrSQLTerms[1]._objValue     =  new Double( 1.5 );

            String[]strarrOperators = new String[1];
            strarrOperators[0] = "OR";
            // select * from Student.ser where name = "John Noor" or gpa = 1.5;
            Iterator resultSet = dbApp.selectFromTable(arrSQLTerms , strarrOperators);
        }
        catch(Exception exp){
            exp.printStackTrace( );
        }
    }*/

}