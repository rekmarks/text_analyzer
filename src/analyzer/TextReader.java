package analyzer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads texts files and stores as ArrayList or String.
 * Once stored, can prune beginning and end.
 * 
 * @author rekmarks
 *
 */
public class TextReader {
	
	/**
	 * lines 		the lines from the text file
	 * text 			the text as a single string
	 */
	private ArrayList<String> lines;
	private String text;
	
	
	/**
	 * Constructor
	 * Initializes lines
	 */
	public TextReader() {
		
		lines = new ArrayList<>();
		text = "";

	}
	
	/**
	 * Reads file at specified path
	 */
	public void readFile(String path, boolean byLines) {
		
		try (Scanner in = new Scanner(new File(path))) {
			
			if (byLines) {
				
				while (in.hasNextLine()) {
					
					lines.add(in.nextLine());
				}
				
				// something is wrong if lines is empty at this point
				if (lines.isEmpty()) {
					
					System.out.println("TextReader: WARNING: File could not be read.");
				}
			} else {
				
				// without this StringBuilder it takes forever because of
				// inefficiencies in appending to strings and garbage
				// collection
				StringBuilder store = new StringBuilder("");
				
				String line;
				
				while (in.hasNextLine()) {
					
					// get rid of leading and trailing whitespace, mostly for
					// formatting purposes
					line = in.nextLine().trim();
					
					// excessively critical addition of single space
					// 		specifically, it makes sure that there is 
					// 		whitespace between lines in the string which
					//		lets later regex matching work
					line = line  + " ";
					
					store.append(line);
				}
				
				// I still want a normal String for the final product
				text = store.toString();
				
				// something is wrong if text is empty at this point
				if (text.isEmpty()) {
					
					System.out.println("TextReader: WARNING: File could not be read.");
				}
			}
				
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Removes lines up to and including start and from the last line up to and
	 * including end. Finds start and end by string matching.
	 * 
	 * Ignores start and end if they are empty.
	 * 
	 * @param start		line from text file
	 * @param end 		line from text file
	 */
	public void pruneLines(String start, String end) {
		
		// remove lines from the first up to and excluding start
		if (!start.equals("")) {
			
			// find start
			for (int i = 0; i < lines.size(); i++) {
				
				if (lines.get(i).equals(start)) {
					
					// remove lines and break
					for (int j = 0; j < i; j++) {
						
						lines.remove(0);
					}
					
					break;
				}
			}
		}
		
		// remove lines from the last up to and excluding end
		if (!end.equals("")) {
			
			// find end
			for (int i = lines.size() - 1; i >= 0; i--) {
				
				if (lines.get(i).equals(end)) {
					
					// remove lines and break
					for (int j = lines.size(); j > i + 1; j--) {
						
						lines.remove(lines.size() - 1);
					}
					
					break;
				}
			}
		}
	}
	
	
	/**
	 * Lines accessor
	 * 
	 * @return 		lines
	 */
	public ArrayList<String> getLines() {
		
		return lines;
	}
	
	
	/**
	 * Reinitializes lines
	 */
	public void resetLines() {
		
		lines = new ArrayList<>();
	}
	
	
	/**
	 * Text accessor
	 * 
	 * @return		text
	 */
	public String getText() {
		
		return text;
	}
	
	
	/**
	 * Sets text to empty string 
	 */
	public void resetText() {
		
		text = "";
	}
}
