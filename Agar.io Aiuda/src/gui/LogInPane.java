package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import connection.Client;

//import Conection.Client;

public class LogInPane extends JPanel implements ActionListener{
	
	public final static String START="Start";
	public final static String WATCH="Watch";
	public final static String LOGS = "./Logs.txt";
	ClientGUI main;
	JPanel aux;
	JPanel aux2;
	JPanel aux3;
	JLabel lblMail;
	JTextField txtMail;
	JLabel lblPass;
	JTextField txtPass;
	JButton btnStart;
	JButton btnWatch;
	
	public LogInPane(ClientGUI main) {
		this.main=main;
		setLayout(new GridLayout(3,1));
		setAlignmentX(LEFT_ALIGNMENT);
		aux=new JPanel();
		aux2=new JPanel();
		aux3=new JPanel();
		lblMail=new JLabel("Correo:");
		txtMail=new JTextField();
		txtMail.setPreferredSize(new Dimension(120, 20));
		lblPass=new JLabel("Password:");
		txtPass=new JTextField();
		txtPass.setPreferredSize(new Dimension(100, 20));
		btnStart=new JButton("Play");
		btnStart.addActionListener(this);
		btnStart.setEnabled(false);
		btnStart.setActionCommand(START);
		btnWatch=new JButton("Watch");
		btnWatch.addActionListener(this);
		btnWatch.setEnabled(false);
		btnWatch.setActionCommand(WATCH);
		aux.add(lblMail);
		aux.add(txtMail);
		aux2.add(lblPass);
		aux2.add(txtPass);
		aux3.add(new JLabel(" "));
		aux3.add(btnStart);
		aux3.add(btnWatch);
		aux3.add(new JLabel(" "));
		add(aux);
		add(aux2);
		add(aux3);
		
	}
	
	public String login(String email, String password) throws IOException{
		String permit = "";
		File logs = new File(LOGS);
		BufferedReader br = new BufferedReader(new FileReader(logs));
		String account = br.readLine();
		while(account != null)
		{
			String[] data = account.split(",");
			if(email.equals(data[0]) && password.equals(data[1]));
			{
				permit = data[2];
			}
		}
		return permit;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando=e.getActionCommand();
		if(comando.equals(START))
		{
			String resp=JOptionPane.showInputDialog("Ingrese la ip del servidor");
			main.startGame(resp, txtMail.getText()+" "+txtPass.getText(),Client.TYPE_PLAYER);
		}
		else {
			String resp=JOptionPane.showInputDialog("Ingrese la ip del servidor");
			main.startGame(resp, txtMail.getText()+" "+txtPass.getText(),Client.TYPE_VIEWER);
		}
		
	}

	public JTextField getTxtMail() {
		return txtMail;
	}

	public JTextField getTxtPass() {
		return txtPass;
	}

	public void setTxtMail(JTextField txtMail) {
		this.txtMail = txtMail;
	}

	public void setTxtPass(JTextField txtPass) {
		this.txtPass = txtPass;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(JButton btnStart) {
		this.btnStart = btnStart;
	}

	public JButton getBtnWatch() {
		return btnWatch;
	}

	public void setBtnWatch(JButton btnWatch) {
		this.btnWatch = btnWatch;
	}
	

	
}
