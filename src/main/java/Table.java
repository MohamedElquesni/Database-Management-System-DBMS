import java.util.*;
import java.util.Iterator;
public class Table {
    private String name;
    private ArrayList<Page> physicalPages;
    private ArrayList<String> pages;
    private ArrayList<String> columns = new ArrayList<>();
    private String primaryKey;
    //private static ArrayList<String> tableNames;
    private Hashtable<String,String> columnsDataTypes;
    private Hashtable<String, Integer> columnsIndex = new Hashtable<String, Integer>();
    private ArrayList<Integer> indexColumns;
    public Table(String tableName, String primaryKey, Hashtable<String, String> columnsDataTypes) {
        this.columnsDataTypes = columnsDataTypes;
        this.pages = new ArrayList<>();
        this.name = tableName;
        this.primaryKey = primaryKey;
        columnsIndex.put(primaryKey, 0);
        indexColumns = new ArrayList<>();
        for(int i=0;i<columnsDataTypes.size();i++){
            indexColumns.add(0);
        }


        for(Map.Entry<String, String> entry : columnsDataTypes.entrySet()){
            String key = entry.getKey();
//            System.out.println("the key is :"+key);
            columns.add(key);
            //System.out.println("key: "+ key);
            //TextFileWriter.metaWrite(name + "," + key + "," + columnsDataTypes.get(key) + "," + key.equals(primaryKey) + ",null,null");
        }
        Integer i = 1;
        for(String key : columnsDataTypes.keySet()){
            TextFileWriter.metaWrite(name + "," + key + "," + columnsDataTypes.get(key) + "," + key.equals(primaryKey) + ",null,null");
            if(!(key.equals(primaryKey)))
                columnsIndex.put(key, i++);
        }
    }

    public boolean isEmpty(){
        return pages.isEmpty();
    }

    public ArrayList<String> getPages() {
        return pages;
    }


    public ArrayList<String> getColumns() {
        return columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }




    public String getName() {
        return name;
    }
    //inserts a tuple in the table
    //using the binarySearchInTable method
    //assumes that t is a valid tuple for this table
    //and that no index is created on the table
    public void insertNew(Tuple t,ArrayList<String>coulumnNames) throws DBAppException {
        ArrayList<Comparable> keyArray;
        ArrayList<String>values;
        Page targetPage = binarySearchInTable(t);
        if(targetPage == null) {
            //targetPage is null means we need to create a new page
            Page newPage = new Page(this.pages.size() + 1, this.name);
            pages.add(newPage.fileName);
            newPage.insertPage(t);
            newPage.serialize();
            int pageNum = pages.size()-1;
            if (indexColumns.contains(1) && !(this.isEmpty())) {
                for (int j = 0; j < indexColumns.size(); j++) {
                    if (indexColumns.get(j) == 1) {
                       String col = coulumnNames.get(j)+".ser";
                        String toBeAdded = "P:"+pageNum+","+"R:"+1;
                        BTree currentBtree = BTree.deserialize(col);
                       currentBtree.insert((Comparable) t.tuple.get(j),toBeAdded);
                       currentBtree.serialize(col);
                    }

                }
        }
        }
        else {
            insertHelper2(t, targetPage);
            int tupleNum = targetPage.binarySearchInPage(t);
            int pageNum = searchForPage(t);
            if (indexColumns.contains(1) && !(this.isEmpty())) {
                for (int j = 0; j < indexColumns.size(); j++) {
                    if (indexColumns.get(j) == 1) {
                        String toBeAdded = "P:"+(pageNum)+","+"R:"+(tupleNum);
                        String col = coulumnNames.get(j)+".ser";
                        BTree currentBtree = BTree.deserialize(col);
                        currentBtree.insert((Comparable) t.tuple.get(j),toBeAdded);
                        currentBtree.serialize(col);
                    }
                }
            }
        }
    }


