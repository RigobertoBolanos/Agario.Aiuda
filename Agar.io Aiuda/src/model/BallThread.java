package model;

import gui.ClientGUI;

public class BallThread extends Thread{
	private ClientGUI gameView;
	private Ball ball;
	
	public BallThread(ClientGUI game,Ball ball) {
		this.gameView=game;
		this.ball=ball;
	}
	
	@Override
	public void run() {
		super.run();
		while(true) {
			gameView.upDate();
			ball.move(gameView.mousePos());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
