package analyzer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper/interface for all other analysis classes
 * 
 * @author rekmarks
 *
 */
public class TextAnalyzer {
	
	private TextReader reader;
	private LetterAnalyzer letters;
	private WordAnalyzer words;
	private QuoteAnalyzer quotes;
	
	
	/**
	 * Constructor
	 * 
	 * Initializes instance variables. 
	 * Sets invariant words instance variables:
	 * 		 - stopList, negativeWords, and positiveWords
	 */
	public TextAnalyzer() {
		
		reader = new TextReader();
		letters = new LetterAnalyzer();
		words = new WordAnalyzer();
		quotes = new QuoteAnalyzer();
		
		// set words instance variables
		reader.readFile("texts/stop-list.txt");		
		words.setStopList(reader.getLines());
		
		reader.readFile("texts/positive-words.txt");
		words.setPositiveWords(reader.getLines());
		
		reader.readFile("texts/negative-words.txt");
		words.setNegativeWords(reader.getLines());
		
		// free up this memory space because why not
		reader.resetLines();
	}
	
	
	/**
	 * Runs all forms of analysis on specified text
	 * 
	 * @param path			text to be analyzed
	 * @param range			beginning and end lines for analysis
	 * 							Note: only uses first and second elements, set
	 * 							both to empty strings to analyze entire text
	 * @param delimiter 		quote delimiter (" or ')
	 */
	public void runSuite(String path, String[] range, String delimiter) {		
		
		System.out.println("\nTEXT: " + path + "\n");
		
		reader.readFile(path);
		reader.pruneLines(range[0], range[1]);
		
		// LETTERS
		System.out.println("LETTER FREQUENCY ANALYSIS");
		runLetters();
		
		// WORDS
		System.out.println("WORD FREQUENCY ANALYSIS");
		runWords();
		
		// SENTIMENT (wildcard)
		System.out.println("SENTIMENT ANALYSIS");
		runSentiment();
		
		// QUOTES
		System.out.println("QUOTE LENGTH ANALYSIS");
		runQuotes(delimiter);
		
		// get rid of lines for great memory savings
		reader.resetLines();
		
		System.out.println("-------------------------------");
	}
	
	
	/**
	 * Runs letter analysis on current text
	 */
	private void runLetters() {
		
		letters.countLetterFrequency(reader.getLines());
		
		HashMap<Character, Integer> letterFreq = letters.getTopTen();
		
		// iterate through topTen, printing them out by value in 
		// descending order
		for (int i = 0; i < 10; i++) {
			
			int maxVal = 0;
			char maxKey = ' ';
			
			for (char key : letterFreq.keySet()) {
				
				if (letterFreq.get(key) > maxVal) {
					
					maxVal = letterFreq.get(key);
					maxKey = key;
				}
			}
			
			System.out.println("   " + maxKey + "\t\t" + maxVal);
			letterFreq.remove(maxKey);
		}
		System.out.println();
	}
	
	
	/**
	 * Runs word analysis on current text
	 */
	private void runWords() {
		
		words.generateWordMap(reader.getLines());
		
		ArrayList<HashMap<String, Integer>> wordTops = new ArrayList<>();
		
		// get topTen both without and with stop list
		wordTops.add(words.getTopTen(false));
		wordTops.add(words.getTopTen(true));
		
		// silly counter for printing out stop list vs. no stop list
		int count = 1;
		
		for (HashMap<String, Integer> topTen : wordTops) {
			
			if (count == 1) {
				System.out.println("   (without stop list)");
			} else {
				System.out.println("   (with stop list)");
			}
			
			// iterate through topTen, printing them out by value in 
			// descending order
			for (int i = 0; i < 10; i++) {
				
				int maxVal = 0;
				String maxKey = "";
				
				for (String key : topTen.keySet()) {
					
					if (topTen.get(key) > maxVal) {
						
						maxVal = topTen.get(key);
						maxKey = key;
					}
				}
				
				// different prints for great formatting justice
				if (maxKey.length() < 5) {
					System.out.println("   " + maxKey + "\t \t" + maxVal);
				} else {
					System.out.println("   " + maxKey + " \t" + maxVal);
				}
				
				topTen.remove(maxKey);
			}
			count++;
			System.out.println();
		}
	}
	
	
	/**
	 * Runs sentiment analysis on current text
	 */
	public void runSentiment() {
		
		int[] counts = words.getSentimentCounts();
		
		// calculate fractions from counts
		float[] fracs = new float[3];
		fracs[0] = counts[1] / (float) counts[0];
		fracs[1] = counts[2] / (float) counts[0];
		fracs[2] = counts[3] / (float) counts[0];
		
		// create percentage decimal format
		DecimalFormat f = new DecimalFormat( "##.#%" );
		
		// print everything very manually because why not
		System.out.println("   COUNTS");
		System.out.println("   Total \t" + counts[0] );
		System.out.println("   Positive \t" + counts[1] );
		System.out.println("   Negative \t" + counts[2] );
		System.out.println("   Neutral \t" + counts[3] );
		System.out.println("\n   PERCENTAGES");
		System.out.println("   Positive \t" + f.format(fracs[0]));
		System.out.println("   Negative \t" + f.format(fracs[1]));
		System.out.println("   Neutral \t" + f.format(fracs[2]) + "\n");
		
		// reset wordMap for great memory savings
		words.resetWordMap();
	}
	
	
	/**
	 * Runs quote analysis on current text
	 */
	private void runQuotes(String delimiter) {
		
		
		
		System.out.println();
	}
}
