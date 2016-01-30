package in.prasilabs.eagleeye;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

public class AudioServer extends Thread
{
	
	int port = 50005;
    AudioFormat format = new AudioFormat();

    private int sampleRate = 16000;
    @SuppressWarnings("deprecation")
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;       
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    
    Data dt = new Data();
    
    public void run()
    {
    	 byte[] data = new byte[512];       
         
         try
         {
         		
        	 System.out.println("Audi Server is started");

         DatagramSocket dsk = new DatagramSocket(port);
         DatagramPacket dgp = new DatagramPacket(data, data.length);
         
//         ByteArrayInputStream bis = new ByteArrayInputStream(dgp.getData());
     
         AudioTrack speaker = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, minBufSize, AudioTrack.MODE_STREAM);
           
         while(dt.getSpeaker())
         {
         	dsk.receive(dgp);
         	byte[] addata = dgp.getData();
         	
         	speaker.write(addata, 0, addata.length);
         	
         	speaker.play();
         }
         
         System.out.println("Audio server stopped");
         this.interrupt();
         
         } catch(SocketException e)
         {
         	e.printStackTrace();
         } catch(IOException e)
         {
         	e.printStackTrace();
         }
    }
    
}
