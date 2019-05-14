package connection;

import java.io.IOException;
import java.net.DatagramPacket;

public class ClientViewThread extends Thread{
	
	private Client client;
	
	public ClientViewThread(Client client) {
		this.client=client;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		byte[] buffer=new byte[65507];
		while(true) {
			System.out.println("Waiting boii");
			try {
				DatagramPacket data = new DatagramPacket(buffer,0,buffer.length );
				client.getSocketView().receive(data);
				String dataReceived=new String(data.getData());
				System.out.println(dataReceived);
				//System.out.println(dataReceived.contains("No"));
				if(!dataReceived.contains("No")) {
					String [] splitData=dataReceived.split("&");
					String[] dataFood;
					if(splitData.length>1){
						
						dataFood=splitData[2].split(" ");
					}
					else{
						//System.out.println("Vacio papu");
						dataFood=new String[0];
					}
					boolean status=Boolean.parseBoolean(splitData[0]);
					String[] dataPlayers=splitData[1].split(" ");
					
					client.upDateGame(status,dataPlayers,dataFood);
				}
				else {
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
