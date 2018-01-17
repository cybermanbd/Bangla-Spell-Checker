import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Formatter;

public class ConditionalFreqDist{
	
	

	public TreeMap <String, FreqDist>map;
	public int N;		// Total number of n-grams
	PrintWriter pr;
	public ConditionalFreqDist() throws IOException{
		map = new TreeMap<String, FreqDist>();
		pr = new PrintWriter(new File("BangTest11.txt"));
		 
	}
	
	
	/* This method increase the frequency of string 'v' with history 'condition' */
	public void inc(String condition[], String v) throws IOException{
		String con = "";
		
		for (String s : condition) {
			con = con + " " + s;
		}
		
		if (map.containsKey(con)) {
			
			FreqDist f = (FreqDist)map.get(con);
			f.inc(v);
			map.remove(con);			
			map.put(con, f);
			
			
		} else {
			N = N + 1;
			FreqDist fd = new FreqDist();
			fd.inc(v);
			map.put(con, fd);
			
			
		}
		
	}
	
	/* This method returns the total number of n_gram of history 'condition'*/
	public int size(String condition[]){
		String con = "";
		
		for (String s : condition) {
			con = con + " " + s;
		}
		
		if (map.containsKey(con)) {
			FreqDist f = (FreqDist)map.get(con);
			
			return f.size();			
		} 
		else return 0; 
	}
	
	/* This method finds the frequency of the string 'v' in corpus based on history 'condition'condition. */
	public int count(String []condition, String v){
		int c = 0;
		String con = "";
		
		for (String s : condition) {
			con = con + " " + s;
		}
		
		if (map.containsKey(con)) {
			FreqDist fd = (FreqDist)map.get(con);
			c = fd.count(v);
			
		}
		
		return c;
	}
	
	/* This method calculates the conditional probability P(v|condition). */
	public double condPr(String []condition, String v){
		int total = 0;
		
		for(String s : samples(condition)) {
			total = total + count(condition, s);
		}
		if(total!=0){
			
		   return (double)count(condition,v) / total;
		}
		else
		    return 0.0;
	}
	
	/* This mehod calculates the string such that P(string|condition) is maximum */
	public String maximum(String []condition){
		int m = 0;
		String max = "";
		
		for (String s : samples(condition)) {
			int t = count(condition, s);
			
			if (m < t) {
				m = t;
				max = s;
			}
		}
			
		return max;
	}

	/* This method finds the 'i_th' highest frequency string by sorting the strings in descending order */
	public Pair maxima(String []condition, int i_th){
		int m, i;
		Pair p = new Pair();
		String n_gram[] = new String[N];
		int frequency[] = new int[N];
		String max = "";
		m = i = 0;
		
		for(String s : samples(condition)) {
			n_gram[i] = s;
			frequency[i] = count(condition, s);
			
			/* Performing insertion sort */
			int j = i - 1;
			while(j > -1 && frequency[j] < count(condition, s)) {
			     n_gram[j+1] = n_gram[j];
			     frequency[j+1] = frequency[j];
			     j--;
			}
			n_gram[j+1] = s;
			frequency[j+1] = count(condition, s);
		        
			/* Printing n-gram iteration-wise based on history 'codition' */
		/*	System.out.print("For " + i+ "-th iteration: ");
			for(int k = 0; k <= i ; k++ )			
			    System.out.print(n_gram[k] + " | ");
			System.out.println("\n");
			for(int k = 0; k <= i ; k++ )			
			    System.out.print(frequency[k] + " | ");
			System.out.println("\n");*/
			
			i++;
		}	// End of the for loop 'for(String s : samples(condition))'
			
		/* Returning 'i_th' highest frequency string */
		p.word = n_gram[i_th];
		p.frequency = frequency[i_th];
		return p;
	}
	
	/* This method returns the sample strings, i.e. words based on the history */
	public String[] samples(String []condition){
		String con = "";
		
		for (String s : condition) {
			con = con + " " + s;
		}
		FreqDist fd = (FreqDist)map.get(con);
		
		if(fd != null)
		  return fd.samples();
		else{
		     String n[] = {""};
		     return n;
		}
	}

	public String[][] condition(){
		int i = 0;
		String s[][] = new String[N][];		
		
		for(String key : map.keySet()) {
			int j = 0;
			StringTokenizer st = new StringTokenizer(key);
			s[i] = new String[st.countTokens()];
			
			while (st.hasMoreTokens()) {
				s[i][j] = st.nextToken();
				j++;
			}
			i++;
		}
	
		return s;
	}
	public void printMap() throws IOException{
	
		
		Set<String> keys = map.keySet();
		
        for(String key: keys){
			FreqDist fd = (FreqDist)map.get(key);
			Set<String> kep = fd.map.keySet();
			for(String ket: kep){
			pr.print(key+" "+ket+" "+fd.map.get(ket)+"\n");
            
        }
	   }
		
        
	
}
}
