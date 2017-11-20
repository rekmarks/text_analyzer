package analyzer;

import java.util.ArrayList;

public class Loid {

	public static void main(String[] args) {
		
<<<<<<< Updated upstream
		Tester tester = new Tester();
		
		tester.testInput();
=======
		TextAnalyzer ta = new TextAnalyzer();
		
		String[] range = {"BOOK FIRST--A JUST MAN", "of itself, as the night comes when day is gone.]"};
		
		ta.runSuite("texts/les-mis.txt", range, "\"");
		
//		Tester tester = new Tester();
////		
//		tester.testInput();
////		
//		tester.testWords();
		
//		tester.testQuotes();
>>>>>>> Stashed changes
		
		tester.testWords();
		
//		String sune = "catchedcatched";
//		
//		int d;
//				
//		for (char c : sune.toCharArray()) {
//			
//			d = c; 
//			
//			System.out.println(c + " : " + d);
//		}
	}
	
	public void Store() {
		
		// ArrayList stopList generator
//		/**
//		 * @param list
//		 */
//		public void setStopList(ArrayList<String> list) {
//			
//			stopList = list;
//			
//			for (int i = 0; i < stopList.size(); i++) {
//				
//				String s = stopList.get(i);
//				
//				s = s.trim();
//				
//				stopList.remove(i);
//				
//				if (!s.isEmpty()) {
//					
//					stopList.add(i, s);
//				}
//			}
//		}
	}

}
