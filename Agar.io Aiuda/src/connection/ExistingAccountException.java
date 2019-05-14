package connection;

public class ExistingAccountException extends Exception{
	
	public ExistingAccountException()
	{
		super("La información de cuenta proporcionada ya ha sido registrada");
	}

}
