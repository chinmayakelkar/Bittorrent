package com.test;

import peerToPeer.Starter;

public class Peer5 {
	public static void main(String args[]) throws Exception{
		Starter controller = Starter.setUpPeer("1005");
		controller.startThread();
	}
}
