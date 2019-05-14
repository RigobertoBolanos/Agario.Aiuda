package Audio;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import connection.Server;

public class IndividualMusicRequestServer extends Thread {
	
	public final static int FORMAT_PORT = 9786;
	public final static int AUDIO_PORT = 9787;
	public final static int TIME_SLEEP = 300;
	public final static int TAMANHO_BUFF = 60000;
	
	private byte audioBuffer[] = new byte[60000];
	private byte formatBuffer[] = new byte[60000];	
	
	private Server server;
	private IndividualAudioServer individualAudioServer;
	private boolean hadAudio;
	private DatagramSocket socketChanges;
	
	public IndividualMusicRequestServer(Server server)
	{
		try
		{
			
			socketChanges = new DatagramSocket();
			this.server = server;
			hadAudio = false;
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		super.run();
		try {
            System.out.println("LISTO PARA MUSICA INDIVIDUAL"); 
            //Siempre atendera peticiones
            while (true) {
            	byte[] buffer = new byte[TAMANHO_BUFF];
            	                //Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                //Recibo el datagrama
                System.out.println("ESCUCHANDO PETICIONES");
                socketChanges.receive(peticion);
                System.out.println("Recibo la informacion del cliente");
                 
                //Convierto lo recibido y mostrar el mensaje
                String mensaje = new String(peticion.getData());
                
               
                String nombreCancion = new String(mensaje);
                
                //Obtengo el puerto y la direccion de origen
                //Sino se quiere responder, no es necesario
                int puertoCliente = peticion.getPort();
                InetAddress direccionCliente = peticion.getAddress();   
                System.out.println(peticion.getAddress());
                String respuesta = "preparamos: "+mensaje;
                buffer = respuesta.getBytes();
                
                //creo el datagrama
                DatagramPacket paqueterespuesta = new DatagramPacket(buffer, buffer.length, direccionCliente, puertoCliente);
 
                //Envio la información
                
                if (hadAudio == false)
                {
                	System.out.println("aqui debe entrar");
                	socketChanges.send(paqueterespuesta);
                	individualAudioServer = new IndividualAudioServer(server, nombreCancion, direccionCliente, socketChanges);
                	individualAudioServer.start();
                	System.out.println("Se configuró el audio por primera vez");

                	hadAudio = true;
                }
                else if(hadAudio = true)
                {
                	
                	individualAudioServer.setPlaying(false);
                	while (individualAudioServer.isAlive())
                	{
                		System.out.println("esta vivo");
                	}
                	individualAudioServer = new IndividualAudioServer(server, nombreCancion, direccionCliente, socketChanges);
                	individualAudioServer.start();
                	System.out.println("Se configuró el audio por segunda vez");
                }
             
            }
 
        } catch (SocketException ex) {
        	ex.printStackTrace();
//            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        	ex.printStackTrace();
//            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public byte[] getAudioBuffer() {
		return audioBuffer;
	}

	public void setAudioBuffer(byte[] audioBuffer) {
		this.audioBuffer = audioBuffer;
	}

	public byte[] getFormatBuffer() {
		return formatBuffer;
	}

	public void setFormatBuffer(byte[] formatBuffer) {
		this.formatBuffer = formatBuffer;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public IndividualAudioServer getIndividualAudioServer() {
		return individualAudioServer;
	}

	public void setIndividualAudioServer(IndividualAudioServer individualAudioServer) {
		this.individualAudioServer = individualAudioServer;
	}

	public boolean isHadAudio() {
		return hadAudio;
	}

	public void setHadAudio(boolean hadAudio) {
		this.hadAudio = hadAudio;
	}

	public DatagramSocket getSocketChanges() {
		return socketChanges;
	}

	public void setSocketChanges(DatagramSocket socketChanges) {
		this.socketChanges = socketChanges;
	}
	
	
}
