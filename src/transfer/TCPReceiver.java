package transfer;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPReceiver implements Runnable {

	private static TCPReceiver instance = null;
	ServerSocket welcomeSocket = null;

	protected TCPReceiver() {
		try {
			this.welcomeSocket = new ServerSocket(6789);
		} catch (IOException e) {
			System.err.println("failed to create server socket");
		}
	}

	public static synchronized TCPReceiver getInstance() {
		if(instance == null) {
			instance = new TCPReceiver();
		}
		return instance;
	}

	@Override
	public void run() {
		while(true){
			try{
				System.out.println("waiting for client connection");
				Socket socket = welcomeSocket.accept();
				DataInputStream in = new DataInputStream(socket.getInputStream());
				int size = Integer.parseInt(in.readLine().split(": ")[1]);
				String fileName = in.readLine().split(": ")[1].trim();
				byte[] item = new byte[size];
				for(int i = 0; i < size; i++){
					item[i] = in.readByte();
				}
				FileOutputStream fos = new FileOutputStream(new File(fileName));
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				bos.write(item);
				bos.close();
				fos.close();
			}catch(IOException e){
				System.err.println("file receive failed!");
				break;
			}
		}
	}

	public void kill() {
		try {
			welcomeSocket.close();
		} catch (IOException e) {
			// do nothing?
		}
	}

	public void continueWaiting() {
		try {
			this.welcomeSocket = new ServerSocket(6789);
		} catch (IOException e) {
			System.err.println("failed to create server socket");
		}
	}
}
