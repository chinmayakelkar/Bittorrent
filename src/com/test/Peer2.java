package com.test;


import peerToPeer.Starter;

public class Peer2 {
	public static void main(String args[]) throws Exception{
		Starter controller = Starter.setUpPeer("1002");
		controller.startThread();
	}
}
