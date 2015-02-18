package gui;

import java.util.LinkedList;

import transfer.TCPController;

public class MainGUI {

	public static void main(String[] args) {
		TCPController tcp = TCPController.getInstance();
		TrayIconControl tray = new TrayIconControl(tcp);
	}
	
}
