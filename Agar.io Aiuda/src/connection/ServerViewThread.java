package connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerViewThread extends Thread{
	
	private Server server;
	
	public ServerViewThread(Server server) {
		
		this.server=server;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(true) {
			try {
				InetAddress mcIPAddress=InetAddress.getByName(Server.IP_MULTICAST);
				String data=server.getInfoGame();
				if(!data.equals("No")) {
					//System.out.println(data);
					DatagramPacket dataSend=new DatagramPacket(data.getBytes(), data.length(),InetAddress.getLocalHost(),Client.SERVER_PORT_VIEW);
					dataSend.setAddress(mcIPAddress);
					dataSend.setPort(Server.SERVER_PORT_VIEW);
					server.getServerSocketView().send(dataSend);
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
