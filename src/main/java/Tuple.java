import java.util.Vector;
import java.io.Serializable;

public class Tuple implements Serializable, Comparable{
    Vector<Object> tuple;

    public Tuple(){
        tuple = new Vector<Object>();
    }

//    public void insert(Object o, int index){
//        tuple.insertElementAt(o,index);
//    }
//
//    public void delete(Object o, int index){
//        tuple.insert(null, index);
//    }

    public String toString()
    {
        String res = "";
        for (int i = 0; i < tuple.size(); i++)
        {
            res += tuple.get(i) + ",";
        }

        return res.substring(0, res.length() - 1);
    }

    public int compareTo(Object o) {
        Tuple t = (Tuple) o;
        if (tuple.get(0) instanceof java.lang.Integer){
            if ((Integer)tuple.get(0) < (Integer) t.tuple.get(0))
                return -1;
            else if((Integer)tuple.get(0) > (Integer) t.tuple.get(0)) {
                return 1;
            }
            else {
                return 0;
            }
        }
        else if(tuple.get(0) instanceof java.lang.Double){
            if ((Double)tuple.get(0) < (Double) t.tuple.get(0))
                return -1;
            else if((Double)tuple.get(0) > (Double) t.tuple.get(0))
                return 1;
            else
                return 0;
        }
        else if(tuple.get(0) instanceof java.lang.String){
            return ((String)tuple.get(0)).compareTo((String)t.tuple.get(0));
        }
        else{
            return -1;
        }
    }
    public boolean equals(Object o){
        Tuple t = (Tuple) o;
        return (this.compareTo(t) == 0);
    }
    public void set(int index, Object newValue) {
        tuple.set(index, newValue);
    }
    public static void main(String[] args){
    }
}
