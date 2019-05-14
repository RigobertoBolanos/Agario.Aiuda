package Audio2;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioServerUDP extends Thread{

	DatagramSocket socketAudio;
	DatagramSocket socketInfo;
	InetAddress clientAddres;
	TargetDataLine targetDataLine;
	int clientPortAudio;
	int clientFormatPort;
	
	private final byte audioBuffer[] = new byte[60000];
	
	File soundFile;
	String fileName;
	AudioInputStream audioStream;
	boolean pause;
	
	public AudioServerUDP(InetAddress inet, int portAudio, int portFormat, String fileN) throws UnsupportedAudioFileException, IOException{
		socketAudio = new DatagramSocket();
		socketInfo = new DatagramSocket();
		clientAddres = inet;
		clientPortAudio = portAudio;
		clientFormatPort = portFormat;
		fileName = fileN;
		
		changeAudio(fileName);
		if (fileN != null && !fileN.contentEquals(""))
			pause = false;
		else
			pause = true;	
	}
	
	public void changeAudio(String fileN) throws UnsupportedAudioFileException, IOException {
		pause = true;
		if (fileN != null && !fileN.contentEquals("")) {
			pause = false;
			soundFile = new File("./songs/" + fileN.trim() + ".wav");
//			soundFile=new File("C:\\Users\\Steven\\Desktop\\dataset\\"+fileN.trim()+".wav");

			audioStream = AudioSystem.getAudioInputStream(soundFile);
			fileName = fileN;
			if (targetDataLine != null) {
				
				
				targetDataLine.close();
				System.out.println("targetData: closed");
			}
			setupAudio();

		} else {

			soundFile = new File("./songs/malu.wav");
			audioStream = AudioSystem.getAudioInputStream(soundFile);

		}

	}
	
	public AudioFormat getAudioFormat() {
		return audioStream.getFormat();
	}
	
	public int getClientPort() {
		return clientPortAudio;
	}
	
	public void run() {
		while (true) {

			try {
				sendAudio();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void pauseMusic() {
		pause = true;
	}
	
	public void continueMusic() {
		pause = false;
	}
	
	public void sendAudio() throws IOException, LineUnavailableException, InterruptedException {

		while (true) {
			int length = 0;
			if (!pause)
				length = audioBuffer.length;
			int count = audioStream.read(audioBuffer, 0, length);
			AudioFormat aud = getAudioFormat();
			String info = aud.getSampleRate() + " " + aud.getSampleSizeInBits() + " " + aud.getChannels();
//			System.out.println(info);
			byte[] convertedInfo = info.getBytes();
			if (count > 0 && length != 0) {
				DatagramPacket packetInfo = new DatagramPacket(convertedInfo, convertedInfo.length, clientAddres,
						clientFormatPort);
				socketInfo.send(packetInfo);
//				System.out.println("a");
				DatagramPacket packet = new DatagramPacket(audioBuffer, length, clientAddres, clientPortAudio);
				socketAudio.send(packet);
				Thread.sleep(300);
			}
		}
	}
	
	public AudioFormat setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

			try {				
				targetDataLine.open(audioFormat);
			}catch (Exception e) {
				// TODO: handle exception
			}
			targetDataLine.start();
			return audioFormat;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}

		return null;
	}
}
