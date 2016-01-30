package in.prasilabs.eagleeye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.Toast;

public class MenuSet extends PreferenceActivity 
{	
	String ip = null;
	String port = null;
	String adport = null;
	String vdport = null;
	String drport = null;
	String username = null;
	String password = null;

	String ping = null;

	int iport = 0;
	int iadport = 0;
	int ivdport = 0;
	int iping = 0;
	int idrport = 0;

	Data dt = new Data();

	@SuppressWarnings("deprecation")

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		System.out.println("Preference is accesed");
		
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		Preference rfrsh = (Preference) findPreference("rfrsh");
		rfrsh.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) 
			{
				operation();
				//TODO	Restart Client Service
				return false;
			}
		});

		Preference abt = (Preference) findPreference("abt");
		abt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				String url = "http://www.facebook.com/prasilabs";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return false;
			}
		});
	}

	public int operation()
	{

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

		Data dt = new Data();

		try
		{

			ip = sp.getString("ip", "192.168.1.91");
			port = sp.getString("port", "1500");
			if(port != null)
			{
				iport = Integer.parseInt(port);
			}
			adport = sp.getString("adport", "4444");
			if(adport != null)
			{
				iadport = Integer.parseInt(adport);
			}
			vdport = sp.getString("vdport", "8081");
			if(vdport != null)
			{
				ivdport = Integer.parseInt(vdport);
			}
			drport = sp.getString("drport", "2000");
			if(drport != null)
			{
				idrport = Integer.parseInt(drport);
			}

			username = sp.getString("username", "user");
			password = sp.getString("password", "pass123");
			dt.setUsername("admin");
			dt.setPassword(password);
			System.out.println("Username saved is :" +username);
			System.out.println("Username saved is :" +password);

			ping = sp.getString("ping", "50000");
			if(ping != null)
			{
				iping = Integer.parseInt(ping);
			}

			if(ip != null)
			{
				dt.setIp(ip);
			}

			if(port != null)
			{
				dt.setserverPort(iport);
			}
			if(adport != null)
			{
				dt.setadPort(iadport);
			}
			if(vdport != null)
			{
				dt.setvdPort(ivdport);
			}
			if(drport != null)
			{
				dt.setdrPort(idrport);
			}

			if(username != null)
			{
				dt.setUsername(username);
			}

			if(password != null)
			{
				dt.setPassword(password);
			}

			if(ping != null)
			{
				dt.setPing(iping);
			}
			
			//Intent intnt = dt.getIntent();		//Restart IntentService
			//Context cntxt = getBaseContext();
			//cntxt.stopService(intnt);
			//cntxt.startService(intnt);
			
			return 1;

		}catch(NumberFormatException e)
		{
			System.out.println("Number Format Exception occurs");
			errorToast("Incorrect settings");
			return 0;
		}
	}

	@Override
	public void onBackPressed() 
	{
		Context context = getApplication();
		CharSequence text = "Saving settings";
		int duration = Toast.LENGTH_SHORT;
		Toast tst = Toast.makeText(context, text, duration);
		tst.show();
		tst.setGravity(Gravity.BOTTOM|Gravity.LEFT, 10 , 10);

		int op = operation();
		if(op == 1)
		{
			finish();
		}
	}   

	public void errorToast(String error)
	{
		Context context = getApplication();
		CharSequence text = error;
		int duration = Toast.LENGTH_SHORT;
		Toast tst = Toast.makeText(context, text, duration);
		tst.show();
		tst.setGravity(Gravity.BOTTOM|Gravity.LEFT, 10 , 10);
	}
}