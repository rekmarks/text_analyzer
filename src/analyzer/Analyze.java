package analyzer;

/**
 * User interface for analyzer package
 * 
 * @author rekmarks
 *
 */
public class Analyze {

	/**
	 * Analyzes all books in texts. 
	 * Some foreknowledge of books required:
	 * 		- path
	 * 		- lines delimiting the actual content of the book (optional)
	 * 		- character delimiting quotes
	 * 		- string delimiting end of 
	 * 
	 * @param args 	n/a
	 */
	public static void main(String[] args) {
		
		System.out.println("Analyzer: Initializing TextAnalyzer.");
		
		TextAnalyzer ta = new TextAnalyzer();
		
		System.out.println("Analyzer: Analysis initiated.");
		
		// run analysis suite on each book separately
		// les-mis.txt
		String[] range = {"BOOK FIRST--A JUST MAN"
				, "of itself, as the night comes when day is gone.]"};
		ta.runSuite("texts/les-mis.txt", range, "\"", "Project Gutenberg");
		
		// alice-in-wonderland.txt
		String[] range2 = {"CHAPTER I. Down the Rabbit-Hole"
				, "              THE END"};
		ta.runSuite("texts/alice-in-wonderland.txt", range2, "'", "AS-IS");
		
		// tale-of-two-cities.txt
		String[] range3 = {"Book the First--Recalled to Life"
					, "far, far better rest that I go to than I have ever known.\""};
		ta.runSuite("texts/tale-of-two-cities.txt", range3, "\"", "");
		
		// christmas-carol.txt
		String[] range4 = {"STAVE I:  MARLEY'S GHOST"
				, "observed, God bless Us, Every One!"};
		ta.runSuite("texts/christmas-carol.txt", range4, "\"", "");
		
		// pride-prejudice.txt
		String[] range5 = {"Chapter 1"
				, "her into Derbyshire, had been the means of uniting them."};
		ta.runSuite("texts/pride-prejudice.txt", range5, "\"", "Project Gutenberg");
		
		// metamorphosis.txt
		String[] range6 = {"One morning, when Gregor Samsa woke from troubled dreams, he found"
				, "first to get up and stretch out her young body."};
		ta.runSuite("texts/metamorphosis.txt", range6, "\"", "");
		
		// my-man-jeeves.txt
		String[] range7 = {"LEAVE IT TO JEEVES", "THE END"};
		ta.runSuite("texts/my-man-jeeves.txt", range7, "\"", "Project Gutenberg");
		
		// huck-finn.txt
		String[] range8 = {"Scene:  The Mississippi Valley Time:  Forty to fifty years ago"
				, "THE END. YOURS TRULY, _HUCK FINN_."};
		ta.runSuite("texts/huck-finn.txt", range8, "\"", "Project Gutenberg");
		
		// tom-sawyer.txt
		String[] range9 = {"PREFACE", "part of their lives at present."};
		ta.runSuite("texts/tom-sawyer.txt", range9, "\"", "Project Gutenberg");
		
		ta.closeWriter();
		
		System.out.println("Analyze: Analysis complete.");
	}
}
