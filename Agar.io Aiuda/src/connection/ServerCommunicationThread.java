package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerCommunicationThread extends Thread{

	private Server server;
	
	public ServerCommunicationThread(Server server) {
		this.server=server;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			System.out.println("Inicia hilo servidorComm");
			Socket gameSocket=server.getServerSocketGame().accept();
			System.out.println("Hay conexion");
			DataInputStream in =new DataInputStream(gameSocket.getInputStream());
			DataOutputStream out=new DataOutputStream(gameSocket.getOutputStream());
			while(true) {
				String data=server.getInfoGame();
				out.writeUTF(data);
				//System.out.println("Envia info al client");
				String dataPlayer=in.readUTF();
				server.upDatePlayer(dataPlayer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
