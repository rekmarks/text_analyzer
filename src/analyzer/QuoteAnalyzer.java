package analyzer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Performs quote-based analysis of text represented as single string
 * 
 * @author rekmarks
 *
 */
public class QuoteAnalyzer {
	
	/**
	 * quotes 		the stored quotes
	 */
	private ArrayList<String> quotes;
	
	
	/**
	 * Constructor
	 * Initializes the single instance variable
	 */
	public QuoteAnalyzer() {
		
		quotes = new ArrayList<>();
	}
	
	
	/**
	 * Quotes accessor
	 * @return		quotes
	 */
	public ArrayList<String> getQuotes() {
		
		return quotes;
	}
	
	
	/**
	 * Finds and stores maybe all, but definitely most quotes (>90%) in
	 * text
	 * 
	 * Even handles multi-paragraph ones
	 * 
	 * @param text			the text to be parsed, as single string
	 * @param delimiter 		the type of quote delimiter (" or ')
	 * @param breaker 		no more quotes will be added once a quote
	 * 						containing this string is found
	 */
	public void findQuotes(String text, String delimiter, String breaker) {
		
		// the pattern is really where the magic happens
		// important that the final token is a negated \w and not simply \s,
		// as the latter doesn't handle stupid formatting like: 'Hello', 
		Pattern pattern = Pattern.compile("\\s(" + delimiter + ".*?\\S"
													+ delimiter + ")[^\\w]");
		Matcher matcher = pattern.matcher(text);
		
		// this is so simple I could weep
		while (matcher.find()) {
			
			// stop finding quotes if breaker is found
			if (!breaker.isEmpty() && matcher.group(1).contains(breaker)) {
				break;
			}
						
			quotes.add(matcher.group(1));
		}
	}
	
	
	/**
	 * Return top ten longest or top ten shortest quotes, not counting
	 * whitespace
	 * 
	 * @param longest 		boolean indicating longest if true, shortest
	 * 						if false
	 * @return 				top ten longest or shortest
	 */
	public ArrayList<String> getTopTen(boolean longest) {
		
		ArrayList<String> topTen = new ArrayList<>();
		
		for (String q : quotes) {
			
			// when finding quote lengths, don't consider whitespace
			String l1 = q.replaceAll("\\s", "");
			
			// simply add the first 10 quotes
			if (topTen.size() < 10) {
				
				topTen.add(q);
				
			} else {
				
				int index = 0;
				int val = 0;
				String l2;
				
				// flag for efficiency, maintains found max/min length in 
				// topTen if no replacement was made
				boolean replace = true;
				
				if (replace) {
					
					// iterate through topTen to find smallest or largest value,
					// per the parameter longest
					for (int i = 0; i < topTen.size(); i++) {
						
						l2 = topTen.get(i).replaceAll("\\s", "");
						
						if (longest) {
							
							// find smallest value of topTen
							// If val is 0,  to set val to length of current
							// topTen quote. Otherwise, do so if the val is 
							// greater than length of current topTen quote
							if (l2.length() < val || val == 0) {
								
								index = i;
								val = l2.length();
							}
						} else {
							
							// ditto for previous case but find the greatest
							// quote length
							if (l2.length() > val || val == 0) {
								
								index = i;
								val = l2.length();
							}
						}
					}
					
					replace = false;
				}
				
				// replace smallest/greatest topTen if current quote from
				// text greater/smaller (depending on longest)
				if ((	longest && l1.length() > val) 
						|| (!longest && l1.length() < val)) {
					
					replace = true;
					topTen.remove(index);
					topTen.add(q);
				} 
			}
		}
		
		return topTen;		
	}
}
