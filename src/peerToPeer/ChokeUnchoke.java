package peerToPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import logger.SimpleLogger;
import PropertyReader.ConfigTokens;

public class ChokeUnchoke implements Runnable {

	private static ChokeUnchoke Handler = null;

	public ScheduledFuture<?> task = null;

	private ScheduledExecutorService taskscheduler = null;

	private Starter threadController = null;
	
	private SimpleLogger logger = null;

	public static synchronized ChokeUnchoke createInstance(Starter controller) {
		if (Handler == null) {
			if (controller == null) {
				return null;
			}

			Handler = new ChokeUnchoke();
			Handler.taskscheduler = Executors.newScheduledThreadPool(1);	
		Handler.logger = controller.getLogger();
			Handler.threadController = controller;

		}

		return Handler;

	}


	public void run() {

		// TODO Auto-generated method stub
		Integer preferredNeighbors = 0;
		HashMap<String, Double> speedMap = threadController.returnDownloadSpeedForPeers();
		
		if (ConfigTokens.returnPropertyValue("NumberOfPreferredNeighbors") != null)
			preferredNeighbors = Integer.parseInt(ConfigTokens.returnPropertyValue("NumberOfPreferredNeighbors"));
		else
			{
			
			}

		if (preferredNeighbors > speedMap.size()) {

		} else {
			ArrayList<String> unchokePeers = new ArrayList<String>();
			
			Set<Entry<String, Double>> entrySet = speedMap.entrySet();

			Entry<String, Double>[] tempArr = new Entry[speedMap.size()];
			tempArr = entrySet.toArray(tempArr);

			for (int i = 0; i < tempArr.length; i++) {
				for (int j = i + 1; j < tempArr.length; j++) {
					if (tempArr[i].getValue().compareTo(tempArr[j].getValue()) == -1) {
						Entry<String, Double> tempEntry = tempArr[i];
						tempArr[i] = tempArr[j];
						tempArr[j] = tempEntry;
					}
				}
			}

		
			LinkedHashMap<String, Double> sortedSpeedMap = new LinkedHashMap<String, Double>();

			for (int i = 0; i < tempArr.length; i++) {
				sortedSpeedMap.put(tempArr[i].getKey(), tempArr[i].getValue());
				
			}

			int count = 0;

			for (Entry<String, Double> entry : sortedSpeedMap.entrySet()) {
				String key = entry.getKey();
				unchokePeers.add(key);
				count++; 
				if (count == preferredNeighbors)
					break;
			}

			ArrayList<String> chokedPeerList = new ArrayList<String>();

			for (String peerID : unchokePeers) {
				sortedSpeedMap.remove(peerID);
			}
			chokedPeerList.addAll(sortedSpeedMap.keySet());

			for (String peerID : chokedPeerList) {
				
			}

			String logMessage = "Peer ["+threadController.getPeerID()+"] has the neighbors ["; 
			
			for (String peerID : unchokePeers) {
			
				logMessage += peerID + " , ";
			}
			
			logMessage +="]";
			
			logger.info(logMessage);
			
			threadController.unchokeThePeers(unchokePeers);
			threadController.chokeThePeers(chokedPeerList);
		}
	}

	public void start(int startDelay, int intervalDelay) {
		task = taskscheduler.scheduleAtFixedRate(this, startDelay, intervalDelay, TimeUnit.SECONDS);
	}

}
