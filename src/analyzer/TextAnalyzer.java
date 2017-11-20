package analyzer;

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
	
	private ArrayList<String> lines;
	private ArrayList<String> posWords;
	private ArrayList<String> negWords;
	
	
	public TextAnalyzer() {
		
		reader = new TextReader();
		letters = new LetterAnalyzer();
		words = new WordAnalyzer();
		quotes = new QuoteAnalyzer();
		
		lines = new ArrayList<>();
		posWords = new ArrayList<>();
		negWords = new ArrayList<>();
	}
	
	
	public void runSuite(String path, String[] range, String delimiter) {
		
		// 0. SETUP AND INPUT
		reader.readFile("texts/stop-list.txt");		
		words.setStopList(reader.getLines());
		
//		reader.readFile("texts/positive-words.txt");
//		posWords = reader.getLines();
//		
//		reader.readFile("texts/negative-words.txt");
//		negWords = reader.getLines();
		
		
		reader.readFile(path);
		reader.pruneLines(range[0], range[1]);
		
		// 1. LETTERS
		runLetters();
		
		// 2. WORDS
		runWords();
		
		// 3. QUOTES
		runQuotes();
	}
	
	
	/**
	 * 
	 */
	private void runLetters() {
		
		letters.countLetterFrequency(reader.getLines());
		
		HashMap<Character, Integer> letterFreq = letters.getTopTen();
		
		for (int i = 0; i < 10; i++) {
			
			int maxVal = 0;
			char maxKey = ' ';
			
			for (char key : letterFreq.keySet()) {
				
				if (letterFreq.get(key) > maxVal) {
					
					maxVal = letterFreq.get(key);
					maxKey = key;
				}
			}
			
			System.out.println(maxKey + "\t:\t" + maxVal);
			letterFreq.remove(maxKey);
		}
		System.out.println();
	}
	
	
	/**
	 * 
	 */
	private void runWords() {
		
		words.generateWordMap(reader.getLines());
		
		ArrayList<HashMap<String, Integer>> wordTops = new ArrayList<>();
		
		wordTops.add(words.getTopTen(false));
		wordTops.add(words.getTopTen(true));
		
		for (HashMap<String, Integer> hm : wordTops) {
			
			for (int i = 0; i < 10; i++) {
				
				int maxVal = 0;
				String maxKey = "";
				
				for (String key : hm.keySet()) {
					
					if (hm.get(key) > maxVal) {
						
						maxVal = hm.get(key);
						maxKey = key;
					}
				}
				
				System.out.println(maxKey + "\t:\t" + maxVal);
				hm.remove(maxKey);
			}
			System.out.println();
		}
	}
	
	
	/**
	 * 
	 */
	private void runQuotes() {
		
	}

}
