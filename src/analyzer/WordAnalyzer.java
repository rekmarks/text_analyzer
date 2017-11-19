package analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Performs word-based analysis of text represented as ArrayList
 * 
 * @author rekmarks
 *
 */
public class WordAnalyzer {
	
	/**
	 * 
	 */
	private HashMap<String, Integer> wordMap;
	private HashSet<String> stopList;
	
	
	/**
	 * Constructor
	 * 
	 * Initializes instance variables.
	 */
	public WordAnalyzer() {
		
		wordMap = new HashMap<>();
		stopList = new HashSet<>();
	}
	
	
	/**
	 * Sets stopList from passed String ArrayList
	 * 
	 * @param list 		ArrayList representation of stop list
	 */
	public void setStopList(ArrayList<String> list) {
		
		for (String s : list) {
			
			stopList.add(s);
		}
	}
	
	
	/**
	 * Accessor for wordMap
	 * 
	 * @return		wordMap
	 */
	public HashMap<String, Integer> getWordMap() {
		
		return wordMap;
	}
	
	
	/**
	 * Splits single String by whitespace, repeated (>1) non-letter characters,
	 * and non-ASCII characters
	 * 
	 * @param line 		the unparsed line
	 * @return 			the parsed line
	 */
	private ArrayList<String> parseLine(String line) {
		
		ArrayList<String> parsedLine = new ArrayList<String>();
		String[] split1, split2, split3;
		
		split1 = line.split("\\s+");
		
		// iterate through words in line
		for (String w1 : split1) {
			
			// remove leading and trailing non-alphabetical characters
				// I cannot tell you how happy I am I figured out how
				// to do this early on.
			w1 = w1.replaceAll("^[^a-zA-Z]+", "");
			w1 = w1.replaceAll("[^a-zA-Z]+$", "");
			
			// handle weird stuff like "on the shingle--will you" (notice "--")
			split2 = w1.split("[^a-zA-Z]{2,}");
			
			for (String w2 : split2) {
				
				// handle weird invisible non-ASCII characters in Huck Finn
				// e.g. "catchedcatched" (print that char by char to see)
				split3 = w2.split("[^\\x00-\\x7F]");
				
				// finally, add word or words to parsedLine
				for (String w3 : split3) {
					parsedLine.add(w3);
				}
			}
		}
		
		return parsedLine;
	}
	
	
	/**
	 * Takes a String ArrayList, finds unique words in the string, and counts
	 * them.
	 * 
	 * Handles first person pronoun capitalization.
	 * Does not handle proper noun capitalization. 
	 * 
	 * @param lines 		the strings to be parsed
	 */
	public void generateWordMap(ArrayList<String> lines) {
		
		ArrayList<String> parsedLine;
		
		for (String line : lines) {
			
			parsedLine = parseLine(line);
			
			// iterate through words
			for (String word : parsedLine) {
				
				// ensure word is not empty
				if (!word.isEmpty()) {
					
					// If word contains personal pronoun "I", set all letters
					// except first to lowercase
					if ( word.startsWith("I") 
							&& word.contains("'") 
							&& word.length() <= 4
							&& !word.endsWith("s")) {
						
						word = word.substring(1, word.length());
						
						word = word.toLowerCase();
						
						word = "I" + word;
					
					// if word isn't just "I", set all letters to lowercase
					} else if (!word.equals("I")) {
						
						word = word.toLowerCase();
					}
					
					// finally, check if wordList contains the word, and add as necessary
					if (wordMap.containsKey(word)) {
						
						wordMap.put(word, wordMap.get(word) + 1);
						
					} else {
						
						wordMap.put(word, 1);
					}
				}
			}
		}
	}
	
	
	/**
	 * Gets top 10 most frequent words, with or without checking against
	 * stop list.
	 * 
	 * @param stop 		boolean, checks against stop list if true
	 * @return			HashMap of top ten words with integer counts
	 */
	public HashMap<String, Integer> getTopTen(boolean stop) {
		
		HashMap<String, Integer> topTen = new HashMap<>();
		
		for (String k1 : wordMap.keySet()) {
			
			// advance loop without action if current word in stop list
			if (stop) {
				if (stopList.contains(k1)) {
					continue;
				}
			}
			
			// simply add the first ten entries from wordMap
			if (topTen.size() < 10) {
				
				topTen.put(k1, wordMap.get(k1));
				
			} else {
				
				// iterate through topTen, replacing the first entry whose
				// value is less than the current entry of wordMap
				for (String k2 : topTen.keySet()) {
					
					if (wordMap.get(k1) > topTen.get(k2)) {
						
						topTen.remove(k2);
						
						topTen.put(k1, wordMap.get(k1));
						
						break;
					}
				}
			}
		}
		
		return topTen;		
	}
}
