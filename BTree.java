import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.Properties;

public class BTree {

    public BTreeNode root;
    public int b;

    public BTree(int b) {
        this.root = null;
        this.b = b; //
    } // end constructor



    // traverse tree and print everything
    public void traverse() {
        if (this.root != null) {
            this.root.traverse();
        }
        System.out.println();
    } // end traverse



    // function to search a key/word in this tree
    // used node_search instead of BTreeNode's search
    public String search(String w) {
        w = w.toLowerCase();
        if (this.root == null)
            return null;
        else
            return this.root.node_search(w);
    } // end search



    // inserts a new word in the tree
    public void insert(String w) {

        // If tree is empty create and fill a new BTreeNode
        if (root == null) {
            root = new BTreeNode(b, true);

            // Insert word to key
            root.key[0] = w;

            // Update number of keys in root
            root.num = 1;

        } else // If tree is not empty
        {
            // Check if the current node is full.
            // If root is full, split the current node
            if (root.num == 2 * b - 1) {
                // Allocate memory for new root
                BTreeNode n_node = new BTreeNode(b, false);

                // Make old root as child of new root
                n_node.num_child ++;
                n_node.C[0] = root;


                // Split the old root and move 1 key to the new root
                split(0, root, n_node);

                // Change root to the new node
                root = n_node;

                insertFillNode(w, root);

            } else  // If root is not full, call insertFillNode for root
                insertFillNode(w, root);
        }
    }



    // function to insert a new key to this node
    // current node isn't full but checks children
    public void insertFillNode(String w, BTreeNode node) {

        int i = node.num - 1; // last item in key

        // If this is a leaf node
        if (node.leaf == true) {

            // find where w is equal to or greater than key
            while (i >= 1 && w.compareTo(node.key[i-1]) < 0) {
                node.key[i] = node.key[i - 1]; // shift everything to create space for w at the begining of keys
                i--;
            }

            // Insert the new key at found location
            node.key[i] = w;
            node.num = node.num + 1;

        } else // If this node is not leaf
        {
            // Find the child which is going to have the new key
            // check w in key and find point where w is less than key[j] and go to the child at that index
            int j = 0;
            while (j < node.num && w.compareTo(node.key[j]) > 0) {
                j++;
            }

            // See if the found child is full
            if (node.C[j].num == 2 * b - 1) {

                // If the child is full, then split it
                // After split, the middle key of C[i] goes to node and C[i] is splitted into two.
                split(j, node.C[i + 1], node);


                // See which of the two is going to get w
                if (w.compareTo(node.key[j]) > 0) {
                    j++; // key goes to C[i+1] ^^
                }
            }
            // insert w
            insertFillNode(w, node.C[j]);
        }
    } // end insertFillNode



    // function to split a full node
    // val is where the split node is going to be stored on the parent node
    public void split(int val, BTreeNode y, BTreeNode parent) {

        // Create a new node which is going to recieve (b-1) keys of the splitting node (y)
        // parent is either the old parent node for the child or new node that's going to become the parent/root node
        BTreeNode rec = new BTreeNode(y.b, y.leaf);


        // Copy the last (b-1) keys of y to rec
        for (int j = 0; j < b - 1; j++) {
            rec.key[j] = y.key[j + b];
            rec.num ++;
        }

        // Copy the last b children of y to rec
        // if leaf is false, we have to reassign child nodes
        if (y.leaf == false) {
            for (int j = 0; j < b; j++){
                rec.C[j] = y.C[j + b]; // reassing child nodes to from y to rec
            }
        }

        // Reduce the number of keys in y
        y.num = b - 1;

        // shift child after val by 1 on parent node to insert split node at val
        for (int j = parent.num; j > val ; j--) {
            parent.C[j + 1] = parent.C[j];
        }

        // Link the new child to this node
        parent.C[val + 1] = rec;
        parent.num_child++;



        // A key of y will move to the parent node.
        // Find the location of new key and move all greater keys one space ahead in parent node
        for (int j = parent.num; j > val; j--) {
            parent.key[j + 1] = parent.key[j];
        }

        // assign pushed key
        parent.key[val] = y.key[b-1];
        parent.num = parent.num + 1;

    } // end split
}