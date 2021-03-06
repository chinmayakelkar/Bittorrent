package peerToPeer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import Messages.BitFieldHandler;
import PropertyReader.Constants;
import PropertyReader.ConfigTokens;

public class ChunkRequester implements Runnable {
	
	private static String LOGGER_PREFIX = ChunkRequester.class.getSimpleName();
	
	public BlockingQueue<MsgDetails> messageQueue;
	
	private boolean isShutDown = false;
	
	private Starter threadController;
	private PeerHandle peerHandler;
	
	private BitFieldHandler neighborPeerBitFieldhandler = null;
	int [] pieceIndexArray = new int[1000];	
	private ChunkRequester(){		
	}
	
	public static ChunkRequester createInstance(Starter controller, PeerHandle peerHandler){
		
		if(controller == null || peerHandler == null){
			return null;
		}
		
		ChunkRequester requestSender = new ChunkRequester();
		
		
		requestSender.messageQueue = new ArrayBlockingQueue<MsgDetails>(Constants.SENDER_QUEUE_SIZE);

		int pieceSize = Integer.parseInt(ConfigTokens.returnPropertyValue("PieceSize"));
		int numOfPieces = (int) Math.ceil(Integer.parseInt(ConfigTokens.returnPropertyValue("FileSize")) / (pieceSize*1.0)) ;
	
		requestSender.neighborPeerBitFieldhandler = new BitFieldHandler(numOfPieces);
		
		requestSender.threadController = controller;
		requestSender.peerHandler = peerHandler;
		
		return requestSender;
	}
	

	
	public int getRandomPieceNo(){
		BitFieldHandler thisPeerBitFieldhandler = threadController.getBitFieldMessage().returnBitFieldHandler(); 			
		int count = 0;
		for(int i=0 ; i<neighborPeerBitFieldhandler.getSize() && count<pieceIndexArray.length ; i++){
			if(thisPeerBitFieldhandler.getBitFieldOn(i) == false && neighborPeerBitFieldhandler.getBitFieldOn(i) == true){
				pieceIndexArray[count] = i;
				count++;
			}
		}
		
		if(count != 0){
			Random random = new Random();
			int index = random.nextInt(count);
			return pieceIndexArray[index];
		}else{
			return -1;
		}	
	} 
	
	public void run() {
		
		
		while(isShutDown == false){
			try {				
				MsgDetails message = messageQueue.take();
			System.out.println(LOGGER_PREFIX+": Received Message: "+Constants.getMessageName(message.returnMsgType()));
				
				MsgDetails requestMessage = MsgDetails.createInstance();
				requestMessage.setMessgageType(Constants.REQUEST_MESSAGE);
				
				MsgDetails interestedMessage = MsgDetails.createInstance();
				interestedMessage.setMessgageType(Constants.INTERESTED_MESSAGE);
				
				if(message.returnMsgType() == Constants.BITFIELD_MESSAGE){
					
					neighborPeerBitFieldhandler = message.returnBitFieldHandler();
					
					int missingPieceIndex = getRandomPieceNo();
					
					if(missingPieceIndex == -1){
						MsgDetails notInterestedMessage = MsgDetails.createInstance();
						notInterestedMessage.setMessgageType(Constants.NOT_INTERESTED_MESSAGE);
						peerHandler.sendNotInterestedMessage(notInterestedMessage);
					}else{
						interestedMessage.setPieceIndex(missingPieceIndex);
						peerHandler.sendInterestedMessage(interestedMessage);
						
						requestMessage.setPieceIndex(missingPieceIndex);
						peerHandler.sendRequestMessage(requestMessage);
					}									
				}
				
				if(message.returnMsgType() == Constants.HAVE_MESSAGE){
					int pieceIndex = message.getPieceIndex();
					
					try {
						neighborPeerBitFieldhandler.setBitFieldOn(pieceIndex, true);
					} catch (Exception e) {
						System.out.println(LOGGER_PREFIX+"["+peerHandler.peerID+"]: NULL POINTER EXCEPTION for piece Index"+pieceIndex +" ... "+neighborPeerBitFieldhandler);
						e.printStackTrace();
					}
					
					int missingPieceIndex = getRandomPieceNo();

					if(missingPieceIndex == -1){
						MsgDetails notInterestedMessage = MsgDetails.createInstance();
						notInterestedMessage.setMessgageType(Constants.NOT_INTERESTED_MESSAGE);
						peerHandler.sendNotInterestedMessage(notInterestedMessage);
					}else{
						if(peerHandler.isPieceMessageForPreviousMessageReceived() == true){
							peerHandler.setPieceMessageForPreviousMessageReceived(false);
							interestedMessage.setPieceIndex(missingPieceIndex);
							peerHandler.sendInterestedMessage(interestedMessage);
							
							requestMessage.setPieceIndex(missingPieceIndex);
							peerHandler.sendRequestMessage(requestMessage);
						}	
					}									
				}
				
				if(message.returnMsgType() == Constants.PIECE_MESSAGE){
					
					int missingPieceIndex = getRandomPieceNo();

					if(missingPieceIndex == -1){
						// do nothing 
					}else{
						if(peerHandler.isPieceMessageForPreviousMessageReceived() == true){
							peerHandler.setPieceMessageForPreviousMessageReceived(false);
							interestedMessage.setPieceIndex(missingPieceIndex);
							peerHandler.sendInterestedMessage(interestedMessage);
							
							requestMessage.setPieceIndex(missingPieceIndex);
							peerHandler.sendRequestMessage(requestMessage);
						}						
					}									
				}
				
				if(message.returnMsgType() == Constants.UNCHOKE_MESSAGE){
					
					int missingPieceIndex = getRandomPieceNo();

					peerHandler.setPieceMessageForPreviousMessageReceived(false);
					
					if(missingPieceIndex == -1){
						// do nothing 
					}else{
						interestedMessage.setPieceIndex(missingPieceIndex);
						peerHandler.sendInterestedMessage(interestedMessage);
						
						requestMessage.setPieceIndex(missingPieceIndex);
						peerHandler.sendRequestMessage(requestMessage);
					}									
				}
				
				
			} catch (Exception e) {				
				e.printStackTrace();
				break;
			}
		}
	}
	
	
	public boolean checkIfNeighbourDownloadFile(){
		if(neighborPeerBitFieldhandler != null && neighborPeerBitFieldhandler.checkIfFileDownloadComplete() == true){
			return true;
		}else{
			return false;
		}
	}
	
}
