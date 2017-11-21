package analyzer;

import java.io.PrintWriter;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper for all other analyzer classes, with a PrintWriter for output
 * 
 * @author rekmarks
 *
 */
public class TextAnalyzer {
	
	/**
	 * self-explanatory		
	 */
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
	 * 
	 * Initializes PrintWriter, DELETES analysis file if it already exists
	 */
	public TextAnalyzer() {
		
		// delete analysis.txt if it exists
		try {
			File f = new File("texts/analysis.txt");
			if (f.delete()) {
				System.out.println("TextAnalyzer: Existing analysis.txt deleted.");
			}
		} catch (Exception e) {
			System.out.println("TextAnalyzer: WARNING: File deletion failure.");
			e.printStackTrace();
		}
		
		// initialize instance variables
		reader = new TextReader();
		letters = new LetterAnalyzer();
		words = new WordAnalyzer();
		quotes = new QuoteAnalyzer();
		
		// initialize PrintWriter
		try {
			writer = new PrintWriter("texts/analysis.txt");
			writer.println();
		} catch (Exception e) {
			writer.println("TextAnalyzer: WARNING: PrintWriter failure.");
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
	 * Closes writer
	 */
	public void closeWriter() {
		
		writer.close();
	}
	
	/**
	 * Runs all forms of analysis on specified text
	 * 
	 * @param path			text to be analyzed
	 * @param range			beginning and end lines for analysis
	 * 							Note: only uses first and second elements, set
	 * 							both to empty strings to analyze entire text
	 * @param delimiter 		quote delimiter (" or ')
	 * @param breaker		stop string for quote analysis
	 */
	public void runSuite(	String path, String[] range
							, String delimiter, String breaker) {		
		
		writer.println("TEXT: " + path + "\n");
		
		reader.readFile(path, true);
		reader.pruneLines(range[0], range[1]);
		
		// LETTERS
		writer.println("LETTER FREQUENCY ANALYSIS\n");
		writer.println("   TOP 10 MOST FREQUENT LETTERS");
		runLetters();
		
		// WORDS
		writer.println("WORD FREQUENCY ANALYSIS\n");
		runWords();
		
		// SENTIMENT (wildcard)
		writer.println("WORD SENTIMENT ANALYSIS\n");
		runSentiment();
		
		// get rid of lines for great memory savings
		reader.resetLines();
		
		// get whole text
		reader.readFile(path, false);
		
		// QUOTES
		writer.println("QUOTE LENGTH ANALYSIS\n");
		runQuotes(delimiter, breaker);
		
		// get rid of text for great memory savings
		reader.resetText();
		
		writer.println("-------------------------------");
		
		// flush here or we actually run out of buffer
		// (is that the right term?)
		writer.flush();
		
		System.out.println("TextAnalyzer: Analysis of " + path + " completed.");
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
			
			writer.println("   " + maxKey + "\t\t\t\t" + maxVal);
			letterFreq.remove(maxKey);
		}
		writer.println();
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
		
		writer.println("   TOP 10 MOST FREQUENT WORDS");
		
		for (HashMap<String, Integer> topTen : wordTops) {
			
			if (count == 0) {
				writer.println("   WITHOUT STOP LIST");
			} else {
				writer.println("   WITH STOP LIST");
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
				if (maxKey.length() == 1) {
					writer.println("   " + maxKey + "  \t\t\t" + maxVal);
				} else if (maxKey.length() < 5) {
					writer.println("   " + maxKey + " \t\t\t" + maxVal);
				} else if (maxKey.length() > 8) {
					writer.println("   " + maxKey + " \t" + maxVal);
				} else {
					writer.println("   " + maxKey + " \t\t" + maxVal);
				}
				
				topTen.remove(maxKey);
			}
			count++;
			writer.println();
		}
	}
	
	
	/**
	 * Runs sentiment analysis on current text
	 */
	private void runSentiment() {
		
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
		writer.println("   WORD COUNTS");
		writer.println("   Total \t\t" + counts[0] );
		writer.println("   Positive \t\t" + counts[1] );
		writer.println("   Negative \t\t" + counts[2] );
		writer.println("   Neutral \t\t" + counts[3] );
		writer.println("\n   PERCENTAGES");
		writer.println("   Positive \t\t" + f.format(fracs[0]));
		writer.println("   Negative \t\t" + f.format(fracs[1]));
		writer.println("   Neutral \t\t" + f.format(fracs[2]) + "\n");
		
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
				writer.println("   TOP 10 LONGEST QUOTES");
			} else {
				writer.println("   TOP 10 SHORTEST QUOTES");
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
				
				writer.println("\t\t" + topTen.get(index));
				
				topTen.remove(index);
			}
			count++;
			writer.println();
		}
		
		// reset for memory savings and to prepare for next text
		quotes.resetQuotes();
	}
}
