package ui;

import java.util.LinkedList;

import transfer.TCPController;

public class consoleRunner {

	public static void main(String[] args) {
		TCPController controller = TCPController.getInstance();
		LinkedList<String> paths = new LinkedList<String>();
		paths.add("C:\\Users\\polle\\Documents\\desktopimages\\zelda.jpg");
		paths.add("C:\\Users\\polle\\Documents\\desktopimages\\zero.jpg");
		controller.sendFiles(paths);
	}

}
