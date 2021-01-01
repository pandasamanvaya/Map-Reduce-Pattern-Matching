import java.io.*;
import java.util.*;


public class NaiveSearch{

	public static String ReadFile(String fileName){
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

	public static int findKey(String data, String key){
		int count = 0;
		for(int i=0; i < data.length()-key.length()+1; i++){
			if(data.substring(i, i+key.length()).equals(key))
				count++;
		}
		return count;
	}

	public static void main(String[] args){
		String data = ReadFile(args[0]);
		String key = ReadFile(args[1]);

		System.out.println("No.of occurences : "+findKey(data, key));
	}
}