package connection;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocketFactory;

import Audio.AudioServidor;
import Audio.IndividualAudioCliente;
import Audio.IndividualAudioServer;
import Audio.IndividualMusicRequestServer;
import Chat.ServidorChat;
import gui.GameThread;
import model.Ball;
import model.Game;
import model.Player;

public class Server {
	
	public final static int MIN_PLAYERS = 2;
	public final static int MAX_PLAYERS = 5;	
	public final static int SERVER_PORT=8000;
	public final static int SERVER_PORT_LOBBY=8001;
	public final static int SERVER_PORT_GAME=8002;
	public final static int SERVER_PORT_VIEW=8003;
	public final static String IP_MULTICAST = "230.1.1.1";
	public static final String KEYSTORE_LOCATION = "./Docs/keystore.jks";
	public static final String KEYSTORE_PASSWORD = "viejito";
	public static final String LOG_PATH = "./Docs/Logs.txt";
	public static final String CANCION_PREDET = "RISE";
	
	private ServerSocket serverSocket;
	private ServerSocket serverSocketLobby;
	private ServerSocket serverSocketGame;
	private DatagramSocket serverSocketView;
	private ArrayList<Socket> playersSockets;
	//private ArrayList<DatagramSocket> viewersSockets;
	private AsignationThread asignationThread;

	private ArrayList<ServerLobbyThread> lobbyThreads;
	private TimerThread timerThread;
	private Game game;
	private ArrayList<String> userNames;
	private ArrayList<ServerCommunicationThread> serverThreads;
	private ServerViewThread viewThread;
	private GameThread gameThread;
	private boolean startView;
	
	private AudioServidor audioServer;
	private ArrayList<IndividualMusicRequestServer> musicRequestServers;
	private ServidorChat servidorChat;
	
	private boolean webService ;
	private ServerSocket serverSocketWebService;
	private static final int PORT_WEB_SERVICE = 7000;
	private HiloDespliegueAppWeb hiloDespliegueAppWeb;
	
	
	public Server(int wait){
		initGameServer(wait);
	}
	
