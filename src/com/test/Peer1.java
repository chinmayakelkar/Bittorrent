package com.test;


import peerToPeer.Starter;

public class Peer1 {
	public static void main(String args[]) throws Exception{
		Starter controller = Starter.setUpPeer("1001");
		controller.startThread();
	}
}
