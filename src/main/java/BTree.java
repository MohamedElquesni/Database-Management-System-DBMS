import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

/**
 * A B+ tree
 * Since the structures and behaviors between internal node and external node are different, 
 * so there are two different classes for each kind of node.
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
public class BTree<TKey extends Comparable<TKey>, TValue> implements java.io.Serializable {
    private BTreeNode<TKey> root;
    public BTree() {
        this.root = new BTreeLeafNode<TKey, Vector<TValue>>();
    }

    /**
     * Insert a new key and its associated value into the B+ tree.
     */
    public void insert(TKey key, TValue value) {
        BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);
        leaf.insertKey(key, value);
        if (leaf.isOverflow()) {
            BTreeNode<TKey> n = leaf.dealOverflow();
            if (n != null)
                this.root = n;
        }
    }

    /**
     * Search a key value on the tree and return its associated value.
     */
    public Vector<TValue> search(TKey key) {
        BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);

        int index = leaf.search(key);
        return (index == -1) ? null : leaf.getValue(index);
    }

    /**
     * Delete a key and its associated value from the tree.
     */
    public void delete(TKey key) {
        BTreeLeafNode<TKey, TValue> leaf = this.findLeafNodeShouldContainKey(key);

        if (leaf.delete(key) && leaf.isUnderflow()) {
            BTreeNode<TKey> n = leaf.dealUnderflow();
            if (n != null)
                this.root = n;
        }
    }

    /**
     * Search the leaf node which should contain the specified key
     */
    @SuppressWarnings("unchecked")
    private BTreeLeafNode<TKey, TValue> findLeafNodeShouldContainKey(TKey key) {
        BTreeNode<TKey> node = this.root;
        while (node.getNodeType() == TreeNodeType.InnerNode) {
            node = ((BTreeInnerNode<TKey>)node).getChild( node.search(key) );
        }

        return (BTreeLeafNode<TKey, TValue>)node;
    }
    public void serialize(String filename) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            //System.out.println("BTree object serialized and saved to " + filename);
        }  catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }

    }
    public static BTree<?, ?> deserialize(String filename) {
        BTree<?, ?> bTree = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            bTree = (BTree<?, ?>) in.readObject();
            in.close();
            fileIn.close();
           // System.out.println("BTree object deserialized from " + filename);
        } catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return bTree;
    }


















    public String toString() {
        StringBuilder sb = new StringBuilder();
        Queue<BTreeNode<TKey>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                BTreeNode<TKey> node = queue.poll();

                // Append node with a border
                assert node != null;
                sb.append("|").append(node.toString().trim());

                if (node.getNodeType() == TreeNodeType.InnerNode) {
                    BTreeInnerNode<TKey> innerNode = (BTreeInnerNode<TKey>) node;
                    for (int j = 0; j <= innerNode.getKeyCount(); j++) {
                        BTreeNode<TKey> child = innerNode.getChild(j);
                        if (child != null) {
                            queue.offer(child);
                        }
                    }
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }



//
}