	public void initGameServer(int wait){
//		try {
//			System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
//			System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
//			game=new Game();
//			//game.generateFood();
//			userNames=new ArrayList<String>();
//			playersSockets=new ArrayList<>();
//			lobbyThreads=new ArrayList<ServerLobbyThread>();
//			serverThreads=new ArrayList<ServerCommunicationThread>();
//			serverSocketView=new DatagramSocket();
//			viewThread=new ServerViewThread(this);
//			viewThread.start();
//			
//			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
//			serverSocket = ssf.createServerSocket(SERVER_PORT);
//			System.out.println(serverSocket.getInetAddress().getLocalHost());
//			
//			serverSocketLobby=new ServerSocket(SERVER_PORT_LOBBY);
//			serverSocketGame=new ServerSocket(SERVER_PORT_GAME);
//			
////			audioServer = new AudioServidor(CANCION_PREDET);
////			audioServer.start();
//			musicRequestServers = new ArrayList<>();
//			
//			asignationThread = new AsignationThread(this);
//			asignationThread.start();
//			timerThread=new TimerThread(asignationThread, wait);
//			gameThread=new GameThread(this, 30);
//			
//			servidorChat = new ServidorChat();
			
			webService = true;
			try {
				serverSocketWebService = new ServerSocket(PORT_WEB_SERVICE);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		
			hiloDespliegueAppWeb = new HiloDespliegueAppWeb(this);
			hiloDespliegueAppWeb.start();
			
//		} 
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public String getInfoGame() {
		String data="";
		if(startView) {
			
			boolean gameStatus=game.isActive();
			data+=gameStatus+"&";
			for(int i=0;i<game.getPlayers().size();i++) {
				Player actual=game.getPlayers().get(i);
				String nick=actual.getNickName();
				Ball ball=actual.getBall();
				double posX=ball.getPosX();
				double posY=ball.getPosY();
				double radius=ball.getRadius();
				Color color=ball.getColor();
				boolean active=ball.isActive();
				if(i==game.getPlayers().size()-1) {
					data+=nick+","+posX+","+posY+","+color.getRGB()+","+radius+","+active;
					
				}
				else {
					data+=nick+","+posX+","+posY+","+color.getRGB()+","+radius+","+active+" ";
				}
			}
			
			data+="&";
			if(game.getArrFood().size()==0){
				//System.out.println("Vacio");
			}
			for(int i=0;i<game.getArrFood().size();i++){
				Ball actual=game.getArrFood().get(i);
				double posX=actual.getPosX();
				double posY=actual.getPosY();
				double radius=actual.getRadius();
				int color=actual.getColor().getRGB();
				if(i==game.getArrFood().size()-1){
					data+=posX+","+posY+","+radius+","+color;
				}
				else{
					data+=posX+","+posY+","+radius+","+color+" ";
				}
			}
		}
		else {
			data="No";
		}
		
		return data;
	}
	
	public void upDatePlayer(String data) {
		String [] dataSplit=data.split(",");
		String nick=dataSplit[0];
		double posX=Double.parseDouble(dataSplit[1]);
		double posY=Double.parseDouble(dataSplit[2]);
		double radius=Double.parseDouble(dataSplit[3]);
		boolean active=Boolean.parseBoolean(dataSplit[4]);
		Player actual=game.getPlayer(nick);
		Ball ball=actual.getBall();
		ball.setPosX(posX);
		ball.setPosY(posY);
		ball.setRadius(radius);
		ball.setActive(active);
		ArrayList<Ball> food=game.getArrFood();
		ArrayList<Player> enemies=game.getPlayers();
		if(food.size()==0){
			//System.out.println("Vacio");
		}
		for(int i=0;i<food.size();i++){
			Ball actualBall=food.get(i);
			if(ball.eat(actualBall)){
				food.remove(actualBall);
			}
		}
		
		for(int i=0;i<enemies.size();i++) {
			Player actualEn=enemies.get(i);
			Ball enemie=actualEn.getBall();
			if(!actual.equals(actualEn)) {
				if(enemie.isActive()) {
					
					if(ball.eat(enemie)) {
						enemie.setActive(false);
					}
				}
			}
		}
		
	}
	
	public String registerPlayer(String nickname, String email, String password) throws IOException
	{
		String[] datosUser = loginQuery(email, password);
		String result = "ExistingAccountException";
		if(datosUser[1] == null)
		{
			File logs = new File(LOG_PATH);
			PrintWriter pw = new PrintWriter(new FileWriter(logs, true));
			pw.write(nickname + "," + email + "," + password + "\n");
			pw.flush();
			pw.close();
			result = "You're registered";
		}
		return result;
	}
	
	public String[] loginQuery(String email, String password) throws IOException
	{
		String[] result = new String[3];
		BufferedReader br = new BufferedReader(new FileReader(new File(LOG_PATH)));
		String line;
		boolean found = false;
		while((line = br.readLine()) != null && !found)
		{
			String[] userData = line.split(",");
			if(email.equals(userData[1]))
			{
				result[1] = userData[1];
			}
			if(password.equals(userData[2]))
			{
				result[2] = userData[2];
			}
			if(result[1] != null && result[2] != null)
			{
				result[0] = userData[0];
			}
			if(result[1] != null)
			{
				found = true;
			}
		}
		br.close();
		return result;
	}
	
	public void starTimer() {
		timerThread.start();
	}
	

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public ArrayList<Socket> getPlayersSockets() {
		return playersSockets;
	}

	public void setPlayersSockets(ArrayList<Socket> playersSockets) {
		this.playersSockets = playersSockets;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public TimerThread getTimerThread() {
		return timerThread;
	}

	public void setTimerThread(TimerThread timerThread) {
		this.timerThread = timerThread;
	}

	public ArrayList<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(ArrayList<String> userNames) {
		this.userNames = userNames;
	}

	public ServerSocket getServerSocketLobby() {
		return serverSocketLobby;
	}

	public void setServerSocketLobby(ServerSocket serverSocketLobby) {
		this.serverSocketLobby = serverSocketLobby;
	}

	public ArrayList<ServerLobbyThread> getLobbyThreads() {
		return lobbyThreads;
	}

	public void setLobbyThreads(ArrayList<ServerLobbyThread> lobbyThreads) {
		this.lobbyThreads = lobbyThreads;
	}

	public ServerSocket getServerSocketGame() {
		return serverSocketGame;
	}

	public void setServerSocketGame(ServerSocket serverSocketGame) {
		this.serverSocketGame = serverSocketGame;
	}

	public ArrayList<ServerCommunicationThread> getServerThreads() {
		return serverThreads;
	}

	public void setServerThreads(ArrayList<ServerCommunicationThread> serverThreads) {
		this.serverThreads = serverThreads;
	}

	public GameThread getGameThread() {
		return gameThread;
	}

	public void setGameThread(GameThread gameThread) {
		this.gameThread = gameThread;
	}

	public DatagramSocket getServerSocketView() {
		return serverSocketView;
	}

	public void setServerSocketView(DatagramSocket serverSocketView) {
		this.serverSocketView = serverSocketView;
	}

	/*
	public ArrayList<DatagramSocket> getViewersSockets() {
		return viewersSockets;
	}

	public void setViewersSockets(ArrayList<DatagramSocket> viewersSockets) {
		this.viewersSockets = viewersSockets;
	}
*/
	public ServerViewThread getViewThreads() {
		return viewThread;
	}

	public void setViewThreads(ServerViewThread viewThreads) {
		this.viewThread = viewThreads;
	}

	public boolean isStartView() {
		return startView;
	}

	public void setStartView(boolean startView) {
		this.startView = startView;
	}


	public AsignationThread getAsignationThread() {
		return asignationThread;
	}

	public void setAsignationThread(AsignationThread asignationThread) {
		this.asignationThread = asignationThread;
	}

	public ServerViewThread getViewThread() {
		return viewThread;
	}

	public void setViewThread(ServerViewThread viewThread) {
		this.viewThread = viewThread;
	}

	public AudioServidor getAudioServer() {
		return audioServer;
	}

	public void setAudioServer(AudioServidor audioServer) {
		this.audioServer = audioServer;
	}

	public ServidorChat getServidorChat() {
		return servidorChat;
	}

	public void setServidorChat(ServidorChat servidorChat) {
		this.servidorChat = servidorChat;
	}

	public ArrayList<IndividualMusicRequestServer> getMusicRequestServers() {
		return musicRequestServers;
	}

	public void setMusicRequestServers(ArrayList<IndividualMusicRequestServer> musicRequestServers) {
		this.musicRequestServers = musicRequestServers;
	}

	public boolean isWebService() {
		return webService;
	}

	public void setWebService(boolean webService) {
		this.webService = webService;
	}

	public ServerSocket getServerSocketWebService() {
		return serverSocketWebService;
	}

	public void setServerSocketWebService(ServerSocket serverSocketWebService) {
		this.serverSocketWebService = serverSocketWebService;
	}

	public HiloDespliegueAppWeb getHiloDespliegueAppWeb() {
		return hiloDespliegueAppWeb;
	}

	public void setHiloDespliegueAppWeb(HiloDespliegueAppWeb hiloDespliegueAppWeb) {
		this.hiloDespliegueAppWeb = hiloDespliegueAppWeb;
	}
	
	
	

}
