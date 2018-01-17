import java.io.*;
import java.util.StringTokenizer;
import java.util.Formatter;
import java.util.*;
public class SpellChecker{
	

	
	/* This method trains bigram */
	public static ConditionalFreqDist trainBigram(String s) throws IOException{
		StringTokenizer stringTokens = new StringTokenizer(s);
		ConditionalFreqDist bigramConditionalFreqDist = new ConditionalFreqDist();
		
		String prev[] = {"<s>"};
		
		
		while (stringTokens.hasMoreTokens()) {
		
			if (prev[0].endsWith("|") || prev[0]=="\n") {
				prev[0] = "<s>";			
			}
			
			String tk = stringTokens.nextToken();
			
			bigramConditionalFreqDist.inc(prev, tk);
			
			prev[0] = tk;
			
		}
		
		return bigramConditionalFreqDist;
	}

	
	
	/* Main method */
	public static void main(String [] args) throws IOException{
		
		/* Capturing all training data from corpus */	
		File inputFile = new File("BengaliWordList_40.txt");
		BufferedReader rslt_br;
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		LinkedList<String> WList = new LinkedList<String>();
		int c, w = 100, N = 0;
		String s = "";
		String rslt_sr;
		String sr = br.readLine();
 
		while (sr != null) { 
			s += sr + "\n";
			
			sr = br.readLine();
			
		} 
		br.close();
		
		StringTokenizer st = new StringTokenizer(s);
		
		while(st.hasMoreTokens()) {
			WList.add(st.nextToken());
		}
		s="";
		inputFile = new File("BangCorpus.txt");
		br = new BufferedReader(new FileReader(inputFile));
		sr = br.readLine();
		
		while (sr != null) { 
			s += sr + "\n";
			
			sr = br.readLine();
			
		} 
		br.close();
		
       	        
		/* Getting choice from user */
		char choice;
		do {
		   br = new BufferedReader(new InputStreamReader(System.in));
		   System.out.println("Choose from the following training model: (1/2/3/4/5/6)");
		   System.out.println("*********************X********************");
		   System.out.println("*	1. Unigram			*");
		   System.out.println("*	2. Bigram			*");			
		   System.out.println("*	3. Trigram			*");
		   System.out.println("*	4. Backoff			*");
		   System.out.println("*	5. Deleted Interpolation        *");
		   System.out.println("*	6. Exit				*");
		   System.out.println("*********************X********************");
		   System.out.print("Input your choice: ");
		   choice = br.readLine().charAt(0);
		   if(choice != '6') {
		     System.out.print("Enter maximum prediction size: ");
		     w =  (new Integer(br.readLine())).intValue();
		     System.out.println();
		   }
				
		   /* Capturing all test data */
		   if((Character.digit(choice, 10) >= 1) && (Character.digit(choice, 10) <= 5)) {
		       System.out.print("Enter the name of the test file: ");
		       String str = br.readLine();
		       inputFile = new File(str);
		       br = new BufferedReader(new FileReader(inputFile));
		   
		       /*rslt_br = new BufferedReader(new InputStreamReader(System.in));        
		       System.out.print("Enter the name of the result file: ");
		       String rslt_str = rslt_br.readLine();
		       File resultFile = new File(rslt_str);
		       rslt_br = new BufferedReader(new FileReader(resultFile));*/
		   
		       PrintWriter pr = new PrintWriter(new File("BangSpell.txt"));
		       String sentence = "";
		       String rslt_sentence = "";
		       
		       StringTokenizer sentenceTokens = new StringTokenizer("");
		       String tk = "";
		       String rslt_tk = "";
		   
		       String predicted;	// The predicted word
		       int track;
		   
		       float total_count = 0.0f, right_count = 0.0f;
		       boolean di_sorting = false;		// Indicator for sorting in deleted interpolation
		   
		       /* Predicting output based on choice of user */
		       switch(choice) {
			 /* Unigram */
		        
			          
			     /* Bigram model */
		         case '2':
					   ConditionalFreqDist biDist = trainBigram(s);
					   biDist.printMap();
			         while((sr = br.readLine()) != null) {
				     	sentence += sr;
				        //System.out.println("sentence" + sentence);
				     	sentenceTokens = new StringTokenizer(sentence);
				     	String prev[] = {" <s>"};
				     	while(sentenceTokens.hasMoreTokens()) {
                                    
				     	   if (prev[0].endsWith("|")) {
				                 prev[0] = " <s>";			
			                   }  			
					     tk = sentenceTokens.nextToken();
							if(WList.contains(tk))
							{
								if(biDist.map.containsKey(prev[0])){
                                      FreqDist fd = (FreqDist)biDist.map.get(prev[0]);
			                           if(fd.map.containsKey(tk)){
			                    
			                                 pr.println(prev[0]+" "+tk+" Correct word");
                       
                                         }
                                       else{
								           pr.println(prev[0]+" "+tk+" contextualy Wrong Word!");
							              }
							   }
							   else {
								pr.println(prev[0]+" "+tk+" contextualy1 Wrong Word!");
							        }
							   
							}
							
					       else{
							 pr.println(tk+" Wrong Word!");
						   }
						   prev[0] = " "+tk;
					}
						  sentence = "";
					 }
							
					    
					
				  
				  br.close();
			          pr.close();
			          break;
			          
			 /* Trigram model*/ 
		         
			          
			 /* Backoff model*/
		         
				  
				 /* Deleted Interpolation */
		         
					   
			  default:
			          
		       } 
		   }// End of switch()		   
		    // End of if(Character.digit(choice, 10) <= 6)
		   else if((Character.digit(choice, 10) < 1) || (Character.digit(choice, 10) > 7)) {
			  System.out.println("Wrong choice!");
			  System.out.println("Please enter a choice again.");
		   }
		   else {
			 System.out.println("Thank you.\n");
		   }
											
		} while(choice != '6');
	} // End of main()
	
}

