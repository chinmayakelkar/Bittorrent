package logger;

import PropertyReader.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SimpleLogger extends Logger{
	

	private String logFileName;

	public static SimpleLogger logger = null;
	

	private FileHandler fileHandler;
	
	
	private String peerID;
	
	
	private SimpleDateFormat formatter = null;
		
	
	public SimpleLogger(String peerID, String logFileName, String name) {
		super(name, null);
		this.logFileName = logFileName;
		this.setLevel(Level.FINEST);
		this.peerID = peerID;
	}
	
	
	public void initialize() throws SecurityException, IOException{

		fileHandler = new FileHandler(logFileName);
		fileHandler.setFormatter(new SimpleFormatter());
		formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss a");
		this.addHandler(fileHandler);
	}

	

	@Override
	public synchronized void log(Level level, String msg) {

		super.log(level, msg+"\n");
	}
	
	
	public void close(){
		try {
			if(fileHandler!=null){
				fileHandler.close();
			}			
		} catch (Exception e) {			
			System.out.println("Unable to close logger.");
			e.printStackTrace();
		}
	}


	public void warning(String msg){
		Calendar c = Calendar.getInstance();		
		String dateInStringFormat = formatter.format(c.getTime());
		this.log(Level.WARNING, "["+dateInStringFormat+"]: Peer [peer_ID "+peerID+"] "+msg);
	}
	
	public synchronized void info(String msg)
	{
		Calendar c = Calendar.getInstance();
		String dateInStringFormat = formatter.format(c.getTime());
		this.log(Level.INFO, "["+dateInStringFormat+"] : "+msg);
	}


	public static SimpleLogger getLogger(String peerID) {
		if (logger == null) {

			logger = new SimpleLogger(peerID, Constants.LOG_FILE_NAME_PREFIX + peerID + ".log", Constants.LOGGER_NAME);
			try {
				logger.initialize();
			} catch (Exception e) {
				logger.close();
				logger = null;
				System.out.println("Unable to create or initialize logger");
				e.printStackTrace();
			}
		}
		return logger;
	}
}
