package model;

import java.io.Serializable;

public class Player implements Serializable{

	//atributos
	private String nickName;
//	private String e_mail;
//	private String password;
	
	//relaciones
	private Ball ball;

	//Constructor
	public Player(String nickName) {
		super();
		this.nickName = nickName;
//		this.e_mail = e_mail;
//		this.password = password;
	}
	
	//métodos
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	public String getNickName() {
		return nickName;
	}
//	public String getE_mail() {
//		return e_mail;
//	}
//	public String getPassword() {
//		return password;
//	}
//	public void setNickName(String nickName) {
//		this.nickName = nickName;
//	}
//	public void setE_mail(String e_mail) {
//		this.e_mail = e_mail;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}

}