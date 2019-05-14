package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import Audio.AudioCliente;
import Chat.ClienteChat;
import connection.AccountNotFoundException;
import connection.Client;
import connection.ExistingAccountException;
import connection.WrongPasswordException;
import model.Ball;
import model.Game;
import model.Player;

//import Conection.Client;
//import Conection.Server;
//import Model.Game;
//import Model.Player;

public class ClientGUI extends JFrame implements ActionListener{
	public final static int ANCHO=1421;
	public final static int LARGO=1000;
	public final static String LOGIN="Log In";
	public final static String REGISTER="Register";
	public final static String SIGN_IN ="Sign In";

	private Client client;
	private LobbyThread lobbyThread;
	private Draw draw;
	private JPanel aux;
	private JPanel first;
	private JButton btnLogIn;
	private JButton btnRegister;
	private LogInPane logIn;
	private RegisterPane register;
	private LobbyPane lobby;
	private Game game; 
	//private Client client;
	//TODO
	private ClienteChat chatPane;
	private MusicPane panelMusica;

	public ClientGUI() {
		setTitle("Agar.io");
		setLayout(new BorderLayout());
		setResizable(true);
		//draw=new Draw(this);
		//add(draw);
		first=new JPanel();
		first.setLayout(new BorderLayout());
		aux=new JPanel();
		btnLogIn=new JButton(LOGIN);
		btnRegister=new JButton(REGISTER);
		aux.add(btnLogIn);
		aux.add(btnRegister);
		logIn=new LogInPane(this);
		btnRegister.addActionListener(this);
		btnRegister.setActionCommand(REGISTER);
		btnLogIn.addActionListener(this);
		btnLogIn.setActionCommand(LOGIN);
		first.add(aux,BorderLayout.SOUTH);
		first.add(logIn,BorderLayout.CENTER);
		add(first);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		pack();
//		setSize(400, 200);
	}

	
	public void upDate() {
		draw.repaint();
		draw.revalidate();
	}
	
	public static void main(String[] args) {
		ClientGUI clientGUI = new ClientGUI();
		clientGUI.setVisible(true);
	}
	
	public void startGame(String serverIp,String data,String type) {
		 try 
		 {
			 
			client=new Client(serverIp, data,this,type);
			if(type.equals(Client.TYPE_PLAYER)) {
				
				lobby();
			}
			else {
				client.startGameView();
			}
			
		 } catch (AccountNotFoundException | WrongPasswordException | ExistingAccountException e) {
			 
			JOptionPane.showConfirmDialog(this, e.getMessage(), "Excepción", JOptionPane.CANCEL_OPTION);

		 }
		
	}
	
	
	
	public void lobby() {
		remove(first);
		lobby=new LobbyPane();
		add(lobby,BorderLayout.CENTER);
		client.startLobbySocket();
		lobbyThread=new LobbyThread(client);
		lobbyThread.start();
		setSize(new Dimension(400,200));
	}
	
	public void game() {
		game=new Game();
		//Poner panel game
	}
	
