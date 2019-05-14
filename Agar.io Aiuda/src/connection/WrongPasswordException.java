package connection;

public class WrongPasswordException extends Exception{

	public WrongPasswordException()
	{
		super("La contraseña es incorrecta");
	}
}
