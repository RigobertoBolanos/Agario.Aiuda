package gui;

import connection.Server;
import model.Game;

public class GameThread extends Thread{
	
	private int countDown;
	private int limit;
	private Server server;
	
	public GameThread(Server server,int limit) {
		this.server=server;
		this.limit=limit;
		this.countDown=limit;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			Game game=server.getGame();
			while(game.isActive()) {
				
				if(game.onePlayer()) {
					game.setActive(false);
				}	
				
				if(countDown>0) {
					Thread.sleep(1000);
					countDown--;
				}
				else {
					game.setActive(false);
				}
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
