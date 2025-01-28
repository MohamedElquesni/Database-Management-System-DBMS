import java.io.*;
import java.util.*;

public class Page implements java.io.Serializable{

        String fileName;
        String tableName;
        Vector<Tuple> records;
        int indexOfPage;
        int maxNumOfRows;

        public Page(int i, String tableName)
        {
                this.records = new Vector<>();
                this.indexOfPage = i;
                this.tableName = tableName;
                fileName = tableName + i + ".ser";
                serialize();
                this.maxNumOfRows = 2; //ConfigReader.configReaderInt("MaximumRowsCountinPage");  //max n for a page
        }
        public void remove(Tuple t){
                records.remove(t);
        }

        public void insertPage(Tuple t) throws DBAppException{
                if(records.contains(t)) {
                        throw new DBAppException("Cannot insert this tuple because it already exists in table " + this.tableName);
                }
                else {
                        records.add(t);
                        records.sort((Tuple::compareTo));
                }
        }

        public boolean isFull()    //returns if the page is full
        {
                return maxNumOfRows <= records.size();
        }

        public void serialize()
        {
                try
                {
                        FileOutputStream file = new FileOutputStream(fileName);
                        ObjectOutputStream out = new ObjectOutputStream(file);

                        out.writeObject(this);

                        out.close();
                        file.close();
                }
                catch(IOException ex)
                {
                        System.out.println("IOException is caught");
                }
        }

        public void setIndexOfPage(int indexOfPage) {
                this.indexOfPage = indexOfPage;
        }

        public String toString(){
                String s = "Page" + this.indexOfPage + " : \n";
//                System.out.println(indexOfPage);
                for(int i = 0; i < records.size() ; i++){
                        s += records.get(i).toString() + "\n";

                }
                return s;
        }

        public static Page deserialize(String fileName)
        {
                try
                {
                        FileInputStream file = new FileInputStream(fileName);
                        ObjectInputStream in = new ObjectInputStream(file);

                        Page deserializedPage = (Page) in.readObject();

                        in.close();
                        file.close();

                        return deserializedPage;
                }

                catch(IOException ex)
                {
                        System.out.println("IOException is caught");
                }
                catch(ClassNotFoundException ex)
                {
                        System.out.println("ClassNotFoundException is caught");
                }

                return null;
        }
        public boolean containsBinarySearch(Tuple t) {
                int low = 0;
                int high = records.size() - 1;
                while (low <= high) {
                        int mid = low + (high - low) / 2;
                        Tuple midVal = records.get(mid);
                        int cmp = midVal.compareTo(t);


                        if (cmp < 0) {
                                low = mid + 1;
                        } else if (cmp > 0) {

                                high = mid - 1;
                        } else {

                                return true; // Match found
                        }
                }
                return false; // No match found
        }
        public int binarySearchInPage(Tuple tuple){
                int low = 0;
                int high = records.size() - 1;

                while (low <= high) {
                        int mid = low + (high - low) / 2;
                        Tuple midVal = records.get(mid);
                        // Comparison is made based on the first attribute
                        int cmp = midVal.compareTo(tuple);

                        if (cmp < 0) {
                                low = mid + 1;
                        } else if (cmp > 0) {
                                high = mid - 1;
                        } else {
                                return mid; // Match found, return the matching tuple
                        }
                }
                return -1;
        }
        /*public Tuple binarySearchInPage(Comparable primaryKey){
                int start = 0;
                int end = records.size() - 1;
                int mid;
                boolean found = false;
                Tuple currentTuple;

                while (!found){
                        if (start > end)
                                return null;
                        mid = start + (end - start) / 2;


                        currentTuple = records.get(mid);

                        if(currentTuple.tuple.get(0).equals(primaryKey)){
                                return currentTuple;
                        }
                        else if(primaryKey.compareTo(currentTuple.tuple.get(0)) < 0){
                                start = mid;
                        }
                        else{
                                end = mid;
                        }
                }
                return null;
        }*/
        public static void main(String[] args){
                //Page page = new Page(1, "Student.ser");
                //System.out.println(page);
                //Page otherPage = deserialize(page.fileName);
                //System.out.println(otherPage);


        }

}