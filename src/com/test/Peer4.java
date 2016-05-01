package com.test;

import peerToPeer.Starter;

public class Peer4 {
	public static void main(String args[]) throws Exception{
		Starter controller = Starter.setUpPeer("1004");
		controller.startThread();
	}
}
