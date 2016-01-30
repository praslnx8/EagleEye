package in.prasilabs.eagleeye;

import in.prasilabs.mjpeg.*;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;





import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Cam extends Activity
{
	Button menu;
	ImageButton door;
	ImageButton mutebtn;
	ImageButton divert;

	MjpegView vv;

	String ip;
	int vdport;
	int adport;
	String uri;

	Boolean lockstatus;
	Boolean on = false;;
	Boolean mute = false;

	EagleClient ec;

	Mic mc;

	Data dt = new Data();
	AudioServer adsrv = new AudioServer();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cam);

//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//		StrictMode.setThreadPolicy(policy);

		dt.setSpeaker(false);
		try
		{
			adsrv.interrupt();
		}catch(Exception e)
		{
			
		}
		dt.setSpeaker(true);
		adsrv.start();

		dt.setMic(false);
		
		ip = dt.getIp();
		vdport = dt.getvdPort();
		adport = dt.getadPort();
		lockstatus = dt.getLockStatus();

		uri = "http://"+ip+":"+vdport+"/";

		ec = dt.getClientObject();
		ec.sendMessage("call");

		door = (ImageButton) findViewById(R.id.door);
		mutebtn = (ImageButton) findViewById(R.id.mute);
		vv= (MjpegView) findViewById(R.id.video);

		new DoRead().execute(uri);

		lockstatus = dt.getLockStatus();
		if(lockstatus)
		{
			door.setImageResource(R.drawable.doorlock);
			on = true;
		}
		else
		{
			door.setImageResource(R.drawable.dooropen);
			on = false;
		}

		door.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{

				if(!on)
				{
					int success = message("open");
					if(success == 1)
					{
						door.setImageResource(R.drawable.dooropen);
						on = true;
						//TODO door.setBackgroundResource(R.drawable.unlocked);
						dt.setLockstatus(true);
					}
					else
					{
						door.setImageResource(R.drawable.doorlock);
						on = false;
						dt.setLockstatus(false);
					}

				}
				else
				{
					int success = message("close");
					if(success == 1)
					{
						door.setImageResource(R.drawable.dooropen);
						on = false;
						//door.setBackgroundResource(R.drawable.locked);
						dt.setLockstatus(false);
					}
					else
					{
						door.setImageResource(R.drawable.doorlock);
						on = true;
						dt.setLockstatus(false);
					}
				}
			}
		});


		mutebtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(dt.getMic())
				{

					dt.setMic(false);
					mutebtn.setImageResource(R.drawable.mute);

				}
				else
				{

					dt.setMic(true);
					mc = new Mic(dt.getIp());
					mc.start();
					mutebtn.setImageResource(R.drawable.muteoff);


				}
			}
		});
	}


	public int message(String msg)
	{
		return ec.sendMessage(msg);

	}

	@Override
	public void onBackPressed() 
	{
		ec.sendMessage("callend");
		dt.setMic(false);
		dt.setSpeaker(false);
		finish();


	}



	@Override
	public void onPause() 
	{
		super.onPause();
		vv.stopPlayback();
	}

	public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
		protected MjpegInputStream doInBackground(String... url) {
			//TODO: if camera has authentication deal with it and don't just not work
			HttpResponse res = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();     
			try {
				res = httpclient.execute(new HttpGet(URI.create(url[0])));
				if(res.getStatusLine().getStatusCode()==401){
					//You must turn off camera User Access Control before this will work
					return null;
				}
				return new MjpegInputStream(res.getEntity().getContent());  
			} catch (ClientProtocolException e) {
				//e.printStackTrace();
				//Error connecting to camera
			} catch (IOException e) {
				//e.printStackTrace();
				//Error connecting to camera
			}

			return null;
		}

		protected void onPostExecute(MjpegInputStream result) {
			vv.setSource(result);
			vv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
			vv.showFps(true);

			MediaPlayer mpl = new MediaPlayer();
			try {
				mpl.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mpl.setDataSource("http://localhost:1234/");

				mpl.prepare();
				System.out.println("Audio prepared");
			} catch (IllegalArgumentException e) {

				//e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			if(!mpl.isPlaying())
			{
				mpl.start();
			}
			else
			{
				mpl.pause();
				mpl.start();
			}

		} 
	}



}
