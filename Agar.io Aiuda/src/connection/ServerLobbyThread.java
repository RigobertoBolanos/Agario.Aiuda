package connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerLobbyThread extends Thread{
	
	private Server server;
	
	public ServerLobbyThread(Server server) {
		this.server=server;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Socket socketLobby=server.getServerSocketLobby().accept();
			DataOutputStream outLobby = new DataOutputStream(socketLobby.getOutputStream());
			TimerThread time=server.getTimerThread();
			while(time.getTime()<time.getWait()) {
				String nicks="";
				ArrayList<String> users=server.getUserNames();
				for(int i=0;i<users.size();i++) {
					String actual=users.get(i);
					if(i==users.size()-1) {
						nicks+=actual;
					}
					else {
						nicks+=actual+" ";
					}
				}
				outLobby.writeUTF(time.getTime()+" "+time.getWait()+" "+nicks);
			}
			outLobby.writeUTF(time.getTime()+" "+time.getWait()+" "+"-");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
