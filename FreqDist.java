import java.util.*;

public class FreqDist{
	
	public	TreeMap <String, Integer> map;
	private	int tokens;	
	private	int N;	// Total number of unigrams
	private	String unigram[];
	private	int frequency[];
	private	boolean sorted = false;

	public FreqDist(){
		map = new TreeMap<String, Integer>();
	}
	
	public void inc(String k){
	
		if (map.containsKey(k)) {		
			Integer i = (Integer)map.get(k);
			map.remove(k);
			map.put(k, new Integer(i.intValue()+1));
		} else {
			N = N+1;
			map.put(k, new Integer(1));
		}
		
		tokens++;
	}
	
	/* This method returns the total number of unigram */
	public int size(){
	
		return N;
	}
	
	/* This method calculates the probability of the string 'k' */
	public double pr(String k){
	
		return (double)count(k) / tokens;
	}
	
	/* This method finds the frequency of the string 'k' in corpus */
	public int count(String k){
		int c = 0;
	
		if (map.containsKey(k)) {
			c = ((Integer)map.get(k)).intValue();		
		}
		
		return c;
	}
	
	/* This method finds the string with highest probability */
	public String maximum(){
		int m = 0;
		String max = "";
	
		for (String s : samples()) {
			if (m < count(s)) {
				m = count(s);
				max = s;
			}
		}
			
		return max;
	}

	/* This method finds the 'i_th' highest frequency string by sorting the strings in descending order */
	public Pair maxima(int n){
		int i = 0;
		Pair p = new Pair();
					
		if(sorted == false)
			sortSamples();
		
		p.word = unigram[n];
		p.frequency = frequency[n];
		return p;
	}

	
	/* This method returns the sample strings, i.e. words */
	public String[] samples() {
		int i = 0;
		String[] s = new String[N];		
			
		for(String key : map.keySet()) {
			s[i] = key;
			i++;
		}
		
		return s;
	}
	
	/* This method returns the sample strings, i.e. words */
	public void sortSamples() {
		int i = 0;
		
		if(sorted == false) {
		  unigram = new String[N];
		  frequency = new int[N];
		  sorted = true;
		  
		  for(String key : map.keySet()) {
			unigram[i] = key;
			frequency[i] = count(key);
			
			int j = i - 1;										/* Performing insertion sort */
			while(j > -1 && frequency[j] < count(key)) {
			     unigram[j+1] = unigram[j];
			     frequency[j+1] = frequency[j];
			     j--;
			}
			unigram[j+1] = key;
			frequency[j+1] = count(key);
		    
			i++;
		  }  // End of for loop
		} // Enf of if
	}  // End of 'void sortSamples()'
}
