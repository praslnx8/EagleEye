package in.prasilabs.eagleeye;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class Mic extends Thread
{
	public byte[] buffer;
	public static DatagramSocket socket;
	private int port=8000;
	AudioRecord recorder;

	private int sampleRate = 44100;
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       
	int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
	private boolean status = true;
	public String ip;
	
	public Mic(String iip)
	{
		ip = iip;
	}
	
	public void run()
	{
		
		 try {
			 
			 Data dt = new Data();
			 
			 minBufSize += 512;
			 
			 InetAddress addr = InetAddress.getByName(ip);
			 
             DatagramSocket socket = new DatagramSocket();
             Log.d("VS", "Socket Created");

             byte[] buffer = new byte[minBufSize];

             Log.d("VS","Buffer created of size " + minBufSize);
             DatagramPacket packet;

             System.out.println("Address is "+addr);
             Log.d("VS", "Address retrieved");


             recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,sampleRate,channelConfig,audioFormat,minBufSize*10);
             Log.d("VS", "Recorder initialized");

             recorder.startRecording();


             while(status == true) {


                 //reading data from MIC into buffer
                 minBufSize = recorder.read(buffer, 0, buffer.length);

                 //putting buffer in the packet
                 packet = new DatagramPacket (buffer,buffer.length,addr,port);

                 socket.send(packet);
                // System.out.println("MinBufferSize: " +minBufSize);
                 
                 status = dt.getMic();

             }



         } catch(UnknownHostException e) {
             Log.e("VS", "UnknownHostException");
         } catch (IOException e) {
             e.printStackTrace();
             Log.e("VS", "IOException");
         } 
		
		
	}
	
	
	
}
