import java.util.*;

public class TandemSASI {

    // return the longest common prefix of s and t
    public static String lcp(String s, String t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != t.charAt(i))
                return s.substring(0, i);
        }
        return s.substring(0, n);
    }


    // return the longest repeated string in s
    public static void lrs(String s, int score_threshold) {
    	SASI a = new SASI();
    	 int n  = s.length();
    	int[] result = a.makeSuffixArrayByInducedSorting(s,256);
    	String[] suffixes = new String[n];
    	for(int i=0;i<n;i++){
    		//res.add(s.substring(result[i], s.length()));
    		suffixes[i] = s.substring(result[i], n);
    	}
        // form the N suffixes
       
//        String[] suffixes = new String[n];
//        for (int i = 0; i < n; i++) {
//            suffixes[i] = s.substring(i, n);
//        }

//        // sort them
//        Arrays.sort(suffixes);
//        for(int i=0;i<n;i++){
//          System.out.println(suffixes[i]);
//        }

        // find longest repeated substring by comparing adjacent sorted suffixes
        String lrs = "";
        ArrayList<String> res= new ArrayList<String>();
        for (int i = 0; i < n-1; i++) {
            String x = lcp(suffixes[i], suffixes[i+1]);
            if(x.length()>=2){
              res.add(x);
            }
            if (x.length() > lrs.length()){
              lrs = x;
            }
        }
        
        for(int i=0;i<res.size();i++){
          // System.out.println(res.get(i));
        }
       System.out.println(lrs); 
    }


    public void start(String sequenceA, int score_threshold){
    	lrs(sequenceA, score_threshold);
    }
//
//    // read in text, replacing all consecutive whitespace with a single space
//    // then compute longest repeated substring
//    public static void main(String[] args) {
//        String s = "srisrisrisir";
//        s = s.replaceAll("\\s+", " ");
//        
//        System.out.println("'" + lrs(s) + "'");
//    }
}