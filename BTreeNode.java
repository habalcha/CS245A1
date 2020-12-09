import java.util.*;
import java.util.ArrayList;

// A BTree node for word search
class BTreeNode {
    String[] key; // stores the words in this node
    int b; // half the number of keys
    BTreeNode[] C; // BTreeNode of all children pointed at from this node
    int num; // number of words in keys
    int num_child; // number of children linked to this node
    boolean leaf;



    // Constructor
    BTreeNode(int b, boolean leaf) {
        this.b = b;
        this.leaf = leaf;
        this.key = new String[2 * b - 1]; // used 2*b to make splitting nodes easier
        this.C = new BTreeNode[2 * b];
        this.num = 0;
        this.num_child = 0;
    } // end constructor



    // traverse through tree and prints everything
    public void traverse() {

        // loops through current node and prints out the words in key
        int i = 0;
        for (i = 0; i < this.num; i++) {

            // print out values in current node
            System.out.print(key[i] + " ");
        }

        // if its not leaf, call traverse again on children
        // if children is null, does print anything
        for (int j = 0; j < num_child; j++){
            if (this.leaf == false) {
                C[j].traverse();
            }
        }

        // everything on the last node
        // does nothing if last node is null
        if (leaf == false)
            C[i].traverse();
    } // end traverse



    // search for word in tree
    public String search(String w) {
        String word;

        // search keys in current node
        // make this more efficient
        for (int i = 0; i < num; i++) {
            if (key[i].equals(w)) {
                return w;  /////// word found
            }
        }

        // search child of node
        for (int j = 0; j < num_child; j++) {
            word = C[j].search(w);
            if (word != null){ // if its null, call search on next child
                return word;
            }
        }

        return null;
    } // end search



    //calls search: if word is found return w, otherwise returns the 3 possible words for w
    public String node_search(String w){

        String found_word = search(w);

        // if word isn't found call missing_search
        if (found_word == null){
            BTreeNode bt = missing_search(w);

            // build word with the suggested words
            StringBuilder replace = new StringBuilder();

             for (int i = 0; i < 2*b-1; i++){
                 if (bt.key[i]!= null && i < 3){
                     replace.append(bt.key[i].strip());
                     replace.append(" ");
                 }
             }
            return replace.toString().strip();
        }

        return found_word;
    } // end node_search



    // build new words from w
    // very expensive
    public BTreeNode missing_search(String w){

        // set up a new BTree to add found words
        BTreeNode node = new BTreeNode(this.b, true);
        String subStr1, subStr2, chunk1, chunk2, possibleWord;
        int count = 0; // stop if count > 2b-1 (current node is full)

        // swap 2 characters in the word -> lenght to length
        for (int j = 1; j < w.length(); j++) {
            subStr1 = w.substring(0, j - 1);
            subStr2 = w.substring(j + 1);
            char ch1 = w.charAt(j - 1);
            char ch2 = w.charAt(j);

            possibleWord = subStr1 + ch2 + ch1 + subStr2;
            if (count < 4 && possibleWord != null && search(possibleWord) == possibleWord &&
                    node.foundInKey(possibleWord) == false) {
                node.key[count] = possibleWord;
                node.num++;
                count++;
            }
        }

        // if we have a word from swapping letters, skip this (more expensive)
        if (count < 1){
            for (int i = 0; i < w.length(); i++) {

                // split word into two
                chunk1 = w.substring(0, i);
                chunk2 = w.substring(i);

                // remove char from word
                subStr1 = w.substring(0, i);
                subStr2 = w.substring(i + 1);

                // check if removing a letter creates a word
                possibleWord = subStr1 + subStr2;
                if (count < 4 && possibleWord!= null && search(possibleWord) == possibleWord &&
                        node.foundInKey(possibleWord) == false) {
                    node.key[count] = possibleWord;
                    node.num ++;
                    count++;
                }

                // check if adding an additional character
                for (char ch = 'a'; ch <= 'z'; ch++) {
                    // add character to word
                    possibleWord = chunk1 + ch + chunk2;
                    if (count < 4 && possibleWord!= null && search(possibleWord) == possibleWord &&
                            node.foundInKey(possibleWord) == false) {
                        node.key[count] = possibleWord;
                        node.num ++;
                        count++;
                    }

                    // add a character to word where 1 char is already removed
                    possibleWord = subStr1 + ch + subStr2;
                    if (count < 4 && possibleWord!= null && search(possibleWord) == possibleWord &&
                            node.foundInKey(possibleWord) == false) {
                        node.key[count] = possibleWord;
                        node.num ++;
                        count++;
                    }
                }

                // check if the first chunk is a word
                possibleWord = chunk1;
                if (count < 4 && possibleWord!= null && search(possibleWord) == possibleWord &&
                        node.foundInKey(possibleWord) == false && !chunk1.equals("a")){
                    node.key[count] = possibleWord;
                    node.num ++;
                    count++;
                }

                // if none of the above steps worked, complete the word from a chunk (at least half the word)
                // recursively reduces the chunk of word until we can form a complete correct word
                if (count < 1 && subStr1.length() > 0) {
                    possibleWord = complete_word(subStr1, "");
                    if (possibleWord != null){
                        node.key[count] = possibleWord;
                        node.num ++;
                    }

                }
            }
        }

        if (node.key[0] == null){
            node.key[count] = "-----"; // wont be used unless complete_word fails
        }



        return node;
    } // end missing_search()



    // check if w in in the current key
    boolean foundInKey(String W){
        for (int i = 0; i < num; i++){
            if (key[i] != null && key[i].equals(W)){
                return true;
            }
        }
        return false;
    }



    // takes a chunk of word and completes it based on what's in the tree
    String complete_word(String chunk, String word1){

        String word;


        // search current node for word chunk
        for (int i = 0; i < num; i++) {
            if (key[i].length() > chunk.length() && chunk.length() > 1){
                if (chunk.equals(key[i].substring(0, chunk.length()-1))){
                    word1= key[num-1];
                    return key[i];  /////// word chunk found
                }
            }
            if (chunk.length() < 1){
                return key[i];
            }
        }

        // search child of node
        for (int j = 0; j < num_child; j++) {
            word = C[j].complete_word(chunk, word1);
            if (word != null){
                return word; // found word with current chunk
            }
        }

        if (word1.equals("")){
            word1 = "error with tree";
        }
        return word1;

    }



    // prints node
    void print(BTreeNode node){
        System.out.println(Arrays.toString(node.key));
    }
}