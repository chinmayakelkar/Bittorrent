package logger;

import java.io.BufferedReader;
import java.io.IOException;


public class OutputDisplayer implements Runnable{
	
	BufferedReader reader;
	String peerID;
	
	
	
	public void run(){
		try {
			String line = null;
			while( (line = reader.readLine()) != null ){
				System.out.println("["+peerID+"]: "+line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public OutputDisplayer(String peerID, BufferedReader reader){
		this.reader = reader;
		this.peerID = peerID;
	}
}
