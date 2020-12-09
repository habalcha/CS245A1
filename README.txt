Name: Hermi Balcha
Date: December 8, 2020

Trie Analysis:

insert - O(n)                         // n is length of string; in CS245A1

search - O(n log(n))                  // n is length of string, worse case, correct_word gets called recursively

correct_word - O(n log(n))



BTree Analysis:

(BTreeNode) search - O(n log(n))                  // n in number of words

insert - O(n log(n))

(BTreeNode) complete_word - O(n log(n))           // similar structure to search but for chunks of words

(BTreeNode) node_search - O(m^2*n log (n))          // In the worse case, missing search gets called
                                                  // search used in BTree

(BTreeNode) missing_search - O(m^2*n log (n))       // worse case search and/or complete_word gets called m*m times (m is length of word )


Overall, BTree requires a lot of time and space to do spell check and give good results. If a word is missing, search gets called multiple times
for various spelling issues (duplicate letter, missing letter,...)


Extra credit: I set up correct_word() so that it starts 2 nodes before the point search failed. This gave me a lot more space to search for and build the words.
It correctly gets 83 of the 104 words I took from the link provided. 

