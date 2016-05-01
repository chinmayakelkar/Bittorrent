package com.test;


import peerToPeer.Starter;

public class Peer3 {
	public static void main(String args[]) throws Exception{
		Starter controller = Starter.setUpPeer("1003");
		controller.startThread();
	}
}
