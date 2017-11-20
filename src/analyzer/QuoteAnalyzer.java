package analyzer;

import java.util.ArrayList;

/**
 * @author rekmarks
 *
 */
public class QuoteAnalyzer {
	
	/**
	 * 
	 */
	private ArrayList<String> quotes;
	private String[] longestQuotes;
	private String[] shortestQuotes;
	
	
	public QuoteAnalyzer() {
		
		quotes = new ArrayList<>();
		longestQuotes = new String[10];
		shortestQuotes = new String[10];
	}
	
	
	public ArrayList<String> getQuotes() {
		
		return quotes;
	}
	
	
	/**
	 * @return	 	longestQuotes
	 */
	public String[] getLongestQuotes() {
		return longestQuotes;
	}
	
	
	/**
	 * @return 	 	shortestQuotes
	 */
	public String[] getShortestQuotes() {
		return shortestQuotes;
	}
	
	
	
	public void findQuotes(ArrayList<String> lines, String delimiter) {
		
		String currentQuote = "";
		String[] currentSplit;
		boolean remainder = false;;
		
		for (String line : lines) {
			
			if (line.trim().isEmpty()) {
				continue;
			}
						
			// (3) non-default case, leftover string from previous iteration(s)
			if (!currentQuote.isEmpty()) {
				
				// split line on quote end
				currentSplit = line.split("(?<=\\S\\" + delimiter + "[^\\S])", 2);
				
				if (currentSplit.length == 1) {
					
					// if split did nothing, entire line is part of quote
					currentQuote += currentSplit[0];
					
				} else {
					
					// if split matched, save complete quote and store string remainder
					quotes.add(currentQuote + currentSplit[0]);
					currentQuote = currentSplit[1];
					remainder = true;
				}
			}
			
			// (1) default case, currentQuote is empty
			if (currentQuote.isEmpty()) {
				
				// split line per delimiter and quote formatting, once
				// note that this removes leading whitespace
				currentSplit = line.split("(\\s)(?=\\" + delimiter + "\\w)", 2);
				
				// handle edge case where line starts with quote
				if (line.charAt(0) == delimiter.charAt(0) && currentSplit.length == 1) {
					
					// match actually found, store
					currentQuote = currentSplit[0];
					
				} else if (currentSplit.length == 1) {
					
					// if the split found no match, continue to next line
					continue;
					
				} else {
					
					// if the split found a match, store relevant portion
					currentQuote = currentSplit[1];
				}
			}
			
			// (2) handles remainders end-quote splitting
			while (!currentQuote.isEmpty()) {
				
				// handle remainders
				if (remainder) {
					
					// split line per delimiter and quote formatting, once
					currentSplit = currentQuote.split("(\\s)(?=\\" + delimiter + "\\w)", 2);
					
					// handle edge case where line starts with quote
					if (line.charAt(0) == delimiter.charAt(0) && currentSplit.length == 1) {
						
						// match actually found, store
						currentQuote = currentSplit[0];
						
					} else if (currentSplit.length == 1) {
						
						// if the split found no match, remainder contains no
						// quote, so advance to next text line
						currentQuote = "";
						break;
						
					} else {
						
						// if the split found a match, store relevant portion
						currentQuote = currentSplit[1];
					}
				}
				
				// split current quote per delimiter and quote formatting, once
				currentSplit = currentQuote.split("(?<=\\S\\" + delimiter + ")([^\\S])", 2);
				
				if (currentSplit.length == 1) {
					
					// if the split found no match, advance to next line
					break;
					
				} else {
					
					// if split found match, save quote
					quotes.add(currentQuote + currentSplit[0]);
					
					// store remainder of split, it may contain a quote
					currentQuote = currentSplit[1];
					remainder = true;
				}
			}
		}
	}
	
