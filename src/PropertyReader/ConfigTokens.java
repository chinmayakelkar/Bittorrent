package PropertyReader;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

public class ConfigTokens {
	
	private static final Hashtable<String, String> tokenMap = new Hashtable<String, String>();

	static{
		try {

			BufferedReader configFileReader =  new BufferedReader(new InputStreamReader(new FileInputStream(Constants.CONFIGURATION_FILE)));
			
			String line = configFileReader.readLine();
			
			while(line != null){
				String tokens[] = line.trim().split(" ");
				tokenMap.put(tokens[0].trim(), tokens[1].trim());
				line = configFileReader.readLine();
			}
			configFileReader.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Exception: "+e.getMessage());
			throw new ExceptionInInitializerError("Error Loading properties");
		}
	}
	
	public static String returnPropertyValue(String value){
		return tokenMap.get(value);
	}
}
