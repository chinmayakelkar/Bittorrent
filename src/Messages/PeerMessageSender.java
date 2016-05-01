package Messages;

import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import peerToPeer.MsgDetails;
import peerToPeer.PeerHandle;
import PropertyReader.Constants;

public class PeerMessageSender implements Runnable {
	
	private static final String LOGGER_PREFIX = PeerMessageSender.class.getSimpleName();
	
	private BlockingQueue<Message> messageQueue;
	
	private ObjectOutputStream outputStream = null;
	
	private boolean isShutDown = false;
	
	private PeerMessageSender(){
		
	}
	
	public static PeerMessageSender createInstance(ObjectOutputStream outputStream, PeerHandle handler){
		
		PeerMessageSender messageSender = new PeerMessageSender();
		boolean isInitialized = messageSender.intialize();		
		if(isInitialized == false){
			messageSender.deinitialize();
			messageSender = null;
			return null;
		}
		
		messageSender.outputStream = outputStream;
		return messageSender;
	}
	
	public void deinitialize(){
		if(messageQueue !=null && messageQueue.size()!=0){
			messageQueue.clear();
		}
		messageQueue = null;
	}
	
	private boolean intialize(){
		messageQueue = new ArrayBlockingQueue<Message>(Constants.SENDER_QUEUE_SIZE);
		return true;
	}
	
	public void printMessageDetails(Message message){
		if(message.returnMsgType() != Constants.HAVE_MESSAGE && message.returnMsgType() != Constants.NOT_INTERESTED_MESSAGE && message.returnMsgType() != Constants.INTERESTED_MESSAGE){
			if(message.returnMsgType() == Constants.PIECE_MESSAGE || message.returnMsgType() == Constants.REQUEST_MESSAGE){

			}else{

			}			
		}		
	}
	
	public void run() {
		
		if(messageQueue == null){
			throw new IllegalStateException(LOGGER_PREFIX+": This object is not initialized properly. This might be result of calling deinit() method");
		}
		
		while(isShutDown == false){
			try {				
				Message message = messageQueue.take();
				
				outputStream.writeUnshared(message);
				outputStream.flush();
				printMessageDetails(message);				
				
				message = null;
			} catch (Exception e) {				
					//e.printStackTrace();	
				System.out.println("SYSTEM EXITED..!!");
				
				break;
			}
		}
	}
	
	public void sendMessage(Message message) throws InterruptedException{
		if(messageQueue == null){
			throw new IllegalStateException("");
		}else{
			messageQueue.put(message);
		}
	}
	
	public void shutdown(){
		isShutDown = true;
	}
}