	/**
	 * @param lines
	 * @param delimiter
	 */
	public void findQuotesOld(ArrayList<String> lines, String delimiter) {
		
		int i1, i2;
		boolean newQuote = true;
		String currentQuote = "";
				
		for (String line : lines) {
			
			i1 = 0;
			i2 = 0;
						
			// set i1 in case of new quote
			if (newQuote) {
				
				// get index of first delimiter
				i1 = line.indexOf(delimiter);
				
				// handle invalid starting delimiters
				// note: indexOf returns -1 if no such character
				while (i1 != -1) {
					
					try {
						
						// if apostrophe quote delimiters are used, ignore 
						// contractions
						if (delimiter.equals("'")) {
							
							i1 = ignoreContractions(line, i1, true, newQuote);
						}
						
						// if delimiter is followed by a letter, break to
						// continue extracting quote
						if (Character.isLetter(line.charAt(i1 + 1))) {
							
							break;
							
						} else {
							
							// if delimiter not followed by letter, attempt
							// to find next delimiter
							i1 = line.indexOf(delimiter, i1 + 1);
						}
						
					} catch (Exception e) {
						
						// if charAt throws an exception, it's probably string
						// index out of bounds, in which case we're dealing 
						// with weirdness and not a quote
						i1 = -1;
					}
				}
				
				if (i1 == -1) {
					
					// go to next lines string if no valid delimiter found
					continue;
				}
			}
			
			while ( (i1 != -1) && (i2 != -1) ) {
				
				if (newQuote) {
					
					// since it's a new quote, attempt to find index of
					// a second delimiter
					i2 = line.indexOf(delimiter, i1 + 1);
					
					// if apostrophe quote delimiters are used, ignore 
					// contractions
					if (delimiter.equals("'")) {
						
						i2 = ignoreContractions(line, i2, false, newQuote);
					}
					
					if (i2 == -1) {
						
						// if no second delimiter found, unflag newQuote and
						// and store part of current quote from this line
						newQuote = false;
						currentQuote = line.substring(i1);
						
					} else {
						
						int lineLen = line.length();
						
						// if second delimiter found, we have a full quote,
						// so store it
						if (i2 == line.length()) {
							quotes.add(line.substring(i1));
						} else {
							quotes.add(line.substring(i1, i2 + 1));
						}
						
						
						// after storing, reset currentQuote, try to find
						// index of new delimiter
						currentQuote = "";
						i1 = line.indexOf(delimiter, i2 + 1);
					}
				} else {
					
					// since it's not a new quote, find first delimiter
					i2 = line.indexOf(delimiter);
					
					// if apostrophe quote delimiters are used, ignore 
					// contractions
					if (delimiter.equals("'")) {
						
						i2 = ignoreContractions(line, i2, false, newQuote);
					}
					
					if (i2 == -1) {
						
						// if no delimiter found, the entire line is part
						// of a quote
						currentQuote = currentQuote + " " + line;
						
					} else {
						
						// if delimiter found, save stored part of quote and 
						// the remainder of quote from current line
						quotes.add(currentQuote + " " + line.substring(0, i2 + 1));
						
						// flag newQuote, reset currentQuote, attempt o find
						// another delimiter
						newQuote = true;
						currentQuote = "";
						i1 = line.indexOf(delimiter, i2 + 1);
					}
				}
			}
		}
	}
	
	
	/**
	 * @param line
	 * @param index
	 * @param precedes
	 * @return
	 */
	private int ignoreContractions(String line, int index, boolean precedes, boolean newQuote) {
		
		int origIndex = index;
		
		int modifier;
		if (precedes) {
			modifier = -1;
		} else {
			modifier = 1;
		}
		
		while (index != -1) {
			
			try {
				
				if (Character.isLetter(line.charAt(index + modifier))) {
					
					index = line.indexOf("'", index + 1);
					
					if (!precedes && !newQuote && index == -1) {
						
						return index;
					}
					
				} else {
					
					index = -1;
				}
			} catch (Exception e) {
				
				index = -1;
				// do nothing, we have a quote if a line begins
				// or ends with a delimiter
			}
		}
		
		if (index < origIndex) {
			
			index = origIndex;
		}

		
		return index;
	}
}
