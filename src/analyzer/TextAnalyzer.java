package analyzer;

import java.io.PrintWriter;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper for all other analyzer classes
 * 
 * @author rekmarks
 *
 */
public class TextAnalyzer {
	
	private TextReader reader;
	private LetterAnalyzer letters;
	private WordAnalyzer words;
	private QuoteAnalyzer quotes;
	private PrintWriter writer;
	
	
	/**
	 * Constructor
	 * 
	 * Initializes instance variables. 
	 * Sets invariant words instance variables:
	 * 		 - stopList, negativeWords, and positiveWords
	 */
	public TextAnalyzer() {
		
		try {
			File f = new File("texts/analysis.txt");
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		reader = new TextReader();
		letters = new LetterAnalyzer();
		words = new WordAnalyzer();
		quotes = new QuoteAnalyzer();
		
		try {
			writer = new PrintWriter("texts/analysis.txt");
		} catch (Exception e) {
			System.out.println("WARNING: PrintWriter failure.");
			e.printStackTrace(writer);
		}
		
		// set words instance variables
		reader.readFile("texts/stop-list.txt", true);		
		words.setStopList(reader.getLines());
		
		reader.readFile("texts/positive-words.txt", true);
		words.setPositiveWords(reader.getLines());
		
		reader.readFile("texts/negative-words.txt", true);
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
	public void runSuite(	String path, String[] range
							, String delimiter, String breaker) {		
		
		System.out.println("\nTEXT: " + path + "\n");
		
		reader.readFile(path, true);
		reader.pruneLines(range[0], range[1]);
		
		// LETTERS
		System.out.println("LETTER FREQUENCY ANALYSIS");
		System.out.println("   Top 10 Most Frequent Letters");
		runLetters();
		
		// WORDS
		System.out.println("WORD FREQUENCY ANALYSIS");
		runWords();
		
		// SENTIMENT (wildcard)
		System.out.println("WORD SENTIMENT ANALYSIS");
		runSentiment();
		
		// get rid of lines for great memory savings
		reader.resetLines();
		
		// get whole text
		reader.readFile(path, false);
		
		// QUOTES
		System.out.println("QUOTE LENGTH ANALYSIS");
		runQuotes(delimiter, breaker);
		
		// get rid of text for great memory savings
		reader.resetText();
		
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
		
		ArrayList<HashMap<String, Integer>> wordTops = new ArrayList<>(2);
		
		// get topTen both without and with stop list
		wordTops.add(words.getTopTen(false));
		wordTops.add(words.getTopTen(true));
		
		// silly counter for printing out stop list vs. no stop list
		int count = 0;
		
		System.out.println("   Top 10 Most Frequent Words");
		
		for (HashMap<String, Integer> topTen : wordTops) {
			
			if (count == 0) {
				System.out.println("   Without Stop List");
			} else {
				System.out.println("   With Stop List");
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
		
		// print everything very manually because a loop would be more effort
		// than it's worth
		System.out.println("   Word Counts");
		System.out.println("   Total \t" + counts[0] );
		System.out.println("   Positive \t" + counts[1] );
		System.out.println("   Negative \t" + counts[2] );
		System.out.println("   Neutral \t" + counts[3] );
		System.out.println("\n   Percentages");
		System.out.println("   Positive \t" + f.format(fracs[0]));
		System.out.println("   Negative \t" + f.format(fracs[1]));
		System.out.println("   Neutral \t" + f.format(fracs[2]) + "\n");
		
		// reset wordMap for great memory savings
		words.resetWordMap();
	}
	
	
	/**
	 * Runs quote analysis on current text
	 */
	private void runQuotes(String delimiter, String breaker) {
		
		quotes.findQuotes(reader.getText(), delimiter, breaker);
		
		ArrayList<ArrayList<String>> quoteTops = new ArrayList<ArrayList<String>>(2);
		
		// add top ten longest and shortest to topTens
		quoteTops.add(quotes.getTopTen(true));
		quoteTops.add(quotes.getTopTen(false));
		
		// silly counter for printing labels
		int count = 0;
		
		for (ArrayList<String> topTen : quoteTops) {
			
			if (count == 0) {
				System.out.println("   Top 10 Longest Quotes");
			} else {
				System.out.println("   Top 10 Shortest Quotes");
			}
			
			// print all 10 quotes out by value in descending order
			for (int i = 0; i < 10; i++) {
				
				int maxVal = 0;
				int index = 0;
				String charsOnly;
				
				// iterate through topTen to find current longest quote
				for (int j = 0; j < topTen.size(); j++) {
					
					charsOnly = topTen.get(j).replaceAll("\\s", "");
					
					if (charsOnly.length() > maxVal) {
						
						maxVal = charsOnly.length();
						index = j;
					}
				}
				
				System.out.println("   " + topTen.get(index));
				
				topTen.remove(index);
			}
			count++;
			System.out.println();
		}
	}
}
