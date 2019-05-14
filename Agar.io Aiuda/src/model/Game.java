package model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Game implements Serializable {
	
	//Constantes
	public final static int MAX_FOOD=500;
	
	
	public static long MATCH_TTL = 5000*60000;
	public static int MAX_PLAYERS = 5;
	public static int MIN_PLAYERS = 2;
	public static long MAX_LOBBY_TTL = 2000*60000;
	
	//Atributos
	
	//Relaciones
	//relación que contiene las esferas que son comida
	private ArrayList<Ball> food;
	
	//relación que contiene a los jugadores
	private ArrayList<Player> players;
	
	private boolean active;
		
	//Constructor
	
	
	//métodos
	public Game() {
		
		food = new ArrayList<>();
		players = new ArrayList<>();
		active=true;
		
	}
	/**
	 * Genera 10.000 puntos de comida, cada uno con una posición al azar según el maximo largo y ancho
	 * del screen que manejará el servidor.
	 */
	public void generateFood () {
		food = new ArrayList<>();

		for (int i = 0; i < MAX_FOOD; i++) {		
			double posX = (Math.random() * 1000)+1;
			double posY = (Math.random() *1000)+1;
			
			Random rand = new Random(); 
			float r = rand.nextFloat(); 
			float g = rand.nextFloat(); 
			float b = rand.nextFloat(); 
			Color color = new Color (r,g,b);
			
			double radius = Ball.FOOD_RADIOUS;
			
			food.add( new Ball(posX, posY, color, radius));			
		}
		
	}
	
	public void addFood() {
		
			
				double posX = (Math.random() * 1000)+1;
				double posY = (Math.random() * 1000)+1;
				
				Random rand = new Random(); 
				float r = rand.nextFloat(); 
				float g = rand.nextFloat(); 
				float b = rand.nextFloat(); 
				Color color = new Color (r,g,b);
				
				double radius = Ball.FOOD_RADIOUS;
				
				food.add( new Ball(posX, posY, color, radius));	
			
		
	}
	
	/**
	 * Le asigna a cada uno de los usuarios una esfera que está en una posicion al azar según 
	 * el largo y ancho del screen que manejará el servidor
	 */
	public ArrayList<Player> assignUsersBalls() {		
		for (int i = 0; i < players.size(); i++) {
			double posX = (Math.random() * 1000)+1;
			double posY = (Math.random() * 1000)+1;
			
			Random rand = new Random(); 
			float r = rand.nextFloat(); 
			float g = rand.nextFloat(); 
			float b = rand.nextFloat(); 
			Color color = new Color (r,g,b);
			
			double radius = Ball.USER_RADIOUS;
			double speed = Ball.USER_SPEED;
			
			Ball esfera = new Ball(posX, posY, color, radius);
			players.get(i).setBall(esfera);
		}
		return players;
	}
	
	public boolean onePlayer() {
		boolean one=false;
		int count=0;
		for(int i=0;i<players.size();i++) {
			if(players.get(i).getBall().isActive()) {
				count++;
			}
		}
		if(count==1) {
			one=true;
		}
		return one;
	}
	
	public Player getWinner() {
		Player winner=null;
		Player temp=null;
		double max=-1;
		for(int i=0;i<players.size();i++) {
			Player actual=players.get(i);
			Ball actualBall=actual.getBall();
			if(actualBall.isActive()) {
				if(actualBall.getRadius()>max) {
					max=actualBall.getRadius();
					temp=actual;
				}
				
			}
		}
		winner=temp;
		return winner;
	}

	public ArrayList<Ball> getArrFood() {
		return food;
	}



	public void setArrFood(ArrayList<Ball> arrFood) {
		this.food = arrFood;
	}
	
	public ArrayList<Ball> getFood() {
		return food;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public void setFood(ArrayList<Ball> food) {
		this.food = food;
	}
	
	public Player getPlayer(String nick) {
		Player player=null;
		for(int i=0;i<players.size();i++) {
			Player actual=players.get(i);
			if(actual.getNickName().equals(nick)) {
				player=actual;
			}
		}
		return player;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	

	
	

}