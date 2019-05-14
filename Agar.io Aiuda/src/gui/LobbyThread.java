package gui;

import java.io.DataInputStream;
import java.io.IOException;
import connection.Client;

public class LobbyThread extends Thread{

	private Client client;
	
	public LobbyThread(Client client) {
		this.client=client;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			DataInputStream inLobby=new DataInputStream(client.getSocketLobby().getInputStream());
			String data;
			data = inLobby.readUTF();
			String[] splitData=data.split(" ");
			while(!splitData[2].equals("-")&&Integer.parseInt(splitData[0])<Integer.parseInt(splitData[1])) {
				//System.out.println(data);
				client.getGui().getLobby().getLblPlayers().setText("Jugadores conectados: "+(splitData.length-2));
				//System.out.println(client.getGui().getLobby().getLblPlayers().getText());
				client.getGui().getLobby().getLblTime().setText("Tiempo restante: "+(Integer.parseInt(splitData[1])-Integer.parseInt(splitData[0])));
				String actual="";
				for(int i=2;i<splitData.length;i++) {
					actual+=splitData[i]+"\n";
				}
				client.getGui().getLobby().getTxtPlayers().setText(actual);
				data=inLobby.readUTF();
				splitData=data.split(" ");
				client.getGui().getLobby().repaint();
			}
			System.out.println("Sale del lobby");
			if(client.getType().equals(Client.TYPE_PLAYER)) {
				
				client.startGameComm();
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
