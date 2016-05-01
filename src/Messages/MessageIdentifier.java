package Messages;

import java.nio.ByteBuffer;

import peerToPeer.MsgDetails;
import PropertyReader.Constants;

public class MessageIdentifier {

	private static MessageIdentifier manager;
	
	private MessageIdentifier(){
		
	}
	
	public static MessageIdentifier createIdentfier(){
		if(manager == null){
			manager = new MessageIdentifier();
			boolean isInitialized = true;
			
			if(isInitialized == false){
				manager.close();
				manager = null;
			}
		}
		return manager;
	} 
	
	public void close(){
		
	}
	
	public byte[] geHandshakeMessage(byte[] rawData){
		String headerString =  "CNT5106CSPRING2016";
		char array[] = headerString.toCharArray();
		byte[] rawHeaderMessage = new byte[32];
		for(int i = 0; i< 18; i++){
			rawHeaderMessage[i] = (byte)array[i];	
		}
		
		for(int i = 18; i<31;i++){
			rawHeaderMessage[i] = (byte)0;
			
		}	
		rawHeaderMessage[31] = rawData[3];
		
		return rawHeaderMessage;
	}
	
	public byte[] getRequestMessage(int pieceIndex){
		return null;
	}
	
	//getChokeMessage
	public byte[] getChokeMessage(){		
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		byteBuffer.putInt(Constants.SIZE_OF_EMPTY_MESSAGE);
		byteBuffer.put(Constants.CHOKE_MESSAGE);
		byte[] rawChokeMessage = byteBuffer.array();
		return rawChokeMessage;
		
	}
	// getUnchokeMessage
	public byte[] getUnchokeMessage(){
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		byteBuffer.putInt(Constants.SIZE_OF_EMPTY_MESSAGE);
		byteBuffer.put(Constants.UNCHOKE_MESSAGE);
		byte[] rawUnChokeMessage = byteBuffer.array();
		return rawUnChokeMessage;		
	}
	
	// getInterstedMessage
	public byte[] getInterestedMessage(){
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		byteBuffer.putInt(Constants.SIZE_OF_EMPTY_MESSAGE);
		byteBuffer.put(Constants.INTERESTED_MESSAGE);
		byte[] rawInterestedMessage = byteBuffer.array();
		return rawInterestedMessage;
	}
	
	// getNotInterstedMessage
	public byte[] getNotInterestedMessage(){
		ByteBuffer byteBuffer = ByteBuffer.allocate(5);
		byteBuffer.putInt(Constants.SIZE_OF_EMPTY_MESSAGE);
		byteBuffer.put(Constants.NOT_INTERESTED_MESSAGE);
		byte[] rawNotInterestedMessage = byteBuffer.array();
		return rawNotInterestedMessage;

	}
	//getHaveMessage
	
	public byte[] getHaveMessage(byte[] payLoad){
		ByteBuffer byteBuffer = ByteBuffer.allocate(9);
		byteBuffer.putInt(5);
		byteBuffer.put(Constants.HAVE_MESSAGE);
		byteBuffer.put(payLoad);
		byte[] rawHaveMessage = byteBuffer.array();
		return rawHaveMessage;
		
	}	
	//getbitFieldMessage
	
	public byte[] getBitFieldMessage(byte[] payload){
		int payloadSize = payload.length;
		ByteBuffer byteBuffer = ByteBuffer.allocate(payloadSize+5);
		byteBuffer.putInt(payloadSize+1);
		byteBuffer.put(Constants.BITFIELD_MESSAGE);
		byteBuffer.put(payload);
		byte[] rawBitFieldMessage = byteBuffer.array();
		return rawBitFieldMessage;
	}

	//getRequestMessage
	
	public byte[] getRequestMessage(byte[] payLoad){
		ByteBuffer byteBuffer = ByteBuffer.allocate(9);
		byteBuffer.putInt(5);
		byteBuffer.put(Constants.REQUEST_MESSAGE);
		byteBuffer.put(payLoad);
		byte[] rawRequestMessage = byteBuffer.array();
		return rawRequestMessage;
	}
	//
	
	public HandshakeMessage parseHandShakeMessage(byte[] rawData){
	
		return null;
	}
	
	public MsgDetails parsePeer2PeerMessage(byte[] rawData){
	
		return null;
	}
	
	public Message parseMessage(byte[] rawData){
		if( rawData== null || rawData.length < 5){
			return null;
		}
		
		byte messageType = rawData[4];
		
		if(messageType == Constants.CHOKE_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessgageType(Constants.CHOKE_MESSAGE);
			message.setMessageLength(1);
			message.setData(null);
			return message;			
		}
		
		if(messageType == Constants.UNCHOKE_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessgageType(Constants.UNCHOKE_MESSAGE);
			message.setMessageLength(1);
			message.setData(null);
			return message;			
		}
		
		if(messageType == Constants.INTERESTED_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessgageType(Constants.INTERESTED_MESSAGE);
			message.setMessageLength(1);
			message.setData(null);
			return message;			
		}
		
		if(messageType == Constants.NOT_INTERESTED_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessgageType(Constants.NOT_INTERESTED_MESSAGE);
			message.setMessageLength(1);
			message.setData(null);
			return message;			
		}
		
		if(messageType == Constants.HAVE_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessageLength(5);
			message.setMessageLength(Constants.HAVE_MESSAGE);
			message.setPieceIndex((int)rawData[8]);
		}
		
		if(messageType == Constants.REQUEST_MESSAGE){

			MsgDetails message = MsgDetails.createInstance();
			message.setMessageLength(5);
			message.setMessageLength(Constants.REQUEST_MESSAGE);
			message.setPieceIndex((int)rawData[8]);
		}
		return null;
	}
}
