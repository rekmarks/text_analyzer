package analyzer;

import java.util.ArrayList;

/**
 * Letter Frequency: Consider all letters ‘a’ – ‘z’. For the given book, count 
 * the number of occurrences of each letter. Print out the top-10 most frequent 
 * letters along with the frequency in the book.
 */

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
	 * Initializes instance variables
	 */
	public LetterAnalyzer() {
		
		alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		counts = new int[26];
	}
	
	
	/**
	 * Iterates through ArrayList of strings and counts frequency of letters.
	 * Ignores case.
	 * 
	 * @param lines 		Strings to iterate through
	 */
	public void countLetterFrequency(ArrayList<String> lines) {
		
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
	 * Accessor for counts
	 * @return		counts
	 */
	public int[] getCounts() {
		return counts;
	}
	
	// TODO: top 10 with counts goes in a method here or elsewhere?
}
