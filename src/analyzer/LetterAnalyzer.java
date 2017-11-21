package analyzer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Performs letter-based analysis of text represented as ArrayList
 * 
 * @author rekmarks
 *
 */
public class LetterAnalyzer {
	
	/**
	 * alphabet 		array of alphabet letters
	 * counts		array of letter counts
	 */
	private char[] alphabet;
	private int[] counts;
	
	
	/**
	 * Constructor
	 * Initializes alphabet
	 */
	public LetterAnalyzer() {
		
		alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	}
	
	
	/**
	 * Iterates through ArrayList of strings and counts frequency of letters.
	 * Ignores case. Initializes counts to new int array.
	 * 
	 * @param lines 		Strings to iterate through
	 */
	public void countLetterFrequency(ArrayList<String> lines) {
		
		counts = new int[26];
		
		// iterate through lines
		for (String line : lines) {
			
			// iterate through characters of line
			for (char c : line.toCharArray()) {
				
				// using isLetter for great efficiency
				// (assuming it checks against the ASCII table and isn't dumb)
				if (Character.isLetter(c)) {
					
					// make character lowercase for comparison
					c = Character.toLowerCase(c);
					
					// increment letter frequency count for each letter found
					for (int i = 0; i < alphabet.length; i++) {
						
						if (alphabet[i] == c ) {
							
							counts[i]++;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Get top ten <letter : count> pairs by count (value)
	 * 
	 * @return 		the 10 most frequent letters in the text
	 */
	public HashMap<Character, Integer> getTopTen() {
		
		HashMap<Character, Integer> topTen = new HashMap<>();
		
		for (int i = 0; i < alphabet.length; i++) {
			
			// simply add the first ten entries
			if (i < 10) {
				
				topTen.put(alphabet[i], counts[i]);
				
			} else {
				
				char storedKey = ' ';
				int minVal = 0;
				
				// flag for efficiency, maintains found min length in 
				// topTen if no replacement was made
				boolean replace = true;
				
				if (replace) {
					
					// find smallest value of topTen
					for (Character key : topTen.keySet()) {
						
						if (topTen.get(key) < minVal || minVal == 0) {
							
							storedKey = key;
							minVal = topTen.get(key);
						}
					}
					
					replace = false;
				}
				
				// if current value of wordMap greater than the smallest value
				// of topTen, replace the latter with the former
				if (counts[i] > minVal) {
					
					topTen.remove(storedKey);
					topTen.put(alphabet[i], counts[i]);
					replace = true;
				}
			}
		}
				
		return topTen;
	}
}