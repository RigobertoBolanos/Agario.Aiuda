package Audio;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import connection.Server;


 
public class IndividualAudioServer extends Thread{

	public final static int PUERTO_SERVIDOR = 5000;
	public final static int FORMAT_PORT = 9781;
	public final static int AUDIO_PORT = 9782;
	public final static int TIME_SLEEP = 300;
	public final static int TAMANHO_BUFF = 60000;
	
	private byte audioBuffer[] = new byte[60000];
	private byte formatBuffer[] = new byte[60000];	
	
	private Server server;
	private AudioInputStream audioStream;
	private TargetDataLine targetDataLine;
	private String nombreCancion;
	private InetAddress direccionCliente;
	private boolean isPlaying;
	private DatagramSocket socketMusica;


	
	public IndividualAudioServer(Server server, String nombreCancion, InetAddress direccionCliente, DatagramSocket socketMusica) 
	{
		this.server = server;
		isPlaying = false;
		this.socketMusica = socketMusica;
		this.direccionCliente = direccionCliente;
		this.nombreCancion = nombreCancion;
		cargarCancion(nombreCancion);
		
	}
	public void changeAudio(String nombreCancion)
	{
		try 
		{
			
			String path = "./Musica/"+nombreCancion.trim()+".wav";
			System.out.println(path);
			File file= new File(path);
			audioStream = AudioSystem.getAudioInputStream(file);
			this.nombreCancion = nombreCancion;
			if(targetDataLine != null) 
			{							
				targetDataLine.close();
			}
			setupAudio();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		isPlaying = true;
		indiAudio();
		targetDataLine.close();
		try {
			audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Murió el hilo del audio");
	}

	public void indiAudio() {
		try {		
			System.out.println("server inicia transmision musica");
			while (isPlaying) {
				int count = audioStream.read(audioBuffer, 0, audioBuffer.length);
				if (count > 0) {
//					System.out.println("Se envian datos de musica");
					String infoFormat = audioStream.getFormat().getSampleRate()+" "+audioStream.getFormat().getSampleSizeInBits()+" "+audioStream.getFormat().getChannels();
					formatBuffer = infoFormat.getBytes();
					DatagramPacket packetFormat =  new DatagramPacket(formatBuffer, formatBuffer.length, direccionCliente, FORMAT_PORT);
					socketMusica.send(packetFormat);
					
					DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length, direccionCliente, AUDIO_PORT);
					socketMusica.send(packet);
					sleep(TIME_SLEEP);
				}
			}
		} catch (Exception ex) {
			 ex.printStackTrace();
		}
	}

	
	
	public void cargarCancion (String nombreCancion) {
		try {
			System.out.println("Cancion cargada");
			System.out.println(nombreCancion);
			String path = "./Musica/"+nombreCancion.trim()+".wav";
			System.out.println(path);
			File file= new File(path);
			audioStream= AudioSystem.getAudioInputStream(file);
			setupAudio();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setupAudio() {
		try {
			AudioFormat audioFormat =audioStream.getFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			try
			{
				targetDataLine.close();
				targetDataLine.open(audioFormat);
			}catch (Exception e) {
				System.out.println("Error en targetDataline.open()");
			}
			targetDataLine.start();
		} catch (IllegalThreadStateException ex) {
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.exit(0);
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
	public AudioInputStream getAudioStream() {
		return audioStream;
	}
	public void setAudioStream(AudioInputStream audioStream) {
		this.audioStream = audioStream;
	}
	public TargetDataLine getTargetDataLine() {
		return targetDataLine;
	}
	public void setTargetDataLine(TargetDataLine targetDataLine) {
		this.targetDataLine = targetDataLine;
	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public String getNombreCancion() {
		return nombreCancion;
	}
	public void setNombreCancion(String nombreCancion) {
		this.nombreCancion = nombreCancion;
	}
	public InetAddress getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(InetAddress direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	
}