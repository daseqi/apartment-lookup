// dph38
public class PricesDLB 
{
    Node root; 


    /**
     * This function creates the root.  
     * 
     * @param word
     */
    void createRoot(String word, int index)
    {
        root = new Node(word.charAt(0), null, null, index);
        Node tempRoot = root;
        for(int i = 1; i < word.length(); i++)
        {
            tempRoot.child = new Node(word.charAt(i), null, null, index);
            tempRoot = tempRoot.child;
        }
        tempRoot.child = new Node('$', null, null, index); // $ is terminator char
    }



    /**
     * This function adds a word to the DLB
     * 
     * @param word
     */
    public void add(String word, int index)
    {
        if(root == null) // no word in DLB
        {
            createRoot(word, index);
        }
        else            // at least one word in DLB
        {
            StringBuilder mutableWord = new StringBuilder(word);
            recursiveAdd(mutableWord, root, null, index);
        }
    }



    /**
     * This function recursively traverses the DLB, checking
     * to see if the word exists. It calls createChildren() 
     * when remaining characters do not exist yet in the 
     * Trie.
     * 
     * @param word : word that is being added
     * @param root : node at current level
     * @param parent : node "above" current level
     */
    void recursiveAdd(StringBuilder word, Node root, Node parent, int index)
    {
        Node tempRoot = root;
        if(word.length()==0)
        {
            tempRoot.child = new Node('$', null, null, index);   // end of recursion
            return;         
        }
        else
        {
            if(Character.toString(word.charAt(0)).compareTo(Character.toString(tempRoot.val)) == 0) // same char
            {
                if(tempRoot.child==null)                // if the node dead ends, we simply create the children ourselves 
                {
                    createChildren(word.deleteCharAt(0), tempRoot, index);
                    return;
                }
                recursiveAdd(word.deleteCharAt(0), tempRoot.child, tempRoot, index);       // else, enter next level
                return;
            }
            else 
            {                                                                                           
                while(tempRoot.sibling != null)                             
                {
                    recursiveAdd(word, tempRoot.sibling, tempRoot, index);
                    return;
                }
                tempRoot.sibling = new Node(word.charAt(0), null, null, index);       // else put it at the end of level
                createChildren(word.deleteCharAt(0), tempRoot.sibling, index);
                return;
            }
        }
    }




    /**
     * This function is called from recursiveAdd()
     * when the remaining characters (children) of 
     * the word are not stored anywhere else 
     * in the Trie, and hence must be created. 
     * 
     * @param word
     * @param root
     */
    void createChildren(StringBuilder word, Node root, int index)
    {
        Node tempRoot = root;
        if(word.length()==0)            // end of word
        {
            tempRoot.child = new Node('$', null, null, index);  
            return;         
        }
        for(int i = 0; i < word.length(); i++)
        {
            tempRoot.child = new Node(word.charAt(i), null, null, index); 
            tempRoot = tempRoot.child;            
        }
        tempRoot.child = new Node('$', null, null, index); // $ is terminator char
    }




    /**
     * This function finds the last node of word using searchLevel() and
     * recursion. Should return either the end node (with .val == '$')
     * or null.
     * 
     * @param word
     * @param end
     * @param depth
     * @return end
     */    
    Node findEnd(String word, Node end, int depth)
    {
        if(end==null) return null;
        if(depth==word.length()) return end; // return $ (terminator char)
        char character = word.charAt(depth);

        Node level = searchLevel(character, end); // does this character exist?
        if(level==null) return null;

        return  findEnd(word, level.child,  depth+1); // go one level down trie
    }





    /**
     * This function searches the root node level for
     * the next character. 
     * 
     * @param c
     * @param root
     * @return root
     */
    Node searchLevel(char c, Node root)
    {
        while(root!=null)
        {
            if(root.val==c)
            {
                return root;
            }
            root = root.sibling;
        }
        return null;
    }




    /**
     * This function takes the word inputted by user
     * and returns the predictions. It calls the 
     * recursive functions findEnd() and update() 
     * 
     * @param word : user input
     * @return predictions
     */
    public int get(String word)
    {
        Node end = findEnd(word, root, 0);
        if(end==null) return -1; // word not in DLB
        if(end.val!='$') return -1; // not at the end of word in DLB 
        return end.index;

    }


    /**
     * This class creates a node that contains a character value,
     * child node, and sibling node.
     * 
     */
    private class Node
    {
        char val;
        int index;
        Node child;
        Node sibling;
        public Node(char value, Node nextChild, Node nextSibling, int index)
        {
            this.val = value;
            this.child = nextChild;
            this.sibling = nextSibling;
            this.index = index;
        }
    }
}
