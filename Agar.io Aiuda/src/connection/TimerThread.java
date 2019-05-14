package connection;

public class TimerThread extends Thread{
	private int time;
	private int wait;
	private AsignationThread asignationThread;
	
	public TimerThread(AsignationThread asignationThread,int wait){
		time=0;
		this.wait=wait;
		this.asignationThread=asignationThread;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(time<wait){
			System.out.println(time);
			try {
				Thread.sleep(1000);
				time++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Finalizo tiempo");
		asignationThread.setTimeOut(false);
		asignationThread.getServer().getGame().generateFood();
		asignationThread.getServer().getGame().assignUsersBalls();
		asignationThread.getServer().getGameThread().start();
		asignationThread.getServer().setStartView(true);
		//asignationThread.getServer().startServerComm();
		
	
		
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getWait() {
		return wait;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}
	
	

}
