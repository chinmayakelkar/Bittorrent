package Messages;

import java.io.Serializable;

public class BitFieldHandler implements Serializable{

	private boolean bitfieldVector[];
	
	private int size;
	
	public BitFieldHandler(int numOfPieces) {
		bitfieldVector = new boolean[numOfPieces];
		size = numOfPieces;
		
		for(int i = 0; i < size; i++)
		{
			bitfieldVector[i] = false;
		}		
	}

	public int getSize()
	{
		return size;
	}
	
	public boolean[] getBitfieldVector() {
		return bitfieldVector;
	}

	
	public void setBitfieldVector(boolean[] bitfieldVector) {
		this.bitfieldVector = bitfieldVector;
	}
	
	public boolean getBitFieldOn(int number)
	{
		return bitfieldVector[number];
	}
	
	synchronized public void setBitFieldOn(int number, boolean value)
	{
		bitfieldVector[number] = value;
	}
	
	
	public void setAllBits(){
		for(int i=0 ; i<bitfieldVector.length ; i++){
			bitfieldVector[i] = true;
		}
	}
	
	public int getSetbitCount(){
		int counter = 0;
		for(int i = 0; i < this.bitfieldVector.length; i++){
			if(this.bitfieldVector[i]==true)
				counter++;
		}
		return counter;
	}
	
	public boolean checkIfFileDownloadComplete(){
		
		if(bitfieldVector==null || bitfieldVector.length==0){
			return false;
		}
		
		int i = 0;
		while(i < this.getSize())
		{
			if(bitfieldVector[i]!=true)
			{
				return false;
			}
			else
			{
				i++;
			}
		}
		return true;
	}
}
