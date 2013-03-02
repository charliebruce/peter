package ember.server.net;

import ember.util.Logger;

public class PacketQueue {
	
	/**
	 * LIFO queue of packets for processing
	 */
	Packet[] queue;
	int at;
	int max;
	PacketQueue(int length){
		max = length;
		queue = new Packet[length];
		at=0;
	}
	public void add(Packet p){
		if (at == max-1){
			Logger.getInstance().warning("PacketQueue maximum length exceeded.");
		}
		queue[at] = p;
		at++;
	}
	public Packet get(){
		//TODO error handling
		at--;
		return queue[at];
	}
	
	public boolean isEmpty(){
		if (at == 0)
			return true;
		return false;
		
	}
	public void disposeOf(){
		if (!this.isEmpty()){
			Logger.getInstance().warning("Packet Queue disposed of but not empty. Data loss?");
		}
	}
}
