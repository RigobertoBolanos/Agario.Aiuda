package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import connection.Client;

public class RegisterPane extends JPanel implements ActionListener{
	
	public final static String START="Start";
	public final static String WATCH="Watch";
	ClientGUI main;
	JPanel aux;
	JPanel aux2;
	JPanel aux3;
	JPanel aux4;
	JLabel lblNick;
	JTextField txtNick;
	JLabel lblMail;
	JTextField txtMail;
	JLabel lblPass;
	JTextField txtPass;
	JButton btnStart;
	JButton btnWatch;
	
	public RegisterPane(ClientGUI main) {
		
		this.main=main;
		setLayout(new GridLayout(4,1));
		setAlignmentX(LEFT_ALIGNMENT);
		aux=new JPanel();
		aux2=new JPanel();
		aux3=new JPanel();
		aux4=new JPanel();
		lblNick=new JLabel("NickName:" );
		txtNick=new JTextField();
		txtNick.setPreferredSize(new Dimension(100, 20));
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
		btnWatch.setActionCommand(WATCH);
		aux.add(lblMail);
		aux.add(txtMail);
		aux2.add(lblPass);
		aux2.add(txtPass);
		aux3.add(new JLabel(" "));
		aux3.add(btnStart);
		aux3.add(btnWatch);
		aux3.add(new JLabel(" "));
		aux4.add(lblNick);
		aux4.add(txtNick);
		add(aux4);
		add(aux);
		add(aux2);
		add(aux3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command=e.getActionCommand();
		if(command.equals(START))
		{
			String resp=JOptionPane.showInputDialog("Ingrese la ip del servidor");
			main.startGame(resp, txtNick.getText() + " " + txtMail.getText()+" "+txtPass.getText(),Client.TYPE_PLAYER);
		}
		else {
			String resp=JOptionPane.showInputDialog("Ingrese la ip del servidor");
			main.startGame(resp, txtNick.getText() + " " + txtMail.getText()+" "+txtPass.getText(),Client.TYPE_VIEWER);
		}
		
	}

	public JTextField getTxtNick() {
		return txtNick;
	}

	public void setTxtNick(JTextField txtNick) {
		this.txtNick = txtNick;
	}

	public JTextField getTxtMail() {
		return txtMail;
	}

	public void setTxtMail(JTextField txtMail) {
		this.txtMail = txtMail;
	}

	public JTextField getTxtPass() {
		return txtPass;
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