	public void upDateGame(boolean status,String[] players,String[] food) {
		if(game==null) {
			game=new Game();
			ArrayList<Player> playerList=new ArrayList<Player>();
			System.out.println("Se crea el mundo");
			for(int i=0;i<players.length;i++) {
				String[] data=players[i].split(",");
				Player actual=new Player(data[0]);
				double posX=Double.parseDouble(data[1]);
				double posY=Double.parseDouble(data[2]);
				double radius=Double.parseDouble(data[4]);
				Color color=new Color(Integer.parseInt(data[3]));
				Ball ball=new Ball(posX, posY, color, radius);
				actual.setBall(ball);
				playerList.add(actual);
				
			}
			game.setPlayers(playerList);
			ArrayList<Ball> arrFood=new ArrayList<>();
			if(food.length==0){
				System.out.println("Vacio");
			}
			for(int i=0;i<food.length;i++){
				String[] data=food[i].split(",");
				System.out.println(food[i]);
				double posX=Double.parseDouble(data[0]);
				double posY=Double.parseDouble(data[1]);
				double radius=Double.parseDouble(data[2]);
				int color;
				try {
					
					color=Integer.parseInt(data[3]);
				} catch (Exception e) {
					// TODO: handle exception
					color=Color.RED.getRGB();
				}
				Ball ball=new Ball(posX, posY, new Color(color), radius);
				arrFood.add(ball);
			}
			game.setArrFood(arrFood);
			draw=new Draw(this);

			
			//TODO
			//La clase Audio server es la que indica la musica disponible para pasarla a panelMusica
			
			
			
			
			if(client.getType().equals(Client.TYPE_PLAYER)) 
			{
				String[] Canciones = new String[4];
				Canciones[0] = "Legends Never Die";
				Canciones[1] = "pumped";
				Canciones[2] = "RISE";
				Canciones[3] = "Yoshi";
				panelMusica = new MusicPane(Canciones,this);
				
				remove(lobby);
				add(draw,BorderLayout.CENTER);
				add(panelMusica, BorderLayout.NORTH);
			}
			else 
			{
				System.out.println("me llamo: "+client.getNick());
				chatPane = new ClienteChat(client);
				String[] Canciones = new String[4];
				Canciones[2] = "RISE";
				Canciones[0] = "Legends Never Die";
				Canciones[1] = "pumped";
				Canciones[3] = "Yoshi";
				panelMusica = new MusicPane(Canciones,this);
				remove(first);
				add(draw,BorderLayout.CENTER);
				JPanel jp1 = new JPanel();
				jp1.setLayout(new BorderLayout());
				
				jp1.add(chatPane, BorderLayout.CENTER);
				jp1.add(panelMusica, BorderLayout.SOUTH);
				
				add(jp1, BorderLayout.EAST);
				
				(new Thread(new Runnable() {
					
					@Override
					public void run() {
						chatPane.recibirMensajesServidor();
						
					}
				})).start();
			}
			
		}
		else {
			ArrayList<Player> jugadores=game.getPlayers();
			game.setActive(status);
			for(int i=0;i<players.length;i++) {
				String[] data=players[i].split(",");
				Player actual=jugadores.get(i);
				if(!actual.getNickName().equals(client.getNick())) {
					
					double posX=Double.parseDouble(data[1]);
					double posY=Double.parseDouble(data[2]);
					double radius=Double.parseDouble(data[4]);
					Color color=new Color(Integer.parseInt(data[3]));
					boolean active=Boolean.parseBoolean(data[5]);
					Ball ball=actual.getBall();
					ball.setPosX(posX);
					ball.setPosY(posY);
					ball.setRadius(radius);
					ball.setColor(color);
					ball.setActive(active);
				}
				else{
					
					double radius=Double.parseDouble(data[4]);
					boolean active=Boolean.parseBoolean(data[5]);
					Ball ball=actual.getBall();
					ball.setRadius(radius);
					ball.setActive(active);
					
				}
			}
			ArrayList<Ball> arrFood=game.getArrFood();
			if(food.length==0){
				System.out.println("Se borra");
				game.setArrFood(new ArrayList<>());
			}
			for(int i=0;i<food.length;i++){
				String[] data=food[i].split(",");
				Ball actual=arrFood.get(i);
				double posX=Double.parseDouble(data[0]);
				double posY=Double.parseDouble(data[1]);
				double radius=Double.parseDouble(data[2]);
				int color;
				try {
					
					color=Integer.parseInt(data[3]);
				} catch (Exception e) {
					// TODO: handle exception
					color=Color.RED.getRGB();
				}
				actual.setColor(new Color(color));
				actual.setPosX(posX);
				actual.setPosY(posY);
				actual.setRadius(radius);
				
			}
		}
		draw.repaint();
		setSize(1000, 1000);
		setLocationRelativeTo(null);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comand=e.getActionCommand();
		if(comand.equals(REGISTER))
		{
			first.remove(logIn);
			
			register = new RegisterPane(this);
			btnRegister.setEnabled(false);
			btnLogIn.setEnabled(true);
			register.getBtnStart().setEnabled(true);
			register.getBtnWatch().setEnabled(true);
			first.add(register,BorderLayout.CENTER);
			pack();
		}
		else if(comand.equals(LOGIN))
		{
			if(register != null)
			{
				first.remove(register);
				register = null;
			}
			btnLogIn.setEnabled(false);
			btnRegister.setEnabled(true);
			logIn.getBtnStart().setEnabled(true);
			logIn.getBtnWatch().setEnabled(true);
			first.add(logIn,BorderLayout.CENTER);
			pack();
			
		}
	}
	public Point mousePos() {
		return draw.mousePos();
	}

	public Draw getDraw() {
		return draw;
	}

	public JPanel getAux() {
		return aux;
	}

	public JButton getBtnLogIn() {
		return btnLogIn;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}

	public LogInPane getLogIn() {
		return logIn;
	}
	
	/*
	public Client getClient() {
		return client;
	}
	*/

	public void setDraw(Draw draw) {
		this.draw = draw;
	}

	public void setAux(JPanel aux) {
		this.aux = aux;
	}

	public void setBtnLogIn(JButton btnLogIn) {
		this.btnLogIn = btnLogIn;
	}

	public void setBtnRegister(JButton btnRegister) {
		this.btnRegister = btnRegister;
	}

	public void setLogIn(LogInPane logIn) {
		this.logIn = logIn;
	}
	/*
	public void setClient(Client client) {
		this.client = client;
	}
	*/


	public LobbyPane getLobby() {
		return lobby;
	}


	public void setLobby(LobbyPane lobby) {
		this.lobby = lobby;
	}


	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}


	public Game getGame() {
		return game;
	}


	public void setGame(Game game) {
		this.game = game;
	}

	
	

}
