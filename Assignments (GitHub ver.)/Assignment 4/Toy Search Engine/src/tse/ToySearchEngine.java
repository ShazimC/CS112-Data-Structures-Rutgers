package tse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class ToySearchEngine {
	
	/**
	 * This is a hash table of all keys. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keysIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keysIndex and noiseWords hash tables.
	 */
	public ToySearchEngine() {
		keysIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of key occurrences
	 * in the document. Uses the getKey method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keys in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeysFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		HashMap<String, Occurrence> docTable = new HashMap<String, Occurrence>(1000, 2.0f);
		Scanner sc = new Scanner(new File(docFile));
		while(sc.hasNext()) {
			String word = sc.next();
			if(getKey(word)!=null) {
				if(docTable.containsKey(word)) {
					docTable.get(word).frequency++;
				}else {
					Occurrence wordDebut = new Occurrence(docFile, 1);
					docTable.put(word, wordDebut);
				}
			}
		}
		sc.close();
		return docTable;
	}
	
	/**
	 * Merges the keys for a single document into the master keysIndex
	 * hash table. For each key, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same key's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeys(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(Map.Entry<String, Occurrence> entry: kws.entrySet()) {
			String word = entry.getKey();
			Occurrence occ = entry.getValue();
			if(keysIndex.get(word) == null) {
				ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();
				occurrences.add(occ);
				keysIndex.put(word, occurrences);
			}else {
				keysIndex.get(word).add(occ);
				insertLastOccurrence(keysIndex.get(word));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * Note: No other punctuation characters will appear in grading testcases
	 * 
	 * @param word Candidate word
	 * @return Key (word without trailing punctuation, LOWER CASE)
	 */
	public String getKey(String word) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if(word.length() == 0 || word.equals(""))
			return null;
		
		int lastIndex = word.length()-1;
		while(word.charAt(lastIndex) == '.' || word.charAt(lastIndex) == ',' || word.charAt(lastIndex) == '?' || word.charAt(lastIndex) == ':' || word.charAt(lastIndex) == ';' || word.charAt(lastIndex) == '!') {
			word = word.substring(0,  lastIndex);
			lastIndex--;
			if(lastIndex < 0)
				return null;
		}
		lastIndex = word.length()-1;
		while(Character.isLetter(word.charAt(lastIndex))) {
			lastIndex--;
			if(lastIndex < 0)
				break;
		}
		
		if(lastIndex<0) {
			word = word.toLowerCase();
			if(noiseWords.contains(word))
				return null;
			return word;
		}
		return null;
	}
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		if (occs.size() <=1)
			return null;
		
		int left = 0;
		int right = occs.size()-2;
		int midpoint = (left+right)/2;
		Occurrence latest = occs.remove(occs.size()-1);
		int latestFrequency = latest.frequency;
		
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		

		while (left<=right){
			midpoint = (left + right)/2;
			int data = occs.get(midpoint).frequency;
			midpoints.add(midpoint);
			if (data == latestFrequency)
				break;

			else if (data < latestFrequency){
				right = midpoint - 1;
			}
			else if (data > latestFrequency){
				left = midpoint + 1;
				if (right <= midpoint)
					midpoint = midpoint + 1;
			}
		}midpoints.add(midpoint);
		
		occs.add(midpoints.get(midpoints.size()-1), latest);

		return midpoints;
	}
	
	/**
	 * This method indexes all words found in all the input documents. When this
	 * method is done, the keysIndex hash table will be filled with all keys,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void buildIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all words
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeysFromDocument(docFile);
			mergeKeys(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		ArrayList<String> docs = new ArrayList<String>();
		
		ArrayList<Occurrence> kw1Docs = keysIndex.get(kw1);
		ArrayList<Occurrence> kw2Docs = keysIndex.get(kw2);
		
		int i = 0;
		int k = 0;
		int limit = 0;
		while(limit<5) {
			while(i>=kw1Docs.size()) {
				if(!docs.contains(kw2Docs.get(k).document))
					docs.add(kw2Docs.get(k).document);
				k++;
				if(k>=kw2Docs.size())
					break;
			}
			if(i>=kw1Docs.size()) break;
			while(k>=kw2Docs.size()) {
				if(!docs.contains(kw1Docs.get(i).document))
					docs.add(kw1Docs.get(i).document);
				i++;
				if(i>=kw1Docs.size())
					break;
			}
			if(k>=kw2Docs.size())
				break;
			if(kw1Docs.get(i).frequency>=kw2Docs.get(k).frequency){
				if(!docs.contains(kw1Docs.get(i).document))
					docs.add(kw1Docs.get(i).document);
				else limit--;
				if(kw1Docs.get(i).frequency==kw2Docs.get(k).frequency)
					k++;
				i++;
			}else {
				if(!docs.contains(kw2Docs.get(k).document))
					docs.add(kw2Docs.get(k).document);
				else limit--;
				k++;
			}
			limit++;
			if(docs.size()>=kw1Docs.size()+kw2Docs.size())
				break;
		}
		
		return docs;
	
	}
}