import java.util.*;
import java.io.*;
import java.util.Properties;

public class CS245A1 {
    File file, input;
    Scanner sc, inputSC;
    FileWriter writer;
    PrintWriter printWriter;
    Trie trie;
    BTree tree;
    String key;
    String value;


    // constructor
    public CS245A1() {
         key = "storage";
         value = "trie";
         System.setProperty(key, value); // set storage property
    } // end constructor



    // set storage for english.0 and decide how it gets stored based on the storage property
    public void set_storage(String storage) throws FileNotFoundException{

        // check if the file is in directory
        try {
            file = new File("english.0");
            sc = new Scanner(file);

        } catch (FileNotFoundException e){
            System.out.println("The file isn't found in the directory.");
            throw e;
        }

        String word;

        // store data in trie
        if (storage.equals("Trie") || storage.equals("trie")){
            trie = new Trie();

            System.out.println("Data stored in trie!");
            while (sc.hasNext()) {
                word = sc.next();
                trie.insert(word);
            }
        }

         // store data in Btree
        if (storage.equals("tree") || storage.equals("Tree")){
            tree = new BTree(5);
            System.out.println("Data stored in tree! Search can sometimes take a while to complete if there are a lot of long misspelled words.");
            while (sc.hasNext()) {
                word = sc.next();
                tree.insert(word);
            }
        }

    } // end set_storage



    // search for word is trie or tree
    public String search_word(String word, String storage){
        if (storage.equals("Tree") || storage.equals("tree")){
            return tree.search(word);
        } else if (storage.equals("Trie") || storage.equals("trie")){
            return trie.search(word);
        }
        return ("Word not found"); // doesn't get used
    }



    // main function
    // reads properties file
    // read and searches input file and writes word to output file

    public static void main (String[] args){
        CS245A1 test = new CS245A1();
        String word;
        try {
            test.input = new File(args[0]); // input file
            test.inputSC = new Scanner (test.input);
        }
        catch (FileNotFoundException e){
            System.out.println("Input file not found!");
        }

         try {
             test.writer = new FileWriter(args[1]); // output file
             test.printWriter = new PrintWriter(test.writer);
         }
         catch (IOException e){
            System.out.println("IOException");
            }

         // check configuration
         try{
            File prop = new File("a1properties.txt");
            Scanner prop_scanner = new Scanner (prop);
            String prop_input = prop_scanner.next();
            test.key = prop_input.split("=")[0];
            test.value = prop_input.split("=")[1];
            System.setProperty(test.key, test.value);
         }
         catch (FileNotFoundException e){}

         // reassign storage type
         String storage = System.getProperty(test.key);
         try{
            test.set_storage(storage);
         } catch (FileNotFoundException e){
            System.out.println("Error setting storage");
         }

        // search words from input and suggest correct word from it
        while (test.inputSC.hasNext()) {
            word = test.inputSC.next();
            String newWord = test.search_word(word, storage);

            // lower letters
            String wordLower = word.toLowerCase();

            if(wordLower != newWord){
                word = newWord;
            }
            test.printWriter.printf("%s" + "%n", word);
        }
        test.printWriter.close();

    } // end main
}

