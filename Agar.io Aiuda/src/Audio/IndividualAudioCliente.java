package Audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import connection.AccountNotFoundException;
import connection.Client;
import connection.ExistingAccountException;
import connection.WrongPasswordException;
 
public class IndividualAudioCliente extends Thread{

	public final static int TAMANHO_BUFF = 60000;
	public final static int FORMAT_PORT = 9781;
	public final static int AUDIO_PORT = 9782;
	
	private int serverPort;
	private boolean isPlaying;
	private AudioInputStream audioInputStream;
	private SourceDataLine sourceDataLine;
	private Client client;
	
	
	public IndividualAudioCliente(Client client, int serverPort)
	{
		this.client = client;
		this.serverPort = serverPort;
		isPlaying = false;
	    try 
	    {
			client.setSocketFormat(new DatagramSocket(FORMAT_PORT));
			client.setSocketMusica(new DatagramSocket(AUDIO_PORT));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void realizarSolicitud (String cancion) {
		 byte[] buffer = new byte[TAMANHO_BUFF];
	        try {
	            //Obtengo la localizacion de localhost
	            InetAddress direccionServidor = InetAddress.getByName(client.getServerIp());
	 
	            //Creo el socket de UDP
	            DatagramSocket socketUDP = new DatagramSocket();
	 
	            String mensaje = cancion;
	 
	            //Convierto el mensaje a bytes
	            buffer = mensaje.getBytes();
	 
	            //Creo un datagrama
	            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, serverPort);
	 
	            //Lo envio con send
	            System.out.println("Envio el datagrama");
	            socketUDP.send(pregunta);
	 
	            //Preparo la respuesta
	            buffer = new byte[TAMANHO_BUFF];
	            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
	 
	            //Recibo la respuesta
	            socketUDP.receive(peticion);
	            System.out.println("Recibo la respuesta");
	 
	            //Cojo los datos y lo muestro
	            mensaje = new String(peticion.getData());
	            System.out.println(mensaje);
	 
	            //cierro el socket
	 
	        } catch (SocketException ex) {
	        	ex.printStackTrace();
//	            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (UnknownHostException ex) {
	        	ex.printStackTrace();
//	            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	        	ex.printStackTrace();
//	            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
	        }
	}

	public void iniciarAudio() {
	
		try {				
//			InetAddress inetAddress = InetAddress.getByName("localhost");
			
			byte[] audioBuffer = new byte[TAMANHO_BUFF];
			byte[] formatBuffer = new byte[TAMANHO_BUFF];
			
			while (isPlaying) {
				DatagramPacket packetFormat = new DatagramPacket(formatBuffer, formatBuffer.length);
				client.getSocketFormat().receive(packetFormat);
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				client.getSocketMusica().receive(packet);
				
				try {
					byte audioData[] = packet.getData();
					byte formatData[] = packetFormat.getData();
					
					String infoFormato = new String(formatData);
					String[] data  = infoFormato.split(" ");

					float data0 = Float.parseFloat(data[0]);
					int data1 = Integer.parseInt(data[1]);
					double data2 = Double.parseDouble(data[2]);					
					
					AudioFormat af = new AudioFormat(data0,data1,(int)data2, true, false);
					
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					
					AudioFormat audioFormat = af;	
					audioInputStream = new AudioInputStream(byteInputStream, audioFormat,audioData.length / audioFormat.getFrameSize());					
					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
					sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceDataLine.open(audioFormat);
					sourceDataLine.start();
					playAudio();
				} catch (Exception e) {
					System.out.println("error");
				}

			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void playAudio() {
		byte[] buffer = new byte[TAMANHO_BUFF];
		try {
			int count;
			while ((count = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
					sleep(AudioCliente.TIME_SLEEP);
					sourceDataLine.write(buffer, 0, count);		
				}
			}
		} catch (Exception e) {
		}
	}
	
	@Override
	public void run() {
		isPlaying = true;

		iniciarAudio();
	}
	
//	public static void main(String[] args) {
//		Client client;
//		try {
//			client = new Client("localhost", null, null, null);
//			IndividualAudioCliente ic = new IndividualAudioCliente(client);
//			ic.realizarSolicitud("Yoshi");
//			ic.start();
//		} catch (AccountNotFoundException | WrongPasswordException | ExistingAccountException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}
	public void setAudioInputStream(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
	}
	public SourceDataLine getSourceDataLine() {
		return sourceDataLine;
	}
	public void setSourceDataLine(SourceDataLine sourceDataLine) {
		this.sourceDataLine = sourceDataLine;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	
	
}