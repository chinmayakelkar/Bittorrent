package peerToPeer;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logger.SimpleLogger;

public class OptimisticUnchoking implements Runnable{
	
	public ScheduledFuture<?> task = null;
	
	
    public ScheduledExecutorService task_scheduler = null;
	
    private static OptimisticUnchoking UnchokeHandler = null;
    
    private Starter threadController = null;
    
    private SimpleLogger logger = null;
    
  
    
    public static synchronized OptimisticUnchoking createInstance(Starter controller){
    	if(UnchokeHandler == null){
    		if(controller == null){
    			return null;
    		}
    		    		
    		UnchokeHandler = new OptimisticUnchoking();
    		boolean isInitialized = UnchokeHandler.initialize();
    		
    		if(isInitialized == false){
    			UnchokeHandler.task.cancel(true);
    			UnchokeHandler = null;
    			return null;
    		}
    		
    		UnchokeHandler.threadController = controller;
    		UnchokeHandler.logger = controller.getLogger();
    		
    	}	
    	
    	return UnchokeHandler;
    	
    }
    
    private boolean initialize(){
    	task_scheduler = Executors.newScheduledThreadPool(1);    	
    	return true;
    }
    
    
	public void run() {
		// TODO Auto-generated method stub		
		
		ArrayList<String> chokedPeerList = threadController.chokedPeerList;
		if(chokedPeerList.size() > 0){
			Random random = new Random();
			threadController.optimisticUnchokeOnePeer(chokedPeerList.get(random.nextInt(chokedPeerList.size())));
		}
		
		threadController.checkAllPeersFileDownloadComplete();
		if(threadController.msgHandler.checkIfFileDownloadComplete() == true){

			logger.info("Peer ["+threadController.getPeerID()+"] has downloaded the complete file.");
			threadController.broadcastShutdownMessage();

		};
		
	}
	

}
