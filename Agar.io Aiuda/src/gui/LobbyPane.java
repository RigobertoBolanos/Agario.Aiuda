package gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LobbyPane extends JPanel{
	private JLabel lblPlayers;
	private JLabel lblTime;
	private JTextArea txtPlayers;
	private JPanel aux;

	//private LobbyThread thread;
	
	
	public LobbyPane() {
		setLayout(new BorderLayout());
		aux=new JPanel();
		//thread=new LobbyThread(this, main);
		lblPlayers=new JLabel("Jugadores conectados: ");
		lblTime=new JLabel("Tiempo restante: ");
		txtPlayers=new JTextArea();
		aux.add(lblPlayers);
		aux.add(lblTime);
		add(aux,BorderLayout.NORTH);
		add(txtPlayers,BorderLayout.CENTER);
	}

	public JLabel getLblPlayers() {
		return lblPlayers;
	}

	public void setLblPlayers(JLabel lblPlayers) {
		this.lblPlayers = lblPlayers;
	}

	public JLabel getLblTime() {
		
		return lblTime;
	}

	public void setLblTime(JLabel lblTime) {
		this.lblTime = lblTime;
	}

	public JTextArea getTxtPlayers() {
		return txtPlayers;
	}

	public void setTxtPlayers(JTextArea txtPlayers) {
		this.txtPlayers = txtPlayers;
	}

	

	
	
	

}
