public class Trie
{
    final int alphabet = 27;  // 27 to include apostrophe
    TrieNode root = new TrieNode();



    //trie node
    class TrieNode{
        TrieNode[] children = new TrieNode[alphabet]; // 27 children at each node
        boolean lastChar; // signifys the end of a word

        // create a node with space for 27 children



        TrieNode(){
            lastChar = false; // is true when we have a complete word
            for (int i = 0; i < alphabet; i++){
                children[i] = null;
            }
        }
    } // end TrieNode



    //returns the root of the trie
    TrieNode getRoot(){
        return root;
    }


    //'insert' a word into the trie by adding each letter to their appropriate node
    public void insert(String w){

        // change all words to lower case for consistency
        String l_word = w.toLowerCase();

        // get current root node
        TrieNode node = root;

        // go through word by character
        for (int i = 0; i < w.length(); i++){

            //get the coresponding index value of the letter at i
            // subtract the ASCII value of "a" from the character
            int index = (l_word.charAt(i) - 'a');

            try {
                //see if the index has been previously initiated
                //if the index is still null, 'initiate' that index by creating a TrieNode there

                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
            }

            catch(ArrayIndexOutOfBoundsException e) {

                //the index of apostrophe is always going to be out of bounds
                //change the index of apostrophe to the last index of our trie

                if (index == -58){ // -58 is ASCII "'" - ASCII "a"
                    index = 26;

                    //see if this new index has been previously initiated
                    //if the index is still null, 'initiate' that index by creating a TrieNode there
                    if (node.children[index] == null){
                        node.children[index] = new TrieNode();
                    }
                }
                else {
                    System.out.println("unrecognized letter/character");
                }
            }

            // if the node has been initiated, change node to the child at index
            node = node.children[index];

        }

        // mark the word as completed
        node.lastChar = true;
    } // end insert



    //find a word in the trie using lastCharacter as an indication of a complete word
    public String search (String w)
    {
        // Change the word into lower string since that is how the trie is being built
        String new_w = w.toLowerCase();

        // new string builder to add all the letters of the word we are searching
        // to return a string of the word and help with correcting misspelled words later
        StringBuilder builder = new StringBuilder();

        TrieNode node = root;
        TrieNode prevNode = null; // to backtrack on a word if it isn't correct
        TrieNode prevPrevNode = null; // to backtrack on null pointer exceptions and correct the word

        // goes through each character and builds the given word
        for (int i = 0; i < new_w.length(); i++)
        {
            // subtract the ascii number for "a" to get number from 0-26 for each letter
            int index = new_w.charAt(i) - 'a';

            try {
                try {

                    // if there is a letter at this node, add that letter to the word builder
                    if (node.children[index] != null) {
                        char charAtIndex = (char) (index + 'a'); // convert int index to character
                        String stringCharAtIndex = Character.toString(charAtIndex);
                        builder.append(stringCharAtIndex);
                    }
                }

                // catch the special case of apostrophe
                catch (ArrayIndexOutOfBoundsException e) {
                    if (index == -58 && node.children[26] != null) {
                        char charAtIndex = (char) (index + 'a');
                        String stringCharAtIndex = Character.toString(charAtIndex);
                        builder.append(stringCharAtIndex);
                    }
                }
            } catch (NullPointerException e){

        }
            // assign current node to previous node and take a "step" to the next character
            prevPrevNode = prevNode;
            prevNode = node;
            try{
                node = node.children[index];
            } catch (NullPointerException e){
                return correct_word(prevPrevNode, builder, 0); // builds a new word by starting from 3 letters back
            }

        }

        try {

            // if the word is complete, return it
            if (node.lastChar == true) {
                String builtWord = builder.toString();
                return builtWord;
            } else {
                return correct_word(prevNode, builder, 0); // correct word
            }
        } catch (NullPointerException e){
            return correct_word(prevNode, builder, 0); // correct word
        }
    } // end search



    public static String correct_word(TrieNode prevNode, StringBuilder builder, int count){

        String builtWord = builder.toString();

        // new string builder to create the replacement word if the word doesn't exist
        StringBuilder replace = new StringBuilder();
        StringBuilder build = new StringBuilder();


        // find the shortest complete forward-moving word starting from previous node
        for (int i = 0; i < 27; i++){

            // if the word can be completed by adding one character
            if (count < 3 && prevNode.children[i] != null && prevNode.children[i].lastChar == true ){

                // complete the word from previous node by adding the last character
                replace.append(builtWord);
                char charAtIndex = (char) (i + 'a');
                String stringCharAtIndex = Character.toString(charAtIndex);
                replace.append(stringCharAtIndex);
                replace.append(" ");
                count = count + 1;

            } else if (count < 3 && prevNode.children[i] != null && prevNode.children[i].lastChar != true ){

                // adds a letter to the built word we had from search
                // calls correct word again until we get lastChar = true
                build.append(builtWord);
                char charAtIndex = (char) (i + 'a');
                String stringCharAtIndex = Character.toString(charAtIndex);
                build.append(stringCharAtIndex);
                replace.append(correct_word(prevNode.children[i], build, count)); // call correct word again
                replace.append(" ");
                count = count + 1;
            }
            build.setLength(0);
        }

        // build the final word that has up to 3 suggested words
        StringBuilder finalWords = new StringBuilder();

        String corrected_words = replace.toString().strip();
        String[] corrected_split = corrected_words.split(" ");

        try{
            finalWords.append(corrected_split[0].strip());
            finalWords.append(" ");
            finalWords.append(corrected_split[1].strip());
            finalWords.append(" ");
            finalWords.append(corrected_split[2].strip());
            finalWords.append(" ");
            finalWords.append(corrected_split[3].strip());
        } catch (ArrayIndexOutOfBoundsException e){}


        return finalWords.toString();
    } // end correct_word
}