package in.prasilabs.eagleeye;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends Activity
{
	EagleClient ec;

	Button menu;
	ImageButton user;
	ImageButton cam;
	ImageButton log;
	ImageButton ping;
	ImageButton light;
	ImageButton exit;

	ImageButton lock;

	String ip;
	int server;
	int timeout = 2000;
	int pingtime = 500;

	String username;
	String password;
	String reply;

	Socket check;
	SocketAddress addr;

	Boolean connect = false;
	Boolean login = false;
	Boolean LockStatus = false;

	Data dt = new Data();

	Intent cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{

		Intent intnt = new Intent(getBaseContext(),EagleServer.class);
		dt.setIntent(intnt);

		startService(intnt);
		System.out.println("Serverservice is called:");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		System.out.println("Layout set");

		ec = dt.getClientObject();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		/*------------------------Thread policy for android 4.0.4-------------------------*/

		dt.setContext(Home.this);


		user = (ImageButton) findViewById(R.id.user);
		cam = (ImageButton) findViewById(R.id.cam);
		log = (ImageButton) findViewById(R.id.log);
		ping = (ImageButton) findViewById(R.id.ping);
		exit = (ImageButton) findViewById(R.id.exit);
		ping.setImageResource(R.drawable.pingoff);

		check = new Socket();
		server = dt.getServerPort();

		LockStatus = dt.getLockStatus();

		lock = (ImageButton) findViewById(R.id.lock);

		LockStatus = dt.getLockStatus();
		if(LockStatus)
		{
			lock.setImageResource(R.drawable.doorlock);
			lock.refreshDrawableState();
		}
		else
		{
			lock.setImageResource(R.drawable.dooropen);
			lock.refreshDrawableState();
		}

		light = (ImageButton) findViewById(R.id.light);
		if(dt.getLightStatus())
		{
			light.setImageResource(R.drawable.light_on);
			light.refreshDrawableState();
		}
		else
		{
			light.setImageResource(R.drawable.light_off);
			light.refreshDrawableState();
		}


		if(dt.getLoginStatus())
		{
			ping.setImageResource(R.drawable.pingon);
			ping.refreshDrawableState();
		}
		else
		{
			ping.setImageResource(R.drawable.pingoff);
			ping.refreshDrawableState();
		}
		/* ............................................................. */
		/*
		//Declare the timer
		Timer t = new Timer();
		//Set the schedule function and rate
		t.scheduleAtFixedRate(new TimerTask() 
		{
			@Override
			public void run() 
			{
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						try 
						{
							connect = false;
							ip = dt.getIp();
							check = new Socket();
							addr = new InetSocketAddress(ip,server);
							System.out.println("Pinging:"+ip);
							check.connect(addr,timeout);
							connect = check.isConnected();
							check.close();
							if(connect)
							{
								ping.setImageResource(R.drawable.pingwarn);
								//ping.refreshDrawableState();
								System.out.println("PI server is running need to sign in");
							}
							else
							{
								ping.setImageResource(R.drawable.pingoff);
								ping.refreshDrawableState();
								System.out.println("PI server is not running");
							}
						} catch (IOException e1) 
						{
							connect = false;
							ping.setImageResource(R.drawable.pingoff);
							ping.refreshDrawableState();
							System.out.println("PI server is not running");
							e1.printStackTrace();
						}
						if(connect)
						{
							System.out.println("Trying to call sendInfo");
							ec.sendInfo();
							try {
								ec.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							if(dt.getLoginStatus())
							{
								Context context = getApplication();
								CharSequence text = "Welcome";
								int duration = Toast.LENGTH_SHORT;
								Toast tst = Toast.makeText(context, text, duration);
								tst.show();
								tst.setGravity(Gravity.BOTTOM|Gravity.LEFT, 6 , 6);
								ping.setImageResource(R.drawable.pingon);
							//	ping.refreshDrawableState();
							}
							else
							{
								ping.setImageResource(R.drawable.pingwarn);
								ping.refreshDrawableState();
							}
						}
						//Called each time when 1000 milliseconds (1 second) (the period parameter)
						else
						{
							ping.setImageResource(R.drawable.pingoff);
							ping.refreshDrawableState();
						}
					}

				});
			}
		},100,50000); */

		//Set how long before to start calling the TimerTask (in milliseconds)

		//Set the amount of time between each execution (in milliseconds)


		/* ............................................................. */



		ping.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) 
			{
				PingOperation po = new PingOperation(Home.this);
				po.execute();

			}
		});


		user.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				String user = dt.getUsername();
				AlertDialog.Builder status = new AlertDialog.Builder(Home.this);
				status.setTitle("Last Login Status");
				if(dt.getLoginStatus())
				{
					status.setMessage("Logged in as::"+user+"\nAt::"+dt.getTime());
				}
				else
				{
					status.setMessage("Not connected:: Last connected"+dt.getTime());
				}
				status.setPositiveButton("OK", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
					}
				});

				status.show();
			}
		});

		cam.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(dt.getLoginStatus())
				{
					Intent cam = new Intent(Home.this,Cam.class);
					cam.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(cam);
				}
				else
				{
					Context context = getApplication();
					CharSequence text = "Not connected";
					int duration = Toast.LENGTH_SHORT;
					Toast tst = Toast.makeText(context, text, duration);
					tst.show();
					tst.setGravity(Gravity.BOTTOM|Gravity.LEFT, 6 , 6);
				}
			}


		});

		log.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Intent lg = new Intent(Home.this,Log.class);
				lg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(lg);
			}
		});




		lock.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(dt.getLoginStatus())
				{
					if(!dt.getLockStatus())
					{
						int success = message("open");
						if(success == 1)
						{
							lock.setImageResource(R.drawable.dooropen);
							lock.refreshDrawableState();
							dt.setLockstatus(true);
						}

						else
						{
							dt.setLoginStatus(false);
							System.out.println("success is -1");
							lock.setImageResource(R.drawable.doorlock);
							lock.refreshDrawableState();
							dt.setLockstatus(false);
						}
					}
					else
					{
						int success = message("close");
						if(success == 1)
						{
							lock.setImageResource(R.drawable.doorlock);
							lock.refreshDrawableState();
							dt.setLockstatus(false);
						}

						else
						{
							dt.setLoginStatus(false);
							lock.setImageResource(R.drawable.dooropen);
							lock.refreshDrawableState();
							dt.setLockstatus(true);
						}
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Not connected", Toast.LENGTH_SHORT).show();
				}
			}
		});


		light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(dt.getLoginStatus())
				{
					if(!dt.getLightStatus())
					{
						int success = message("lightoff");
						if(success == 1)
						{
							light.setImageResource(R.drawable.light_off);
							light.refreshDrawableState();
							dt.setLightStatus(true);
						}

						else
						{
							dt.setLoginStatus(false);
							System.out.println("success is -1");
							light.setImageResource(R.drawable.light_on);
							light.refreshDrawableState();
							dt.setLightStatus(false);
						}
					}
					else
					{
						int success = message("lighton");
						if(success == 1)
						{
							light.setImageResource(R.drawable.light_on);
							light.refreshDrawableState();
							dt.setLightStatus(false);
						}

						else
						{
							dt.setLoginStatus(false);
							light.setImageResource(R.drawable.light_off);
							light.refreshDrawableState();
							dt.setLightStatus(true);
						}
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Not connected", Toast.LENGTH_SHORT).show();
				}

			}
		});

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				doExit();
			}
		});



	}

	@Override
	public void onBackPressed() 
	{
		Toast.makeText(getApplicationContext(),"Press Exit button to Exit", Toast.LENGTH_SHORT).show();
	}
	private void doExit() 
	{
		AlertDialog.Builder alertDialoge = new AlertDialog.Builder(Home.this);
		alertDialoge.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				NotificationManager NM = dt.getNM();
				NM.cancelAll();
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				finish();
				//System.exit(1);
				//onDestroy();
			}
		});
		alertDialoge.setNegativeButton("No", null);
		alertDialoge.setNeutralButton("Minimize", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{

				Intent startmain = new Intent(Intent.ACTION_MAIN);
				startmain.addCategory(Intent.CATEGORY_HOME);
				startmain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startmain);
			}
		});

		alertDialoge.setMessage("Do you want to exit?");
		alertDialoge.setTitle("Raspi");
		alertDialoge.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		//openset();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.menu.splash:
			openset();
		default :
			openset();
		}
		return false;
	}

	void openset()
	{
		Intent mn = new Intent(Home.this,MenuSet.class);
		mn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(mn);
	}

	int message(String msg)
	{
		ec.sendMessage(msg);
		try {
			ec.join();
		} catch (InterruptedException e) 
		{
			System.out.println("Thread interrupted");
		}
		return ec.ret;
	}



	@Override
	public void onPause()
	{
		super.onPause();

	}



	@Override
	public void onResume()
	{
		super.onResume();
		if(dt.getLoginStatus())
		{
			ping.setImageResource(R.drawable.pingon);
			ping.refreshDrawableState();
		}
		else
		{
			ping.setImageResource(R.drawable.pingoff);
			ping.refreshDrawableState();
		}

	}


	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		NotificationManager NM = dt.getNM();
		NM.cancelAll();
		Notification endnotification = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle("Progress")
		.setContentText("APP exit | Please Relaunch")
		.setTicker("Notification!")
		.setWhen(System.currentTimeMillis())
		.setDefaults(Notification.DEFAULT_SOUND)
		.setAutoCancel(true)
		.setSmallIcon(R.drawable.ic_launcher)
		.build();

		Intent intnt = new Intent(getApplicationContext(),SplashActivity.class);
		PendingIntent pintent=PendingIntent.getActivity(getApplicationContext(), 0, intnt,0);
		endnotification.setLatestEventInfo(getApplicationContext(), "Application exited", "Please Restart the application", pintent);

		NM.notify(2, endnotification);

	}






	class PingOperation extends AsyncTask<String, Void, Void>
	{

		Activity act;
		Data dt = new Data();
		ProgressDialog pd;
		Calendar cal;
		public PingOperation(Home hm)
		{
			act = hm;
		}

		@Override
		protected void onPreExecute() {
			// Things to be done before execution of long running operation.
			//For example showing ProgessDialog
			pd = new ProgressDialog(act);
			pd.setTitle("checking");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();

		}

		@Override
		protected Void doInBackground(String... params) 
		{

			int result = 0;	//0 - Not connected | 1 - Connected, Login success | -1 - Auth problem
			boolean connect = true;
			boolean login = false;
			String ip;
			Socket check;
			int server;
			InetSocketAddress addr;

			try {
				connect = false;
				ip = dt.getIp();
				check = new Socket();
				server = dt.getServerPort();
				addr = new InetSocketAddress(ip,server);
				System.out.println("Pinging:"+ip);
				check.connect(addr,2000);
				connect = check.isConnected();
				check.close();
				if(connect)
				{
					result = -1;


					//ping.refreshDrawableState();
					System.out.println("PI server is running need to sign in");

					ec = dt.getClientObject();
					ec.interrupt();

					ec = new EagleClient();
					ec.start();
					dt.setClientObject(ec);

					String user = dt.getUsername();
					String pass = dt.getPassword();
					System.out.println("Sending login details");
					ec.sendInfo(user,pass);
					System.out.println("Sent succesfully");
					try
					{
						Thread.sleep(2000);
					}catch(InterruptedException e)
					{

					}

					System.out.println("Login status is::"+dt.getLoginStatus());
					if(dt.getLoginStatus())
					{
						login = true;
					}
					else
					{
						login = false;
						connect = true;
					}	
				}
				else
				{
					System.out.println("PI server is not running");
					connect = false;
					login = false;
				}
			}
			catch (IOException e1) {
				connect = false;
				System.out.println("PI server is not running");
				e1.printStackTrace();
			}
			System.out.println("Login status set to"+login);
			dt.setLoginStatus(login);


			// perform long running operation operation
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pd.dismiss();
			if(dt.getLoginStatus())
			{
				cal = Calendar.getInstance();
				dt.setTime(cal.getTime());
				ping.setImageResource(R.drawable.pingon);
				Toast.makeText(act,"Welcome", Toast.LENGTH_SHORT).show();
				if(dt.getLockStatus())
				{
					lock.setImageResource(R.drawable.doorlock);
				}
				else
				{
					lock.setImageResource(R.drawable.dooropen);
				}
				lock.refreshDrawableState();

			}
			else if(connect)
			{
				ping.setImageResource(R.drawable.pingwarn);
				Toast.makeText(act,"Authentication Failed", Toast.LENGTH_SHORT).show();
			}
			else
			{
				ping.setImageResource(R.drawable.pingoff);
				Toast.makeText(act,"Unable to connect", Toast.LENGTH_SHORT).show();
			}
			// execution of result of Long time consuming operation
		}

		protected void onProgressUpdate(Void... values) 
		{
			// Things to be done while execution of long running operation is in progress. For example updating ProgessDialog

		}

	}

}

