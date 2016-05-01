package PropertyReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import peerToPeer.PeerDetails;

public class Configurations {

	public LinkedHashMap<String,PeerDetails> peerDetailsMap = null;
	
	private static Configurations  PeerConfiguration = null;
	
	private Configurations(){
		
	}
	
	public static Configurations createInstance(){
		if( PeerConfiguration == null){
			 PeerConfiguration = new  Configurations();
			 PeerConfiguration.initialize();
		}
		return  PeerConfiguration;
	}
	
	public boolean initialize(){
		
		
		try {

			BufferedReader configFileReader =  new BufferedReader(new InputStreamReader(new FileInputStream(Constants.PEER_INFO_FILE)));
			
			peerDetailsMap = new LinkedHashMap<String,PeerDetails>();
			
			String line = configFileReader.readLine();
			
			PeerDetails peerInfoInstance = null;
			while(line != null){
				peerInfoInstance = new PeerDetails();
				String tokens[] = line.trim().split(" ");
				peerInfoInstance.setPeerID(tokens[0]);
				peerInfoInstance.setHostAddress(tokens[1]);
				peerInfoInstance.setPortNumber(Integer.parseInt(tokens[2]));
				
				if(tokens[3].equals("1")){
					peerInfoInstance.setFileExists(true);
				}else{
					peerInfoInstance.setFileExists(false);
				}
				
				peerDetailsMap.put(tokens[0],peerInfoInstance);
				
				line = configFileReader.readLine();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return true;
	}
	
	public HashMap<String, PeerDetails> getPeerInfoMap() {
		return peerDetailsMap;
	}
}
