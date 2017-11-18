package analyzer;

import java.util.ArrayList;

/**
 * @author rekmarks
 *
 */
public class InputTester {

	public static void main(String[] args) {
		
		TextReader reader = new TextReader();
		
		reader.readFile("texts/alice-in-wonderland.txt");
		
		ArrayList<String> lines = reader.getLines();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(lines.get(i));
		}
		
		System.out.println("\n---\n");
		
		for (int i = 4; i >= 0; i--) {
			System.out.println(lines.get(lines.size() - 1 - i));
		}
		
		System.out.println("\n--------------------------------\n");
		
		reader.pruneLines("CHAPTER I. Down the Rabbit-Hole", "              THE END");
		
		lines = reader.getLines();
		
		for (int i = 0; i < 5; i++) {
			System.out.println(lines.get(i));
		}
		
		System.out.println("\n---\n");
		
		for (int i = 4; i >= 0; i--) {
			System.out.println(lines.get(lines.size() - 1 - i));
		}
		
		System.out.println("\nFIN.");
		
	}

}
