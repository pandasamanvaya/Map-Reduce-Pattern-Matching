import java.io.*;
import java.util.*;


public class QuickSearch{

	private static String ReadFile(String fileName){
		String data = "";
		
		try{
			File file = new File(fileName);
			Scanner fileReader = new Scanner(file);

			while(fileReader.hasNextLine()){
				data += fileReader.nextLine();
			}
			fileReader.close();
		}catch(FileNotFoundException e){
			System.out.println("File not found : "+fileName);
		}
		return data;
	}

	private static HashMap<Character, Integer> preProcess(String key){
		HashMap<Character, Integer> table = new HashMap<Character, Integer>();

		for(int i=0; i < key.length()-1; i++){
			table.put(key.charAt(i), key.length()-i-1);
		}
		if(table.containsKey(key.charAt(key.length()-1)) == false)
			table.put(key.charAt(key.length()-1), key.length());

		return table;
	}

	private static int findKey(String data, String key){

		int count = 0, i=0;
		int s = key.length(), t = data.length();
		HashMap<Character, Integer> table = preProcess(key);
		while(i < t-s+1){
			Character last = data.charAt(i+s-1);
			boolean equal = true;
			for(int j = i+s-1; j >= i; j--){
				if(data.charAt(j) != key.charAt(j-i)){
					equal = false;
					if(table.containsKey(last) == false)
						i += s;
					else
						i += table.get(last);
					break;
				}
			}
			if(equal){
				count++;
				i++;
			}
		}
		return count;
	}

	public static void main(String[] args){
		String data = ReadFile(args[0]);
		String key = ReadFile(args[1]);

		System.out.println("No.of occurences : "+findKey(data, key));
	}
}