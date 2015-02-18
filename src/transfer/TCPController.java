package transfer;

import java.util.LinkedList;

public class TCPController {

	private static TCPController instance = null;
	private TCPSender sender = null;
	private TCPReceiver receiver = null;
	private Thread receiverThread = null;

	protected TCPController() {
		sender = TCPSender.getInstance();
		receiver = TCPReceiver.getInstance();
		receiverThread = new Thread(receiver);
		receiverThread.start();
		System.out.println("TCP Receiver thread started!");
	}

	public static TCPController getInstance() {
		if(instance == null) {
			instance = new TCPController();
		}
		return instance;
	}
	
	public boolean isReceivingOn(){
		if(this.receiverThread == null){
			return false;
		}else{
			return true;
		}
	}
	
	public void stopReceiving(){
		this.receiver.kill();
		this.receiverThread = null;
	}
	
	public void restartReceiving(){
		if(receiverThread != null){
			this.stopReceiving();
		}
		receiver.continueWaiting();
		receiverThread = new Thread(receiver);
		receiverThread.start();
	}
	
	public void sendFiles(LinkedList<String> paths){
		sender.addFiles(paths);
		(new Thread(sender)).start();
	}
}