    //helper method that handles inserting in a full page and shifting
    //last tuple
    public void insertHelper2(Tuple t, Page currentPage) throws DBAppException {
        int index = pages.indexOf(currentPage.fileName);
        if (currentPage.isFull()) {
            currentPage.insertPage(t);
            Tuple lastTuple = currentPage.records.remove(currentPage.records.size() - 1);

            // if currentPage is the last page
            //create a new page and insert in it directly
            if(index == this.pages.size() - 1){
                //System.out.println(table);
                Page newPage = new Page(this.pages.size() + 1, this.name);
                newPage.insertPage(lastTuple);
                this.pages.add(newPage.fileName);
                newPage.serialize();
            }

            //else: get next page and call insertHealper2 again
            //with lastTuple as parameter because
            //nextPage might be full as well
            else {
                String nextPageName = pages.get(index + 1);
                Page nextPage = Page.deserialize(nextPageName);
                insertHelper2(lastTuple, nextPage);
            }
        }
        //currentPage is not full so insert directly
        else {
            currentPage.insertPage(t);
        }
        //don't forget to serialize currentPage
        currentPage.serialize();
    }




    //deletes tuple from table using binarySearchInTable method
    //must be given the type tuple
    //which means we must formulate a tuple using the info given
    //in the big method call
    //which is bedan bs 3ady
    public void deleteFromTable(Hashtable<String,Object>hash){
        boolean[] allConditions = new boolean[hash.size()];
        int index;
        SQLTerm[] arrSQLTerms = new SQLTerm[hash.size()];
        String[] strarrOperators = new String[hash.size() - 1];
        Arrays.fill(strarrOperators, "AND");
        int i = 0;
        for(String key: hash.keySet()){
            SQLTerm sqlTerm = new SQLTerm();
            sqlTerm._strColumnName = key;
            sqlTerm._objValue = hash.get(key);
            sqlTerm._strOperator = "=";
            arrSQLTerms[i++] = sqlTerm;
        }
        for(i = 0; i < pages.size(); i++ ){ // betemshy 3al pages
            Page currentPage = Page.deserialize(pages.get(i));
            for(int j = 0; j < currentPage.records.size(); j++){//betemshy 3al tuples
                allConditions = new boolean[hash.size()];
                Tuple currentTuple = currentPage.records.get(j);
                int k = 0;
                for(String key: hash.keySet()) { //betemshy 3al sqlTerm
                    allConditions[k] = isConditionTrue(arrSQLTerms[k], currentTuple);
                    k++;
                }
                if (isAllConditions(allConditions, strarrOperators)){
//                    System.out.println("edeeny");
                    int currentIndex = currentPage.binarySearchInPage(currentTuple);
                    currentPage.records.remove(currentIndex);
                    if(currentPage.records.isEmpty()){
                        int indexOfPage = currentPage.indexOfPage;
                        Page nextPage = Page.deserialize(pages.get(indexOfPage));
                        for(i = indexOfPage; i < pages.size(); i++){
                            System.out.println("dakhal");

                            nextPage.fileName = nextPage.tableName + "" + nextPage.indexOfPage + ".ser";
                            nextPage.indexOfPage = i;
                            nextPage.serialize();
                            if(i == pages.size() - 1){
                                break;
                            }
                            nextPage = Page.deserialize(pages.get(i+1));
                        }
                        pages.remove(indexOfPage - 1);
                    }
                    else {
                        currentPage.serialize();
                    }
                }
            }
        }

    }


    public boolean deleteFromTableUsingPK(Tuple t) {
        Page targetPage = binarySearchInTable(t);

        if(targetPage == null) {
            return false;
        }
        else {
            if(targetPage.records.contains(t))
                targetPage.records.remove(t);
            else return false;
            return true;
        }
    }



