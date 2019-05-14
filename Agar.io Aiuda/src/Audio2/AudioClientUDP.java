package Audio2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioClientUDP extends Thread{

	DatagramSocket socketAudio;
	DatagramSocket socketFormat;
	AudioInputStream audioStream;
	AudioFormat audioFormat;
	SourceDataLine sourceLine;
	int changingSocketPort;
	
	public DatagramSocket socketSongs;
	
	public final static int AUDIO_PORT =54321;
	public final static int FORMAT_PORT= 54325;
	public final static int CONST=60000;
	
	public AudioClientUDP() {	
		try {
			socketAudio = new DatagramSocket();
			socketFormat = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		InitiateAudio();
		playAudio();
	}
	
	
	public int getAudioPort() {
		return socketAudio.getLocalPort();
	}
	
	public int getFormatPort() {
		return socketFormat.getLocalPort();
	}
	
	public void changeSong(String song) {
		String songName = "c "+song; 
		byte[] b= songName.getBytes();
		try {
			//TODO
			socketSongs.send(new DatagramPacket(b, b.length, InetAddress.getByName("localhost"),changingSocketPort));
			System.out.println("sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playAudio() {
		byte[] buffer = new byte[CONST];
		try {
			int count;
			while ((count = audioStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
//					System.out.println("b");
					sleep(300);
					sourceLine.write(buffer, 0, count);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InitiateAudio() {

		try {

			byte[] audioBuffer = new byte[CONST];
			byte[] formatInfo= new byte[1024];
			// ...

			while (true) {
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				socketAudio.receive(packet);
				DatagramPacket packetInfo = new DatagramPacket(formatInfo, formatInfo.length);
				socketFormat.receive(packetInfo);
				String[] info= new String(packetInfo.getData()).trim().split(" ");
				// ...
				System.out.println(new String(packetInfo.getData()));
				try {

					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					audioFormat = new AudioFormat(Float.parseFloat(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]), true, false);
					audioStream = new AudioInputStream(byteInputStream, audioFormat,
							audioData.length / audioFormat.getFrameSize());

					//					audioStream = new Audio

					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioStream.getFormat());


					sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceLine.open(audioStream.getFormat());

					sourceLine.start();
					playAudio();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
