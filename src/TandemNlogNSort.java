import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TandemNlogNSort {

	public static String lcp(String string1, String string2) {
		int n = Math.min(string1.length(), string2.length());
		for (int i = 0; i < n; i++) {
			if (string1.charAt(i) != string2.charAt(i))
				return string1.substring(0, i);
		}
		return string1.substring(0, n);
	}

	public static void lrs(String sequence) {
		int length  = sequence.length();
		String[] suffixes = new String[length];
		for(int i=0;i<length;i++){
			suffixes[i] = sequence.substring(i, length);
		}
		Arrays.sort(suffixes);
		int maxLength = 0;
		HashSet<String> lrs= new HashSet<String>();
		ArrayList<String> res= new ArrayList<String>();
		for (int i = 0; i < length-1; i++) {
			String commonString = lcp(suffixes[i], suffixes[i+1]);
			if(commonString.length()>=2){
				res.add(commonString);
			}

			if(commonString.length() == maxLength){
				lrs.add(commonString);
			}
			else if (commonString.length() > maxLength){
				maxLength = commonString.length();
				lrs.clear();
				lrs.add(commonString);
			}
		}

		System.out.println(lrs); 
	}


	public void start(HashMap<String,String> database){
		System.out.println("From Suffix arrays nlogn sorting:");
		for(String key: database.keySet()){
			System.out.print("ID: "+key+" ");
			String sequence = database.get(key);
			lrs(sequence);
		}
	}
}