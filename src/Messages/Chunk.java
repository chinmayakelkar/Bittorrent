package Messages;

import java.io.Serializable;

public class Chunk implements Serializable{
	private byte[] data;
	int size;
	
	public Chunk(int size)
	{
		this.size = size;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public int getSize(){
		if(data == null){
			return -1;
		}else{
			return data.length;
		}		
	}
}
