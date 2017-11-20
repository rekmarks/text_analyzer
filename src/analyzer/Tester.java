package analyzer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tester for TextReader
 * 
 * @author rekmarks
 *
 */
public class Tester {
	
	private ArrayList<String> lines;

	/**
	 * Reads in text file, prints beginning and end of lines from file
	 * before and after pruning
	 * 
	 * @param args 		n/a
	 */
	public void testInput() {
		
		TextReader reader = new TextReader();
		
//		reader.readFile("texts/alice-in-wonderland.txt");
//		reader.readFile("texts/huck-finn.txt");
		reader.readFile("texts/les-mis.txt");
		
		lines = reader.getLines();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(lines.get(i));
		}
		
		System.out.println("\n---\n");
		
		for (int i = 4; i >= 0; i--) {
			System.out.println(lines.get(lines.size() - 1 - i));
		}
		
		System.out.println("\n--------------------------------\n");
		
//		reader.pruneLines("CHAPTER I. Down the Rabbit-Hole", "              THE END");
//		reader.pruneLines("CHAPTER I.", "THE END. YOURS TRULY, _HUCK FINN_.");
		reader.pruneLines("BOOK FIRST--A JUST MAN", "of itself, as the night comes when day is gone.]");
		
		lines = reader.getLines();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(lines.get(i));
		}
		
		System.out.println("\n---\n");
		
		for (int i = 4; i >= 0; i--) {
			System.out.println(lines.get(lines.size() - 1 - i));
		}
		
		System.out.println("\nFIN.");
		
		
		// LetterAnalyzer
		
		LetterAnalyzer letters = new LetterAnalyzer();
		
		letters.countLetterFrequency(lines);
		
		char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		int[] counts = letters.getCounts();
		int letterSum = 0;
		
		for (int i = 0; i < counts.length; i++) {
			System.out.println(alphabet[i] + " : " + counts[i]);
			letterSum += counts[i];
		}
		
		System.out.println("\n" + letterSum);
		
		HashMap<Character, Integer> topTen = letters.getTopTen();
		
		for (Character key : topTen.keySet()) {
			
			System.out.println(key + " : " + topTen.get(key));
		}
	}
	
	public void testWords() {
		
		WordAnalyzer words = new WordAnalyzer();
		
		words.generateWordMap(lines);
		
		HashMap<String, Integer> wordMap = words.getWordMap();
		
		for (String key : wordMap.keySet()) {
			
			System.out.println(key + " : " + wordMap.get(key));
		}
		
		System.out.println();
		
		HashMap<String, Integer> topTen = words.getTopTen(false);
		
		for (String key : topTen.keySet()) {
			
			System.out.println(key + " : " + topTen.get(key));
		}
		
		System.out.println();
		
		TextReader reader = new TextReader();
		
		reader.readFile("texts/stop-list.txt");
		
		words.setStopList(reader.getLines());
		
		topTen = words.getTopTen(true);
		
		for (String key : topTen.keySet()) {
			
			System.out.println(key + " : " + topTen.get(key));
		}
		
		System.out.println();
	}
}
