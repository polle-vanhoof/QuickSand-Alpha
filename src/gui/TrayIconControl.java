package gui;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import transfer.TCPController;

public class TrayIconControl {
	
	TCPController tcp = null;
	
	public TrayIconControl(TCPController tcp){
		this.tcp = tcp;
		instantiateTray();
		LinkedList<String> paths = new LinkedList<String>();
		paths.add("C:\\Users\\polle\\Documents\\desktopimages\\zelda.jpg");
		paths.add("C:\\Users\\polle\\Documents\\desktopimages\\zero.jpg");
		tcp.sendFiles(paths);
	}
	
	public void instantiateTray(){
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}

		SystemTray tray = SystemTray.getSystemTray();

		PopupMenu menu = new PopupMenu();

		/*
		 * GET OWN IP POPUP
		 */
		MenuItem messageItem = new MenuItem("Get IP");
		messageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					URL whatismyip = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(
					                whatismyip.openStream()));

					String ip = in.readLine();
					in.close();
					JOptionPane.showMessageDialog(null, "your public ip: " +ip);
				} catch (HeadlessException | IOException e1) {
					try {
						JOptionPane.showMessageDialog(null, "Could not properly detect ip. It MIGHT be: " + InetAddress.getLocalHost().getHostAddress());
					} catch (HeadlessException | UnknownHostException e2) {
						JOptionPane.showMessageDialog(null, "Could not detect ip :(");
					}
				}
			}
		});
		menu.add(messageItem);

		/*
		 * TOGGLE RECEIVE ON/OFF
		 */
		MenuItem toggleReceiveItem = new MenuItem("Toggle Receive");
		toggleReceiveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tcp.isReceivingOn()){
					tcp.stopReceiving();
				}else{
					tcp.restartReceiving();
				}
			}
		});
		menu.add(toggleReceiveItem);
		
		/*
		 * EXIT PROGRAM
		 */
		MenuItem closeItem = new MenuItem("Exit");
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(closeItem);

		BufferedImage in = null;
		try {
			in = ImageIO.read(new File("trayIcon.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		TrayIcon icon = new TrayIcon(in, "SystemTray Demo", menu);
		icon.setImageAutoSize(true);

		try {
			tray.add(icon);
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
