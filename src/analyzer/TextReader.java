package analyzer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads texts files and stores as ArrayLists.
 * Once stored, can prune beginning and end.
 * 
 * @author rekmarks
 *
 */
public class TextReader {
	
	/**
	 * lines 		the lines from the text file
	 */
	private ArrayList<String> lines;
	
	
	/**
	 * Constructor
	 * Initializes lines
	 */
	public TextReader() {
		
		lines = new ArrayList<>();
<<<<<<< Updated upstream
		
=======
>>>>>>> Stashed changes
	}
	
	/**
	 * Reads file at specified path
	 */
	public void readFile(String path) {
		
		try (Scanner in = new Scanner(new File(path), "utf-8")) {
						
			while (in.hasNextLine()) {
				
				lines.add(in.nextLine());
			}
			
			// something is wrong if lines is empty at this point
			if (lines.isEmpty()) {
				
				System.out.println("__TextReader: WARNING: File could not be read\n");
				
			} else {
				
				System.out.println("__TextReader: File read successfully\n");
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
			
			System.out.println("__TextReader: Beginning pruned\n");
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
			
			System.out.println("__TextReader: End pruned\n");
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
	
<<<<<<< Updated upstream
}
=======
	
	/**
	 * Sets lines to null
	 */
	public void forgetLines() {
		
		lines = null;
	}
}
>>>>>>> Stashed changes
