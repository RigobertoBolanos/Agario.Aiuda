package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocketFactory;

import Audio.AudioCliente;
import Audio.IndividualAudioCliente;
import gui.ClientGUI;

public class Client {
	public final static int SERVER_PORT=8000;
	public final static int SERVER_PORT_LOBBY=8001;
	public final static int SERVER_PORT_GAME=8002;
	public final static int SERVER_PORT_VIEW=8003;
	public final static String IP_MULTICAST = "230.1.1.1";
	public static final String TRUSTTORE_LOCATION = "./Docs/keystore.jks";
	public final static String TYPE_PLAYER="Player";
	public final static String TYPE_VIEWER="Viewer";
	
	private DataInputStream in;
	private DataOutputStream out;
	private String serverIp;
	private Socket socketConnection;
	private Socket socketLobby;
	private Socket socketGame;
	private MulticastSocket socketView;
	private String nick;
	private ClientGUI gui;
	private ClientComunicationThread clientThread;
	private ClientViewThread viewThread;
	private char[] password = {'v','i','e','j','i','t', 'o'};
	private String type;
	
	private DatagramSocket socketMusica;
	private DatagramSocket socketFormat;
	private AudioCliente audioClient;
	private IndividualAudioCliente audioIndividual;
	
	public Client(String serverIp,String data,ClientGUI client,String type) throws AccountNotFoundException, WrongPasswordException, ExistingAccountException{
		try {
			this.serverIp=serverIp;
			gui=client;
			this.type=type;
			if(type.equals(TYPE_PLAYER)) 
			{
				System.setProperty("javax.net.ssl.trustStore", TRUSTTORE_LOCATION);
				SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
				
				KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyStore.load(new FileInputStream(TRUSTTORE_LOCATION), password);
				socketConnection = sf.createSocket(serverIp, SERVER_PORT);
				in=new DataInputStream(socketConnection.getInputStream());
				out=new DataOutputStream(socketConnection.getOutputStream());
				String [] userInfo=data.split(" ");
				nick=userInfo[0];
				out.writeUTF(data);
				String respond = in.readUTF();
				if(respond.equals("AccountNotFoundException"))
				{
					throw new AccountNotFoundException();
				}
				else if(respond.equals("WrongPasswordException"))
				{
					throw new WrongPasswordException();
				}
				else if(respond.equals("ExistingAccountException"))
				{
					throw new ExistingAccountException();
				}
//				audioClient = new AudioCliente();
//				audioClient.start();
				System.out.println(respond);
				audioIndividual = new IndividualAudioCliente(this, Integer.parseInt(respond));
			}
			else 
			{
				this.nick = data.split(" ")[0];
//				audioClient = new AudioCliente();
//				audioClient.start();
			}
		} catch (IOException | KeyStoreException |NoSuchAlgorithmException | CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void startLobbySocket() {
		try {
			socketLobby=new Socket(serverIp,SERVER_PORT_LOBBY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startGameComm() {
		try {
			socketGame=new Socket(serverIp,SERVER_PORT_GAME);
			clientThread=new ClientComunicationThread(this);
			clientThread.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void startGameView() {
		try {
			InetAddress mcIpAddress=InetAddress.getByName(IP_MULTICAST);
			socketView=new MulticastSocket(SERVER_PORT_VIEW);
			socketView.joinGroup(mcIpAddress);
			viewThread=new ClientViewThread(this);
			viewThread.start();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void upDateGame(boolean active,String[] players,String[] food) {
		gui.upDateGame(active,players,food);
	}
	
	public void gameNotStarted() {
		
	}

	public Socket getSocketLobby() {
		return socketLobby;
	}

	public void setSocketLobby(Socket socketLobby) {
		this.socketLobby = socketLobby;
	}

	public ClientGUI getGui() {
		return gui;
	}

	public void setGui(ClientGUI gui) {
		this.gui = gui;
	}

	public Socket getSocketGame() {
		return socketGame;
	}

	public void setSocketGame(Socket socketGame) {
		this.socketGame = socketGame;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DatagramSocket getSocketView() {
		return socketView;
	}

	public void setSocketView(MulticastSocket socketView) {
		this.socketView = socketView;
	}

	public DataInputStream getIn() {
		return in;
	}

	public void setIn(DataInputStream in) {
		this.in = in;
	}

	public DataOutputStream getOut() {
		return out;
	}

	public void setOut(DataOutputStream out) {
		this.out = out;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Socket getSocketConnection() {
		return socketConnection;
	}

	public void setSocketConnection(Socket socketConnection) {
		this.socketConnection = socketConnection;
	}

	public ClientComunicationThread getClientThread() {
		return clientThread;
	}

	public void setClientThread(ClientComunicationThread clientThread) {
		this.clientThread = clientThread;
	}
	
	public ClientViewThread getViewThread() {
		return viewThread;
	}

	public void setViewThread(ClientViewThread viewThread) {
		this.viewThread = viewThread;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public AudioCliente getAudioClient() {
		return audioClient;
	}

	public void setAudioClient(AudioCliente audioClient) {
		this.audioClient = audioClient;
	}

	public IndividualAudioCliente getAudioIndividual() {
		return audioIndividual;
	}

	public void setAudioIndividual(IndividualAudioCliente audioIndividual) {
		this.audioIndividual = audioIndividual;
	}

	public DatagramSocket getSocketMusica() {
		return socketMusica;
	}

	public void setSocketMusica(DatagramSocket socketMusica) {
		this.socketMusica = socketMusica;
	}

	public DatagramSocket getSocketFormat() {
		return socketFormat;
	}

	public void setSocketFormat(DatagramSocket socketFormat) {
		this.socketFormat = socketFormat;
	}
	
	

}
