package gui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;

import connection.Client;
import model.*;



public class Draw extends JPanel implements MouseMotionListener{

	private ClientGUI main;
	private BallThread thread;
	private Point mouse;
	private Ball playerBall;
	ArrayList<Ball> food;
	private ArrayList<Player> enemies;
	//private double scale;
	
	public Draw(ClientGUI main) {
		this.main=main;
		mouse=new Point();
		addMouseMotionListener(this);
		setLayout(new BorderLayout());
		setFocusable(true);
		//scale=1
		Game ok=main.getGame();
		if(main.getClient().getType().equals(Client.TYPE_PLAYER)) {
			Player actual=ok.getPlayer(main.getClient().getNick());
			playerBall = actual.getBall();
			thread=new BallThread(main, playerBall);
			thread.start();
		}
		food = main.getGame().getArrFood();
		enemies = main.getGame().getPlayers();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2=(Graphics2D)g;
		//System.out.println(player.getRadius());
		g2.setColor(Color.WHITE);
		g2.drawImage(new ImageIcon("./Background.png").getImage(), 0, 0, this);
		//g2.fillRect(0, 0, GameGUI.ANCHO, GameGUI.LARGO);
		//g2.translate(Game.ANCHO/2, Game.LARGO/2);
		//g2.scale(50.0/player.getRadius(),50.0/player.getRadius());
		//g2.translate(-player.getPosX(), -player.getPosY());
		g2.setFont(new Font("Arial", Font.PLAIN, 30));
		food=main.getGame().getArrFood();
		for(int i=0;i<food.size();i++){
			Ball actual=food.get(i);
			g2.setColor(actual.getColor());
			g2.fillOval((int)actual.getPosX(), (int)actual.getPosY(), (int)actual.getRadius(), (int)actual.getRadius());
//			if(playerBall.eat(actual)){
//				food.remove(actual);
//			}
		}
		
		for (int i = 0; i < enemies.size(); i++)
		{
			Player actualPlayer=enemies.get(i);
			Ball actual= actualPlayer.getBall();
			if(playerBall!=actual && actual.isActive()) {
				g2.setColor(actual.getColor());
				g2.fillOval((int)actual.getPosX(), (int)actual.getPosY(), (int)actual.getRadius(), (int)actual.getRadius());
				g2.setColor(Color.BLACK);
				g2.drawString(actualPlayer.getNickName(), (int)actual.getPosX()+((int)actual.getRadius()/2), (int)actual.getPosY()+((int)actual.getRadius()/2));
			}
		}
		if(main.getClient().getType().equals(Client.TYPE_PLAYER)) {
			
			if(playerBall.isActive()) {
				g2.setColor(playerBall.getColor());
				g2.fillOval((int)playerBall.getPosX()-(int)(playerBall.getRadius()/2), (int)(playerBall.getPosY()-(int)playerBall.getRadius()/2), (int)playerBall.getRadius(), (int)playerBall.getRadius());
				g2.setColor(Color.BLACK);
				g2.drawString(main.getClient().getNick(), (int)playerBall.getPosX(), (int)playerBall.getPosY());
			}
			g2.setColor(Color.BLACK);
			g2.drawString("Puntaje: "+(int)playerBall.getRadius(), 20, 40);
		}
		
		if(!main.getGame().isActive()) {
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("Arial", Font.PLAIN, 100));
			g2.drawString("El ganador es: "+main.getGame().getWinner().getNickName(), 20, this.getHeight()/2);
		}
		
		//System.out.println("Se actualizó draw");
	}
	
	
	public Point mousePos() {
		return mouse;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		mouse=arg0.getPoint();
		
	}
	
}
