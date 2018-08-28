package prefixTree;

import java.util.ArrayList;

/**
 * This class implements a PrefixTree.
 * 
 * @author Sesh Venugopal
 *
 */
public class PrefixTree {

	// prevent instantiation
	private PrefixTree() {
	}

	/**
	 * Builds a PrefixTree(Prefixtionary-tree) by inserting all words in the input array, one at a
	 * time, in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words
	 * in the input array are all lower case.
	 * 
	 * @param allWords
	 *            Input array of words (lowercase) to be inserted.
	 * @return Root of PrefixTree with all words inserted from the input array
	 */
	public static PrefixTreeNode buildPrefixTree(String[] allWords) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		
		PrefixTreeNode root = new PrefixTreeNode(null, null, null);
		
		Indices substring = new Indices(0, (short)0, (short)(allWords[0].length()-1));
		root.firstChild = new PrefixTreeNode(substring, null, null);
			
		for(int i=1; i < allWords.length; i++) {
			addWord(i, allWords, root);
		}
		return root;
	}
	
	private static void addWord(int newWordIndex, String[] allWords, PrefixTreeNode root) {
		if(root == null) {System.out.println("at null");};
		if(root.firstChild == null) System.out.println("atNull");
		
		int childIndex = root.firstChild.substr.wordIndex;
		String childWord = allWords[childIndex];
		int childStart = root.firstChild.substr.startIndex;
		int childEnd = root.firstChild.substr.endIndex + 1;
		
		String newWord = allWords[newWordIndex];
		if(prefixExists(childStart, childEnd, childWord.substring(childStart, childEnd), newWord)){
			addWord(newWordIndex, allWords, root.firstChild);
			
			/*PrefixTreeNode temp = root.firstChild.firstChild;
			while(temp.sibling!=null)
				temp = temp.sibling;
			
			Indices newWordIndices = new Indices(newWordIndex, (short)(temp.substr.startIndex), (short)(newWord.length()-1));
			temp.sibling = new PrefixTreeNode(newWordIndices, null, null);
			return;*/
			
		}else if(!commonPrefix(childStart, childWord, newWord).equals("")) {
			
			String prefix = commonPrefix(childStart, childWord, newWord);
			PrefixTreeNode temp = root.firstChild;
			int prefixLocation = root.firstChild.substr.wordIndex;
			int prefixStart = root.firstChild.substr.startIndex;
			int prefixEnd = prefixStart + prefix.length()-1;
			Indices prefixIndices = new Indices(prefixLocation, (short)prefixStart, (short)prefixEnd);
			root.firstChild = new PrefixTreeNode(prefixIndices, temp, temp.sibling);
			temp.substr.startIndex = prefixEnd + 1;
			temp.sibling = null;
			
			while(temp.sibling!=null) {
				temp = temp.sibling;
			}
			
			int newLocation = newWordIndex;
			int newWordStart = temp.substr.startIndex;
			int newWordEnd = newWord.length()-1;
			Indices newWordIndices = new Indices((short)newLocation, (short)newWordStart, (short)newWordEnd);
			temp.sibling = new PrefixTreeNode(newWordIndices, null, null);
			return;
		}
		else if(root.firstChild.sibling!=null) {
			/*PrefixTreeNode siblingPointer = root.firstChild.sibling;
			int siblingIndex = siblingPointer.substr.wordIndex;
			String siblingWord = allWords[siblingIndex];
			int siblingStart = siblingPointer.substr.startIndex;
			int siblingEnd = siblingPointer.substr.endIndex + 1;*/
			
			siblingHelper(root.firstChild, allWords, newWordIndex);
		}
		
		else{
			PrefixTreeNode temp = root.firstChild;
			while(temp.sibling!=null)
				temp = temp.sibling;
			
			int location = newWordIndex;
			int start = temp.substr.startIndex;
			int end = newWord.length()-1;
			Indices newWordIndices = new Indices(location, (short)start, (short)end);
			temp.sibling = new PrefixTreeNode(newWordIndices, null, null);
			return;
		}
	}
	
	private static void siblingHelper(PrefixTreeNode child, String[] allWords, int newWordIndex) {
		/*int childIndex = child.substr.wordIndex;
		String childWord = allWords[childIndex];
		int childStart = child.substr.startIndex;
		int childEnd = child.substr.endIndex;*/
		
		if(child.sibling == null) {
			Indices siblingIndices = new Indices(newWordIndex, (short)child.substr.startIndex, (short)(allWords[newWordIndex].length()-1));
			child.sibling = new PrefixTreeNode(siblingIndices, null, null);
			return;
		}
		
		int siblingIndex = child.sibling.substr.wordIndex;
		String siblingWord = allWords[siblingIndex];
		int siblingStart = child.sibling.substr.startIndex;
		int siblingEnd = child.sibling.substr.endIndex+1;
		
		String newWord = allWords[newWordIndex];
		
		if(prefixExists(siblingStart, siblingEnd, siblingWord.substring(siblingStart, siblingEnd), newWord)) {
			addWord(newWordIndex, allWords, child.sibling);
		}else if(!commonPrefix(siblingStart, siblingWord, newWord).equals("")) {
			String prefix = commonPrefix(siblingStart, siblingWord, newWord);
			PrefixTreeNode temp = child.sibling;
			int prefixLocation = temp.substr.wordIndex;
			int prefixStart = temp.substr.startIndex;
			int prefixEnd = prefixStart + prefix.length()-1;
			Indices prefixIndices = new Indices(prefixLocation, (short)prefixStart, (short)prefixEnd);
			child.sibling = new PrefixTreeNode(prefixIndices, temp, temp.sibling);
			
			temp.substr.startIndex = prefixEnd + 1;
			
			int newLocation = newWordIndex;
			int newWordStart = temp.substr.startIndex;
			int newWordEnd = newWord.length()-1;
			Indices newWordIndices = new Indices((short)newLocation, (short)newWordStart, (short)newWordEnd);
			temp.sibling = new PrefixTreeNode(newWordIndices, null, null);
			return;
		}else {
			siblingHelper(child.sibling, allWords, newWordIndex);
		}
	
	}
	
	private static String commonPrefix(int start, String existingWord, String newWord) {
		
		int counter = start;
		String prefix = "";
		
		while(existingWord.charAt(counter) == newWord.charAt(counter)) {
			counter++;
			if(counter >= existingWord.length() || counter >= newWord.length())
				break;
		}
		if(counter == start)
			return "";
		prefix = existingWord.substring(start, counter);
		
		return prefix;
	}
	private static boolean prefixExists(int prefixStart, int prefixEnd, String prefix, String newWord) {
		if(prefix.length()>=newWord.length() || prefixEnd > newWord.length())
			return false;
		return newWord.substring(prefixStart, prefixEnd).equals(prefix);
	}

	/**
	 * Given a PrefixTree, returns the "completeWordList" for the given prefix, i.e. all the
	 * leaf nodes in the PrefixTree whose words start with this prefix. For instance,
	 * if the PrefixTree had the words "bear", "bull", "stock", and "bell", the
	 * completeWordList for prefix "b" would be the leaf nodes that hold "bear",
	 * "bull", and "bell"; for prefix "be", the completeWordList would be the leaf nodes
	 * that hold "bear" and "bell", and for prefix "bell", completeWordList would be the
	 * leaf node that holds "bell". (The last example shows that an input prefix can
	 * be an entire word.) The order of returned leaf nodes DOES NOT MATTER. So, for
	 * prefix "be", the returned list of leaf nodes can be either hold [bear,bell]
	 * or [bell,bear].
	 *
	 * @param root
	 *            Root of PrefixTree that stores all words to search on for completeWordList
	 * @param allWords
	 *            Array of words that have been inserted into the PrefixTree
	 * @param prefix
	 *            Prefix to be completed with words in PrefixTree
	 * @return List of all leaf nodes in PrefixTree that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<PrefixTreeNode> completeWordList(PrefixTreeNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		if(prefix.equals(" ") || prefix.equals(""))
				return null;
		ArrayList<PrefixTreeNode> wordsUnderThePrefix = new ArrayList<PrefixTreeNode>();
		
		PrefixTreeNode nodePointer = root;
		if(nodePointer.substr == null)
			nodePointer = root.firstChild;
		String tempString = "";
		while(nodePointer!=null) {
			
			int nodeWordIndex = nodePointer.substr.wordIndex;
			int nodeStart = nodePointer.substr.startIndex;
			int nodeEnd = nodePointer.substr.endIndex+1;
			
			String nodeFullWord = allWords[nodeWordIndex];
			tempString = nodeFullWord.substring(0, nodeEnd);
			
			if(commonPrefix(0, tempString, prefix).equals(""))
				nodePointer = nodePointer.sibling;
			else if(!commonPrefix(0, tempString, prefix).equals("")) {
				if(commonPrefix(0, tempString, prefix).equals(prefix)) {
					if(nodePointer.firstChild!=null)
						addNodes(wordsUnderThePrefix, nodePointer.firstChild);
					else wordsUnderThePrefix.add(nodePointer);
					break;
					/*if(nodePointer.firstChild==null) {
						wordsUnderThePrefix.add(nodePointer);
						nodePointer = nodePointer.sibling;
					}else nodePointer = nodePointer.firstChild;*/
				}else if(commonPrefix(0, tempString, prefix).length()<prefix.length()){
					if(prefix.indexOf(tempString)<0)
						nodePointer = nodePointer.sibling;
					else nodePointer = nodePointer.firstChild;
				}
			}
		}
		if(wordsUnderThePrefix.isEmpty()) {
			return null;
		}
		return wordsUnderThePrefix;
	}
	
	private static void addNodes(ArrayList<PrefixTreeNode> list, PrefixTreeNode root) {
		if(root == null) return;
		if(root.firstChild == null && root.sibling == null)
			list.add(root);
		if(root.firstChild == null && root.sibling!=null) {
			list.add(root);
			addNodes(list, root.sibling);
		}else {
			addNodes(list, root.firstChild);
			addNodes(list, root.sibling);
		}
	}

	public static void print(PrefixTreeNode root, String[] allWords) {
		System.out.println("\nPrefixTree\n");
		print(root, 1, allWords);
	}

	private static void print(PrefixTreeNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (PrefixTreeNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
