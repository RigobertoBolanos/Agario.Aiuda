package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Audio.IndividualAudioCliente;
import connection.AccountNotFoundException;
import connection.Client;
import connection.ExistingAccountException;
import connection.WrongPasswordException;

public class MusicPane extends JPanel implements ActionListener{

	public final static String CAMBIAR_CANCION = "cambiar canción";
	public final static String START = "Reproducir Canción";
	
	private ClientGUI clientGUI;
	private JComboBox<String> jcCanciones;
	private JButton jbControl;

	
	public MusicPane (String[] canciones, ClientGUI clientGUI) {
		
		this.clientGUI = clientGUI;
		setLayout(new GridLayout(1,2));
		jcCanciones = new JComboBox<>(canciones);
		jbControl = new JButton(START);
		jbControl.setActionCommand(START);
		jbControl.addActionListener(this);

		add(jcCanciones);
		add(jbControl);
		
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		String e = evento.getActionCommand();
		if(e.equals(CAMBIAR_CANCION)) 
		{
			clientGUI.getClient().getAudioIndividual().realizarSolicitud(jcCanciones.getSelectedItem()+"");	
		}
		else if (e.equals(START)) 
		{
//			jbControl.setText(CAMBIAR_CANCION);
//			jbControl.setActionCommand(CAMBIAR_CANCION);
			jbControl.setEnabled(false);
			clientGUI.getClient().getAudioIndividual().realizarSolicitud(jcCanciones.getSelectedItem()+"");
			clientGUI.getClient().getAudioIndividual().start();

		}
		
	}
	
	public static void main(String[] args) {
		JFrame fra = new JFrame();
		
		fra.setSize(400,80);
		
		String[]Canciones = new String[4];
		Canciones[0] = "Legends Never Die";
		Canciones[1] = "pumped";
		Canciones[2] = "RISE";
		Canciones[3] = "Yoshi";
		Client client;
		try {
			client = new Client(null, null, null, null);
			client.setServerIp("localhost");
			client.setType(Client.TYPE_PLAYER);
			ClientGUI clientgui = new ClientGUI();
			clientgui.setClient(client);
			MusicPane ms = new MusicPane(Canciones, clientgui);
			fra.add(ms);
			fra.setVisible(true);
		} catch (AccountNotFoundException | WrongPasswordException | ExistingAccountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	

	
}
