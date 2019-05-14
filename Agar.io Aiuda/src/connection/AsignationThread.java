package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Audio.IndividualMusicRequestServer;
import model.Player;

public class AsignationThread extends Thread{

	private Server  server;
	private boolean inTime;
	
	public AsignationThread(Server server){
		this.server=server;
		inTime=true;
	} 
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(inTime){
			try {
				if(server.getPlayersSockets().size()==1) {
					server.starTimer();
				}
				Socket socket=server.getServerSocket().accept();
				if(inTime && server.getPlayersSockets().size()<server.MAX_PLAYERS) {
					
					System.out.println("Un cliente se ha conectado");
					DataInputStream in =new DataInputStream(socket.getInputStream());
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					String[] userData= in.readUTF().split(" ");
					if(userData.length == 3)
					{
						String registration = server.registerPlayer(userData[0], userData[1], userData[2]);
						if(registration.equals("ExistingAccountException"))
						{
							out.writeUTF("ExistingAccountException");
							socket.close();
						}
						else if(registration.equals("You're registered"))
						{
							server.getPlayersSockets().add(socket);
							System.out.println("Se conecto: "+userData[0]);
							server.getUserNames().add(userData[0]);
							Player player=new Player(userData[0]);
							System.out.println("aqui me llamo: "+userData[0]);
							server.getGame().getPlayers().add(player);
							ServerLobbyThread lobbyThread=new ServerLobbyThread(server);
							server.getLobbyThreads().add(lobbyThread);
							lobbyThread.start();
							ServerCommunicationThread serverThread= new ServerCommunicationThread(server);
							server.getServerThreads().add(serverThread);
							serverThread.start();
							
							IndividualMusicRequestServer musicRequestThread = new IndividualMusicRequestServer(server);
							server.getMusicRequestServers().add(musicRequestThread);
							musicRequestThread.start();
							
							out.writeUTF(musicRequestThread.getSocketChanges().getPort()+"");
						}
					}
					else if(userData.length == 2)
					{
						String[] dataBaseInfo = server.loginQuery(userData[0], userData[1]);
						if(dataBaseInfo[1] == null)
						{
							out.writeUTF("AccountNotFoundException");
							socket.close();
						}
						else if(dataBaseInfo[1] != null && dataBaseInfo[2] == null)
						{
							out.writeUTF("WrongPasswordException");
							socket.close();
						}
						else
						{
							server.getPlayersSockets().add(socket);
							System.out.println("Se conecto: "+dataBaseInfo[0]);
							server.getUserNames().add(dataBaseInfo[0]);
							Player player=new Player(dataBaseInfo[0]);
							server.getGame().getPlayers().add(player);
							ServerLobbyThread lobbyThread=new ServerLobbyThread(server);
							server.getLobbyThreads().add(lobbyThread);
							lobbyThread.start();
							ServerCommunicationThread serverThread= new ServerCommunicationThread(server);
							server.getServerThreads().add(serverThread);
							serverThread.start();
							
							IndividualMusicRequestServer musicRequestThread = new IndividualMusicRequestServer(server);
							server.getMusicRequestServers().add(musicRequestThread);
							musicRequestThread.start();
							
							System.out.println("getPort: "+musicRequestThread.getSocketChanges().getPort());
							System.out.println("getLocalPort"+musicRequestThread.getSocketChanges().getLocalPort());
							out.writeUTF(musicRequestThread.getSocketChanges().getLocalPort()+"");
						}
					}
					
				}
				else {
					System.out.println("Ya no hay tiempo");
				}
				}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Ya no se puede aceptar mas clientes");
		server.getGame().assignUsersBalls();
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public boolean isTimeOut() {
		return inTime;
	}

	public void setTimeOut(boolean timeOut) {
		this.inTime = timeOut;
	}

	public boolean isInTime() {
		return inTime;
	}

	public void setInTime(boolean inTime) {
		this.inTime = inTime;
	}
	
	
	
}