    //uses primary key to binary search in the table and returns the
    //target page
    //if this returns null:
    //in case of insert, we should create a new page and insert in it
    //in case of delete, we are sure that the element doesn't exist in the table
    public Page binarySearchInTable(Tuple t) {
        if (pages.isEmpty())
            return null;

        //variables that are used in binary search
        int start = 0;
        int end = pages.size() - 1;
        int mid;
        boolean found = false; //indicates if we found our target page

        //used to retrieve and store the current page
        //we are looking at
        String currentPageFileName;
        Page currentPage;

        //used to retrieve and store the next page
        String nextPageFileName;
        Page nextPage;

        //stores tuples of pages so we compare them with t
        Tuple firstTuple;
        Tuple lastTuple;

        while(!found) {
            mid = start + (end - start) / 2;
//            System.out.println("start:" + start + " , mid:" + mid + " ,end: " + end);
            //we encounter this case when we end up with one page after the search
            //or if we have only one page in the table
            //both cases that page is at index 'start' so we return it
            //in case of insert, we should immediately insert in this returned page
            //in case of delete, the tuple is either in this page or not in the table so
            //we perform that check in delete method
            if (start >= end) {
                found = true;
                currentPageFileName = pages.get(start);
                currentPage = Page.deserialize(currentPageFileName);
                return currentPage;
            }
            //normal case where end > start
            //we grab the middle page and perform our checks
            else {
                currentPageFileName = pages.get(mid);
                currentPage = Page.deserialize(currentPageFileName);

                lastTuple = currentPage.records.get(currentPage.records.size() - 1);

                //case 1: t is greater than lastTuple
                //makany fel page de aw ba3d
                if (t.compareTo(lastTuple) >= 0) {

                    //case 1.1: currentPage is the last page
                    //in case of delete, this means the element doesn't exist in the table
                    if(mid == pages.size() - 1) {

                        //case 1.1.1: the page is full
                        //in case of insert, we should create a new page and insert
                        //in it
                        //so we return null and handle the cases in insert and delete method
                        if(currentPage.isFull()) {
                            return null;
                        }

                        //case 1.1.2: page is not full
                        //in case of insert, we should insert the elment here
                        //so we return the page
                        //however, in case of delete we return a page when we know the element is not there
                        //this will not be a problem because delete method will search in the page
                        //itself and not find it and return null/false
                        else {
                            return currentPage;
                        }
                    }

                    //case 1.2: currentPage is not the last page
                    //in case of delete, we are sure that target is to the right (if exists)
                    else {
                        //we check isFull

                        //case 1.2.1: page is full
                        //we discard the left half of the search
                        //because the target page is definitely to the right
                        if(currentPage.isFull()) {
                            start = mid + 1;
                        }

                        //case 1.2.2: page is not full
                        //tricky case :)
                        //because the target page might be this one in case of insert
                        //but we are not sure
                        //so we grab the first tuple of the next page
                        //and compare
                        else {
                            //retrieve next page
                            nextPageFileName = pages.get(mid + 1);
                            nextPage = Page.deserialize(nextPageFileName);

                            firstTuple = nextPage.records.get(0);

                            //case 1.2.2.1: t is greater than firstTuple
                            //we discard the left half of the search
                            //including currentPage but not nextPage
                            //because we are sure that currentPage is not the target
                            if(t.compareTo(firstTuple) > 0) {
                                start = mid + 1;
                            }
                            //case 1.2.2.2: firstTuple is greater than t
                            //we return currentPage
                            //we are sure it is the target and t would be the last element in it in case of insert
                            else {
                                return currentPage;
                            }
                        }
                    }
                }
                //case 2: lastTuple is greater than t
                //makany fel page de aw abl
                else {

                    //case 2.1: currentPage is the first page
                    //we return currentPage because
                    //in case of insert, we are sure we should insert here
                    //in case of delete, we are sure that element is either here
                    //or doesn't exist
                    //no need for cases 2.1.1 and 2.1.2 because they will be handled by insert method
                    if(mid == 0) {
                        return currentPage;
                    }
                    //case 2.2: currentPage is not the first page
                    //makany hena aw abl
                    //to be sure we check firstTuple of currentPage and compare
                    else {
                        firstTuple = currentPage.records.get(0);

                        //case 2.2.1: t is greater than firstTuple
                        //we return currentPage
                        //because t is between lastTuple and firstTuple
                        //so we are sure that currentPage is the target
                        if(t.compareTo(firstTuple) > 0) {
                            return currentPage;
                        }
                        //case 2.2.2: firstTuple is greater than t
                        //we discard the right half of the search
                        //because t is smaller than the whole of currentPage
                        else {
                            end = mid - 1;
                        }
                    }

                }
            }
        }
        return null;
    }
    public int searchForPage(Tuple targetTuple){
        if (pages.isEmpty())
            return -1;

        int start = 0;
        int end = pages.size() - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;
            String currentPageFileName = pages.get(mid);
            Page currentPage = Page.deserialize(currentPageFileName);

            // Use binary search to check if the current page contains the tuple
            if (currentPage.containsBinarySearch(targetTuple)) {
                return mid; // The page containing the tuple is found
            }

            Tuple lastTuple = currentPage.records.get(currentPage.records.size() - 1);
            if (targetTuple.compareTo(lastTuple) > 0) {
                // If the tuple is greater than the last tuple in the page, search right
                start = mid + 1;
            } else {
                // If the tuple is less, search left
                end = mid - 1;
            }
        }
        return -1;
    }

    public void updateInsideTable(String strClusteringKeyValue, Hashtable<String,Object> htblColNameValue )
            throws DBAppException {

        String typeOfPrimaryKey = columnsDataTypes.get(primaryKey);
        int identifier = 0; //if zero then string, if 1 then int, if 2 then double
        switch (typeOfPrimaryKey) {
            case "java.lang.Integer":
                identifier = 1;
                break;
            case "java.lang.Double":
                identifier = 2;
                break;
            default:
                break;
        }
        Page pageOfTargetTuple;
        Integer intActualPrimaryKey;
        Double doubleActualPrimaryKey;
        String strActualPrimaryKey;
        Tuple tupleToSearchWith;
        if (identifier == 1) {
            intActualPrimaryKey = Integer.parseInt(strClusteringKeyValue);
            tupleToSearchWith = new Tuple(); // this is to use in binarySearch
            tupleToSearchWith.tuple.add(intActualPrimaryKey);
             pageOfTargetTuple = binarySearchInTable(tupleToSearchWith);// we got the target page
            if (pageOfTargetTuple == null)
                throw new DBAppException("Tuple is not found.");
        }
        else if (identifier == 2) {
            doubleActualPrimaryKey = Double.parseDouble(strClusteringKeyValue);
            tupleToSearchWith = new Tuple(); // this is to use in binarySearch
            tupleToSearchWith.tuple.add(doubleActualPrimaryKey);
             pageOfTargetTuple = binarySearchInTable(tupleToSearchWith);// we got the target page
            if (pageOfTargetTuple == null)
                throw new DBAppException("Tuple is not found.");
        }

        else {
            strActualPrimaryKey = strClusteringKeyValue;
            tupleToSearchWith = new Tuple(); // this is to use in binarySearch
            tupleToSearchWith.tuple.add(strActualPrimaryKey);
             pageOfTargetTuple = binarySearchInTable(tupleToSearchWith);// we got the target page
            if (pageOfTargetTuple == null)
                throw new DBAppException("Tuple is not found.");
        }
        //System.out.println(pageOfTargetTuple);

        //Tuple targetTuple = pageOfTargetTuple.binarySearchInPage(tupleToSearchWith); lama kona fakreen en el target tuple mohem
        int indexOfTuple = pageOfTargetTuple.binarySearchInPage(tupleToSearchWith);
        //Tuple targetTuple = pageOfTargetTuple.binarySearchInPage((Comparable) tupleToSearchWith.tuple.get(0));
        //System.out.println(pageOfTargetTuple.records.get(indexOfTuple));

        for(String key: htblColNameValue.keySet()){
            if(key.equals(primaryKey)){
                throw new DBAppException("Cannot change primary key.");
            }
            int index2 = columnsIndex.get(key);
            Object newValue = htblColNameValue.get(key);
            System.out.println(pageOfTargetTuple.records.get(indexOfTuple));
            pageOfTargetTuple.records.get(indexOfTuple).tuple.set(index2,newValue);
            pageOfTargetTuple.serialize();
            System.out.println(pageOfTargetTuple.records.get(indexOfTuple));
            //System.out.println(pageOfTargetTuple);
            //tupleToSearchWith.tuple.set(index, newValue);//bala7 oksemballah kona negeeb el index beta3o fy el page w khalas kosom el araf
        }

    }

    public static int compareTo(int index, Tuple t1, Object data){
        if (t1.tuple.get(index) instanceof java.lang.Integer){
            if ((Integer)t1.tuple.get(index) < (Integer) data)
                return -1;
            else if((Integer) t1.tuple.get(index) == (Integer)data)
                return 0;
            else
                return 1;
        }
        else if(t1.tuple.get(index) instanceof java.lang.Double){
            if ((Double)t1.tuple.get(index) < (Double) data)
                return -1;
            else if((Double)t1.tuple.get(index) == (Double) data)
                return 0;
            else
                return 1;
        }
        else{
            return ((String)t1.tuple.get(index)).compareTo((String) data);
        }
    }

    public void creatIndexOnTable(String strColName){
        int indexColume = columnsIndex.get(strColName);
        System.out.println(indexColumns);
            indexColumns.set(indexColume, 1);

        BTree btree = new BTree<>();
        String fileNameBtree = strColName+".ser";
        for(int i=0;i<pages.size();i++){
            Page currentPage = Page.deserialize(pages.get(i));
            for(int j=0;j<currentPage.records.size();j++){
                Tuple currentTuple = currentPage.records.get(j);
                Object key = currentTuple.tuple.get(indexColume);
                String value = "P:"+(i+1)+","+"R:"+(j+1);
                btree.insert((Comparable) key,value);

            }
        }
       btree.serialize(fileNameBtree);//serialize the b+tree to use it when needed
    }






    public String toString(){
        String s = "Table " + this.name + " : ";
        //System.out.println("page size: " + this.pages.size());
        for(int i = 0; i < this.pages.size() ; i++){
            s += Page.deserialize(this.pages.get(i)).toString() + "\n";
        }
        return s;
    }

    public static void displayVector(Iterator v){
        while(v.hasNext()){
            System.out.println(v.next().toString());
        }
    }


    public static void main(String[] args) throws DBAppException{
        Hashtable<String, String> htbl = new Hashtable<>();
        htbl.put("ID","java.lang.Integer");
        htbl.put("Name","java.lang.String");
        htbl.put("Age","java.lang.Integer");
        Table table = new Table("Student.ser", "ID",htbl);
        Tuple t1 = new Tuple();
        Tuple t2 = new Tuple();
        Tuple t3 = new Tuple();
        Tuple t4 = new Tuple();
        Tuple t5 = new Tuple();
        Tuple t6 = new Tuple();
        Tuple t7 = new Tuple();
        Tuple t8 = new Tuple();
        ArrayList<String> columnsNames = new ArrayList<>();
        columnsNames.add("ID");
        columnsNames.add("Name");
        columnsNames.add("Age");
        t1.tuple.add(1);
        t1.tuple.add("mazen");
        t1.tuple.add(20);
        t2.tuple.add(2);
        t2.tuple.add("mohamed");
        t2.tuple.add(9);
        t3.tuple.add(3);
        t3.tuple.add("maran");
        t3.tuple.add(18);
        t4.tuple.add(4);
        t4.tuple.add("seif");
        t4.tuple.add(21);
        t5.tuple.add(5);
        t5.tuple.add("sika");
        t5.tuple.add(23);
        t6.tuple.add(6);
        t6.tuple.add("Sherif");
        t6.tuple.add(25);
        t7.tuple.add(7);
        t7.tuple.add("Wael");
        t7.tuple.add(25);
        t8.tuple.add(8);
        t8.tuple.add("Karam");
        t8.tuple.add(11);
        table.insertNew(t1,columnsNames);
        table.insertNew(t2,columnsNames);
        table.insertNew(t3,columnsNames);
        table.insertNew(t4,columnsNames);
        table.insertNew(t5,columnsNames);
        table.insertNew(t6,columnsNames);
        table.insertNew(t7,columnsNames);
        table.insertNew(t8,columnsNames);
        table.creatIndexOnTable("Age");
        BTree<?, ?> test = BTree.deserialize("Age.ser");
        System.out.println(test);


    }











   public static  Iterator selectInsideTable(Table table,SQLTerm[] arrSQLTerms,String[]  strarrOperators) {
        ArrayList<Table> tablesMatched = new ArrayList<>();
        for(int i=0;i<arrSQLTerms.length;i++){
            for(int j=0;j<table.pages.size();j++){
                Page currentPage = table.physicalPages.get(j);
                for(int k=0;k<currentPage.records.size();k++){
                    Tuple currentTuple = currentPage.records.get(k);
                    int index = table.columnsIndex.get(arrSQLTerms[i]._strColumnName);
                    if(currentTuple.tuple.get(index).equals(arrSQLTerms[i]._objValue)){

                    }

                }
            }
        }
        return null;
   }
    public boolean isConditionTrue(SQLTerm term, Tuple t){
        int indexOfColumnToCheck = this.columnsIndex.get(term._strColumnName);
        switch(term._strOperator){
            case "=" : return t.tuple.get(indexOfColumnToCheck).equals(term._objValue);
            case "!=" : return !(t.tuple.get(indexOfColumnToCheck).equals(term._objValue));
            case ">" : return compareTo(indexOfColumnToCheck, t, term._objValue) == 1;
            case ">=" : return compareTo(indexOfColumnToCheck, t, term._objValue) == 1 || compareTo(indexOfColumnToCheck, t, term._objValue) == 0;
            case "<" : return compareTo(indexOfColumnToCheck, t, term._objValue) == -1;
            default : return compareTo(indexOfColumnToCheck, t, term._objValue) == -1 || compareTo(indexOfColumnToCheck, t, term._objValue) == 0;
        }

    }

    public Iterator selectNew(SQLTerm[] arrSQLTerms,String[]  strarrOperators){
        Vector<Tuple> resultSet = new Vector<>();
        boolean[] allConditions = new boolean[arrSQLTerms.length];
        int index;
        for(int i = 0; i < pages.size(); i++ ){ // betemshy 3al pages
            Page currentPage = Page.deserialize(pages.get(i));
            for(int j = 0; j < currentPage.records.size(); j++){//betemshy 3al tuples
                allConditions = new boolean[arrSQLTerms.length];
                Tuple currentTuple = currentPage.records.get(j);
                for(int k = 0; k < arrSQLTerms.length; k++) { //betemshy 3al sqlTerm
                    allConditions[k] = isConditionTrue(arrSQLTerms[k], currentTuple);
//                    System.out.println("maatedeneesh");
                }
                if (isAllConditions(allConditions, strarrOperators)){
//                    System.out.println("edeeny");
                    resultSet.add(currentTuple);
                }
            }
        }

        return resultSet.iterator();
    }

    public static boolean isAllConditions(boolean[] allConditions, String[]  strarrOperators){
        boolean result = allConditions[0];
        for(int i = 1; i < allConditions.length; i++){
            switch(strarrOperators[i-1]){
                case "AND" : result = result && allConditions[i];break;
                case "OR" : result = result || allConditions[i];break;
                default: result = result != allConditions[i];break;
            }
        }
        return result;
    }












}