package connection;

public class WrongPasswordException extends Exception{

	public WrongPasswordException()
	{
		super("La contraseņa es incorrecta");
	}
}
