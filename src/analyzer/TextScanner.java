package analyzer;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author rekmarks
 *
 */
public class TextScanner {
	
	/**
	 * 
	 */
	private ArrayList<String> textLines;
	
	
	/**
	 * 
	 */
	public TextScanner() {
		
		textLines = new ArrayList<>();
		
	}
	
	/**
	 * Method for reading file.
	 */
	public void readFile(String path) {
		
		try {
			
			File f = new File(path);
			Scanner in = new Scanner(f);
			
			while (in.hasNextLine()) {
				
				textLines.add(in.nextLine());
			}
			
			if (textLines.isEmpty()) {
				
				System.out.println("Warning: file could not be read.");
			} else {
				
				System.out.println("File read successfully.");
			}
			
			// remove header row, which contains no data
			textLines.remove(0);
			
			in.close();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 */
	public void extractBookChapters() {
		
	}
	
	

}
