package Messages;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

import PropertyReader.Constants;
import PropertyReader.ConfigTokens;

public class ChunkHandler {
	
	
	public static final String LOGGER_PREFIX = ChunkHandler.class.getSimpleName();

	private static ChunkHandler chunkhandlr;	
	int chunkSize;
	int chunkCount ;
	RandomAccessFile outStream;
	FileInputStream inStream;
	private static BitFieldHandler bitField ;

	private ChunkHandler(){
		
	}
		
	
	synchronized public static ChunkHandler createChunkHanlder(boolean isFileExists, String peerID){
		if(chunkhandlr == null){
			chunkhandlr = new ChunkHandler();
			boolean isSuccessfullyInitialized = chunkhandlr.initChunkProp(isFileExists,peerID);
			if(isSuccessfullyInitialized == false){
				chunkhandlr = null;
			}
	
		}
		return chunkhandlr;
	}
	
	public boolean initChunkProp(boolean isFileExists, String peerID){
		
		if(ConfigTokens.returnPropertyValue("PieceSize")!=null)
			chunkSize = Integer.parseInt(ConfigTokens.returnPropertyValue("PieceSize"));
		
			if(ConfigTokens.returnPropertyValue("FileSize")!= null)
			chunkCount = (int) Math.ceil(Integer.parseInt(ConfigTokens.returnPropertyValue("FileSize")) / (chunkSize*1.0)) ;
		

		try
		{
			bitField = new BitFieldHandler(chunkCount);
			
			if(isFileExists){
				bitField.setAllBits();
			}
			
			String outputFileName = new String();			
			outputFileName = ConfigTokens.returnPropertyValue("FileName");
			
			String directoryName = "peer_"+peerID;
			File directory = new File(directoryName);
			
			if(isFileExists == false){

				directory.mkdir();

			}
			
			outputFileName = directory.getAbsolutePath()+"/"+outputFileName;
			
			File outFile = new File(outputFileName);
			if(outFile.exists() == true){
		
			}
			
			outStream = new RandomAccessFile(outputFileName,"rw");
			
			outStream.setLength(Integer.parseInt(ConfigTokens.returnPropertyValue(Constants.FILE_SIZE)));
			
			return true;
			
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		  return false;
		}	
	}
	
	synchronized public void close(){
		//close outputfilestream
		try {
			if(outStream != null){
				outStream.close();
			}
			
			if(inStream != null){
				inStream.close();
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
				
	}
	
	synchronized public Chunk getdata(int number){
		
		Chunk readPiece = new Chunk(chunkSize);
		if(bitField.getBitFieldOn(number))
		{
			//have to read this piece from my own output file.
			try{
				byte[] readBytes = new byte[chunkSize];
				outStream.seek(number*chunkSize);
				int dataSize = outStream.read(readBytes);
				
				if(dataSize != chunkSize){
					byte[] newReadBytes = new byte[dataSize];
					for(int i=0 ; i<dataSize ; i++){
						newReadBytes[i] = readBytes[i];
					}
					readPiece.setData(newReadBytes);
				}else{
					readPiece.setData(readBytes);
				}
				
				return readPiece;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			}
		}
		else {
			
			return null;
		}
	}
	
	synchronized public boolean writePieceToPeer(int number,Chunk piece){
		
		if(!bitField.getBitFieldOn(number))
		{
			try {
				
				outStream.seek(number*chunkSize);
				outStream.write(piece.getData());
				
				bitField.setBitFieldOn(number, true);
				
				return true;
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			
			return false;
		}
		
	}
	
	synchronized public int[] getMissingPieceNumberArray(){
	
		int i = 0, j = 0;
		
		while( i < bitField.getSize())
		{
			if(bitField.getBitFieldOn(i) == false)
			{				
				j++;
			}
			i++;											
		}
	
		int[] missing = new int[j];
		j = 0;
		i = 0;
		while( i < bitField.getSize())
		{
			if(bitField.getBitFieldOn(i) == false)
			{				
				missing[j] = i;
				j++;
			}
			i++;											
		}				
		return missing;

	}
	
	synchronized public int[] getAvailablePieceNumberArray(){

		int i = 0, j = 0; 		
		int[] available = new int[chunkCount];
		while( i < bitField.getSize())
		{
			if(bitField.getBitFieldOn(i) == true)
			{
				available[j] = i;
				j++;
			}
			i++;								
		}
		return available;
	}
	
	synchronized public boolean checkIfFileDownloadComplete(){
		return bitField.checkIfFileDownloadComplete();
	}
	
	public BitFieldHandler returnBitFieldHandler(){
		return bitField;
	}
}
