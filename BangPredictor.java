import java.io.*;
import java.util.StringTokenizer;
import java.util.*;

public class BangPredictor{
	

	
	/* This method trains bigram */
	public static ConditionalFreqDist trainBigram(String s) throws IOException{
		StringTokenizer stringTokens = new StringTokenizer(s);
		ConditionalFreqDist bigramConditionalFreqDist = new ConditionalFreqDist();
		
		String prev[] = {"<s>"};
		
		
		while (stringTokens.hasMoreTokens()) {
		
			if (prev[0].endsWith("।")) {
				prev[0] = "<s>";			
			}
			
			String tk = stringTokens.nextToken();
			
			bigramConditionalFreqDist.inc(prev, tk);
			
			prev[0] = tk;
			
		}
		
		return bigramConditionalFreqDist;
	}

	/* This method trains trigram */
	public static ConditionalFreqDist trainTrigram(String s) throws IOException{
		StringTokenizer stringTokens = new StringTokenizer(s);
		ConditionalFreqDist trigramConditionalFreqDist = new ConditionalFreqDist();
		
		String prev[] = {"<s>", "<s>"};
		String tk="<s>";	
		while (stringTokens.hasMoreTokens()) {
		
		    
			if (prev[0].endsWith("।")) {
				prev[0] = "<s>";
				
			}
			
			prev[1]= stringTokens.nextToken();
			String temp=prev[1];
			if (tk.endsWith("।")) {
				
				prev[1] = "<s>";
			}
			
			trigramConditionalFreqDist.inc(prev, tk);
			
			prev[0] = tk;
			tk = temp;
		}

		return trigramConditionalFreqDist;
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
			         /* while((sr = br.readLine()) != null) {
			          	rslt_sr = rslt_br.readLine();
			          	rslt_sentence += rslt_sr;
			          	sentenceTokens = new StringTokenizer(rslt_sentence);
		                        while(sentenceTokens.hasMoreTokens())
                                        rslt_tk = sentenceTokens.nextToken();   
				     	rslt_sentence = "";
					    
				     	sentence += sr;
				        //System.out.println("sentence" + sentence);
				     	sentenceTokens = new StringTokenizer(sentence);
				     	String prev1[] = {"<s>"};
				     	while(sentenceTokens.hasMoreTokens()) {
				     	     if(prev1[0].endsWith("|"))
                                     	 		    prev1[0] = "<s>";			
					     tk = sentenceTokens.nextToken();
					     prev1[0] = tk;
					}
					    
					int i;
					track = i = 0;
					N = biDist.size(prev1);
					while(i < N && i < w) {
						String word_point = (biDist.maxima(prev1, i)).word;
						if(word_point != null && word_point.compareTo(rslt_tk) == 0){
							   		right_count += (float)(w - i) / w;
						                        track = i;
						                        break;
						}
						i++;
					}
						  
					/* Writing first line of pattern */
					/*if(i < N && i < w) {			// If prediction matches
					  predicted = (biDist.maxima(prev1, track)).word;
					  pr.print(sentence+" "+ predicted + "(" + (track+1) + "-th attempt)" +"\n");
					  
					}
					else {
					    predicted = "<failure>";
					    pr.print(sentence + "  " + "<failure>" + "\n");
					}
					    
				        total_count++;
					
					/* Writing second line of pattern */
					/*if(predicted.compareTo("<failure>") != 0) {
				          sentence = sentence + " " + (biDist.maxima(prev1, track)).word;
				          sentenceTokens = new StringTokenizer(sentence);
					  prev1[0] = "<s>";
					  pr.print("P("+sentence+") = ");
					  tk = sentenceTokens.nextToken();
					  pr.print("P("+tk+" | "+prev1[0]+") ");
					  while(sentenceTokens.hasMoreTokens()){
					        prev1[0] = tk;
						tk = sentenceTokens.nextToken();
						pr.print("* P("+tk+" | "+prev1[0]+") ");
					  }
					  pr.print("\n");
					}  // End of if(predicted.compareTo("<failure>") != 0)
					
				        /* Writing third line of pattern */
					/*if(predicted.compareTo("<failure>") != 0) {
					  sentenceTokens = new StringTokenizer(sentence);
					  prev1[0] = "<s>";
					  pr.print("P("+sentence+") = ");
					  tk = sentenceTokens.nextToken();
					  pr.print(""+biDist.condPr(prev1, tk));
					  while(sentenceTokens.hasMoreTokens()){
					        prev1[0] = tk;
						tk = sentenceTokens.nextToken();
						pr.print(" * "+biDist.condPr(prev1, tk));
					  }
					}
				        pr.print("\n\n");
				        sentence = "";
			          }	// End of 'while((sr = br.readLine()) != null)'
				  
				  pr.println("------------------------------------------X-----------------------------------------");
				  pr.print("Total number of testing smaples = " + total_count + "\n");
				  pr.print("Accuracy = " + ((float)right_count / total_count) * 100 + "%\n");
				  total_count = right_count = 0;
				  rslt_sentence = "";*/
				  
				  //br.close();
			          //pr.close();
			          break;
			          
			 /* Trigram model*/ 
			 case '3':
			          ConditionalFreqDist triDist = trainTrigram(s);
			          triDist.printMap();
			       
			          while((sr = br.readLine()) != null) {
				     	sentence += sr;
				        //System.out.println("sentence" + sentence);
				     	sentenceTokens = new StringTokenizer(sentence);
					  
                          String prev2[] = {"<s>", "<s>"};
		                  while(sentenceTokens.hasMoreTokens()) {
                               if(prev2[0].endsWith("।")){
                               	   prev2[0] = "<s>";
                               }
                              
                               			prev2[1]= sentenceTokens.nextToken();
			                         String temp=prev2[1];
			                         if (tk.endsWith("।")) {
				
				                          prev2[1] = "<s>";
			                           }
		                       if(!WList.contains(tk))
							{
								int i;
                          track = i = 0;
                          N = triDist.size(prev2);
                          pr.println("Previous word: "+prev2[0]+"Wrong word "+tk+" After word: "+prev2[1]+"total Word "+N);
                          while(i < N ) {
                               	   Pair word = (triDist.maxima(prev2, i));
                               	   String word_point=word.word;
                               	   int freq=word.frequency;
                               	  pr.println(" Suggested "+word_point+" Frequency: "+freq);
			                  i++;
			              }
							   
							}
							prev2[0] = tk;
			                 tk = temp;
		                       
		                  }
		                   sentence = "";
					 }
		                  
		               
		                 
				  
			          br.close();
			          pr.close();
			          break;
		         
			          
			 /* Backoff model*/
		         
				  
				 /* Deleted Interpolation */
		         
					   
			  default:
			          
		       } // End of switch()		   
		   } // End of if(Character.digit(choice, 10) <= 6)
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

