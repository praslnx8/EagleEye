package in.prasilabs.eagleeye;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import android.util.Log;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageButton;

public class EagleServer extends IntentService
{
	private int idrport;
	private ServerSocket serv;
	private Socket sock;
	private DataInputStream is;
	private String msg;
	private MParser mp;
	private Data dt;
	public EagleServer() 
	{
		super("Eagle Eye");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		dt = new Data();
		Log.i("status", "Server is accesed");
		//--------------
		NotificationManager notificationManager ;
		notificationManager = dt.getNM();
		String notificationText = "Server is running";
		Notification servernotification;
		servernotification = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("Eagle Eye")
		.setContentText(notificationText)
		.setTicker("Notification!")
		.setWhen(System.currentTimeMillis())
		.setDefaults(Notification.DEFAULT_SOUND)
		.setAutoCancel(false)
		.setSmallIcon(R.drawable.ic_launcher)
		.build();

		Intent intnt = new Intent(getApplicationContext(),Home.class);
		PendingIntent pintent=PendingIntent.getActivity(getApplicationContext(), 0, intnt,0);
		servernotification.setLatestEventInfo(getApplicationContext(), "Server is Running", "Eagle Eye is Running", pintent);
		Log.i("status", "Notification started");
		servernotification.flags = Notification.FLAG_ONGOING_EVENT;

		Notification serverclose;
		serverclose = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("Eagle Eye")
		.setContentText(notificationText)
		.setTicker("Notification!")
		.setWhen(System.currentTimeMillis())
		.setDefaults(Notification.DEFAULT_SOUND)
		.setAutoCancel(false)
		.setSmallIcon(R.drawable.ic_launcher)
		.build();

		dt = new Data();
		dt.getClientObject();
		//context = dt.getContext();
		idrport = dt.getdrPort();	//Integer.parseInt(drport);		//Use numberFormat exception if problem occurs
		try 
		{
			serv = new ServerSocket(idrport);
			System.out.println("Droid server established at :"+idrport);
			boolean newcon = true;
			while(true)
			{
				try
				{
					sock = serv.accept();
					/*while((!(sock.getRemoteSocketAddress().toString().split(":")[0].equals(dt.getIp())))||(newcon))//prf.getString("ip", "192.168.1.91"))))
					{
						Log.w("Connection","Connection wrongly with "+sock.getRemoteSocketAddress().toString().split(":")[0]);
						serv.close();
						sock = serv.accept();
					}*/


					System.out.println("Connection accepted with "+sock.getRemoteSocketAddress());
					is = new DataInputStream(sock.getInputStream());
					Log.i("connection", "Stream created");
					while(sock.isConnected())
					{	
						
						Log.i("msgstat", "Waiting for message from PI");
						msg = is.readUTF();
						Log.i("msg", "Message arrived:"+msg);

						mp = new MParser(msg);
						mp.run();

					}
					
					newcon = true;

				}catch(SocketException e)
				{
					Log.i("connection", "PI Connection closed");
					e.printStackTrace();
					newcon = true;
					//ImageButton pingbtn = dt.getPingButton();
					//pingbtn.setImageResource(R.drawable.pingoff);
					//pingbtn.refreshDrawableState();
					dt.setLoginStatus(false);
				}

			}

		}
		catch (IOException e) 
		{
			intnt = new Intent(getApplicationContext(),Home.class);
			pintent=PendingIntent.getActivity(getApplicationContext(), 0, intnt,0);
			serverclose.setLatestEventInfo(getApplicationContext(), "Server is Running| Not connected", "Eagle Eye is Running", pintent);

			serverclose.flags = Notification.FLAG_ONGOING_EVENT;


			notificationManager.notify(1, serverclose);

			Log.wtf("exception", "IO exception at eagle server| PI disconnected");
			dt.setLoginStatus(false);
			e.printStackTrace();
		}	

	}

	class MParser
	{
		String msg;
		public MParser(String mesg)
		{
			msg = mesg;
		}
		@SuppressWarnings("deprecation")
		public void run()
		{
			Data dt = new Data();
			NotificationManager NM = dt.getNM();
			if(msg.equals("call"))
			{

				String notificationText = "Incoming Call";
				Notification myNotification;
				myNotification = new NotificationCompat.Builder(getApplicationContext())
				.setContentTitle("Eagle Eye")
				.setContentText(notificationText)
				.setTicker("Call from PI")
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_SOUND)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_launcher)
				.build();

				Intent intnt = new Intent(getApplicationContext(),Cam.class);
				PendingIntent pintent=PendingIntent.getActivity(getApplicationContext(), 0, intnt,0);
				myNotification.setLatestEventInfo(getApplicationContext(), "Guest at door", "Click to view", pintent);


				NM.notify(3, myNotification);
			}

			else if(msg.equals("on"))
			{
				dt.setLockstatus(true);
			}
			else if(msg.equals("off"))
			{
				dt.setLockstatus(false);
			}
			else if(msg.equals("success"))
			{
				dt.setLoginStatus(true);
			}
			else
			{
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}