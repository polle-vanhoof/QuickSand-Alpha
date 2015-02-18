package transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TCPSender implements Runnable{

	private static TCPSender instance = null;
	private ConcurrentLinkedQueue<String> toSend = new ConcurrentLinkedQueue<String>();

	protected TCPSender() {
		// Exists only to defeat instantiation.
	}

	public static synchronized TCPSender getInstance() {
		if(instance == null) {
			instance = new TCPSender();
		}
		return instance;
	}

	public void addFiles(LinkedList<String> paths){
		toSend.addAll(paths);
	}

	@Override
	public void run() {
		System.out.println("ready to send files");
		while(!toSend.isEmpty()){
			String filePath = toSend.poll();
			try{
				Socket socket = new Socket("localhost", 6789);
				PrintStream out = new PrintStream(socket.getOutputStream(), true);
				FileInputStream requestedfile = new FileInputStream(filePath);
				byte[] buffer = new byte[1];
				File file = new File(filePath);
				long fileLength = file.length();
				String fileName = file.getName();
				System.out.println("sending file: " + fileName + " (size: " + fileLength + ")");
				out.println("Content-Length: "+fileLength); // for the client to receive file
				out.println("Name: " +fileName);
				long bytesSent = 0;
				while((requestedfile.read(buffer)!=-1)){
				    out.write(buffer);
				    out.flush();
				    bytesSent++;
				    if(bytesSent % 30000 == 0){
				    	double percentage = Math.round(100.0*bytesSent/fileLength);
				    	System.out.println("transferred: " + percentage + "%");
				    }
				}
				out.close(); 
				socket.close();
				requestedfile.close();
			}catch(IOException e){
				System.err.println("file send failed!");
			}
			
		}
	}

}
