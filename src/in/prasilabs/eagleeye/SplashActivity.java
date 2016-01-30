package in.prasilabs.eagleeye;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.ImageView;

public class SplashActivity extends Activity 
{
	private ImageView bootImage;
	EagleClient ec;
	NotificationManager NM;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		bootImage = (ImageView) findViewById(R.id.bootimg);  //Bootanimation splash image
		bootImage.setVisibility(1);

		Data dt = new Data(); 

		/*------ Strict mode policy to work on higher android 4.0.3 ---------------*/

		//	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//	StrictMode.setThreadPolicy(policy);

		/*------------Strict mode policy ends------------------------------------*/

		SharedPreferences prf = PreferenceManager.getDefaultSharedPreferences(this);
		dt.setSharedPreference(prf);
		System.out.println("Shared preference set");
		
		NM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		dt.setNM(NM);
		
		try
		{
			String ip = prf.getString("ip", "192.168.1.91");
			{
				dt.setIp(ip);
				System.out.println("IP set at splash is::"+ip);
			}
			String port = prf.getString("port", "1500");
			{
				int iport = Integer.parseInt(port);
				dt.setserverPort(iport);
				System.out.println("port set at splash is::"+port);
			}
			String adport = prf.getString("adport", "4444");
			{
				int iadport = Integer.parseInt(adport);
				dt.setadPort(iadport);
				System.out.println("adport set at splash is::"+adport);
			}
			String vdport = prf.getString("vdport", "8081");
			{
				int ivdport = Integer.parseInt(vdport);
				dt.setvdPort(ivdport);
			}
			String drport = prf.getString("drport","2000");
			{
				int idrport = Integer.parseInt(drport);
				dt.setdrPort(idrport);
			}
			String ping = prf.getString("ping", "50000");
			{
				int iping = Integer.parseInt(ping);
				dt.setPing(iping);
			}
			
			String UserName = prf.getString("username", "user");
			dt.setUsername(UserName);
			String pass = prf.getString("password", "pass123");
			{
				dt.setPassword(pass);
			}
			
			ec = new EagleClient();
			dt.setClientObject(ec);
			ec.start();

		}catch(NumberFormatException e)
		{
			System.out.println("Incorrect preference values");
			openPreference();
		}

		
		Thread splashTimer = new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					System.out.println("Waiting for splash logo finishes");
					sleep(2000);
					System.out.println("directing to home page");
					Intent hm = new Intent(SplashActivity.this, Home.class);
					startActivity(hm);
				}catch(InterruptedException e)
				{
					e.printStackTrace();
				}

			}
		};
		splashTimer.start();
	}

	public void openPreference()
	{
		Intent prfint = new Intent(this,MenuSet.class);
		startActivity(prfint);
	}

	@Override
	public void onBackPressed() 
	{
		doExit();
	}

	private void doExit() 
	{

		AlertDialog.Builder alertDialoge = new AlertDialog.Builder(SplashActivity.this);
		alertDialoge.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				finish();
				NM.cancelAll();
				System.exit(0);
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
		
		alertDialoge.setMessage("Do you Really want to exit?");
		alertDialoge.setTitle("Eagle Eye");
		alertDialoge.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override 
	protected void onDestroy() 
	{
		NM.cancelAll();
	    super.onDestroy();
	}
	
}