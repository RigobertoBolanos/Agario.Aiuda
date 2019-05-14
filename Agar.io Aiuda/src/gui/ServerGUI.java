package gui;

import javax.swing.JFrame;

import connection.Server;


public class ServerGUI extends JFrame{

	private static Server server;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		server = new Server(2);
		//192.168.43.180
	}

}
