package in.prasilabs.eagleeye;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageButton;

public class Data implements Serializable 
{
	protected static final long serialVersionUID = 1112122200L;

	private static String ip = "192.168.1.91";	
	private static int port = 1500;					//Port address of eagle server
	private static int vdport = 3333;				//port address of video streaming server at pi
	private static int adport = 4444;				//port address of voice client at eagle
	private static int drport = 2000;

	private static String StreamUri = "http://"+ip+":"+vdport+"/";

	private static String user = "unknown";	
	private static String pass = "pass123";
	
	private static Calendar cal = Calendar.getInstance();
	
	private static Date time = cal.getTime();
	
	private static Context context;
	
	private static EagleClient ec;
	
	private static boolean loginstatus = false;

	private static int ping = 10;

	private static ExecutorService ex = Executors.newFixedThreadPool(1);

	private static boolean lockstatus = true;
	private static boolean lightstatus = true;
	
	private static SharedPreferences prf;
	
	private static Intent intnt;
	
	private static Socket sck;
	
	private static DataOutputStream os;
	private static DataInputStream is;
	
	private static NotificationManager NM;
	
	private static boolean mic = false;
	private static boolean speaker = false;

	private static ImageButton pingbtn;
	
	Data()
	{
		System.out.println("Data is refered");
		System.out.println(" sample data::Voice address"+adport);
	}

	public void setContext(Context cnt)
	{
		context = cnt;
	}

	public void setClientObject(EagleClient e)
	{
		ec = e;
	}
	
	public void setIp(String host)
	{
		ip = host;
	}
	
	public void setvdPort(int istream) 
	{
		vdport = istream;
	}

	public void setadPort(int ivoice) 
	{
		adport = ivoice;
	}

	public void setserverPort(int iserver) 
	{

		port = iserver;
	}
	
	public void setdrPort(int idrport)
	{
		drport = idrport;
	}

	public void setUsername(String username) 
	{
		user = username;
	}

	public void setPassword(String password) 
	{
		pass = password;
	}

	public void setLoginStatus(boolean i) 
	{
		loginstatus = i;
	}

	public void setLockstatus(boolean b) 
	{
		lockstatus = b;
	}

	public void setVideoStream(String uri)
	{
		StreamUri = "http://"+ip+":"+vdport+"/";
	}

	public void setPing(int png) 
	{
		ping = png;
	}

	public void setSharedPreference(SharedPreferences pref)
	{
		prf = pref;
	}
	
	public void setIntent(Intent intent)
	{
		intnt = intent;
	}
	
	public void setSocket(Socket sock)
	{
		sck = sock;
	}
	
	public void setOS(DataOutputStream ose)
	{
		os = ose;
	}
	public void setIS(DataInputStream ise)
	{
		is = ise;
	}
	
	public void setNM(NotificationManager nm)
	{
		NM = nm;
	}
	
	public void setMic(boolean micst)
	{
		mic = micst;
	}
	
	public void setPingButton(ImageButton pngbtn)
	{
		pingbtn = pngbtn;
	}
	
	public void setSpeaker(boolean spk)
	{
		speaker = spk;
	}
	
	public void setTime(Date date)
	{
		time = date;
	}
	
	public void setLightStatus(boolean lght)
	{
		lightstatus = lght;
	}
	/*---------------------------------------------*/

	public Context getContext() 
	{
		return context;
	}

	public String getIp() 
	{
		return ip;
	}

	public EagleClient getClientObject() 
	{
		return ec;
	}

	public String getUsername() 
	{
		return user;
	}
	
	public int getServerPort()
	{
		return port;
	}

	public String getPassword() 
	{
		return pass;
	}

	public boolean getLoginStatus()
	{
		return loginstatus;
	}

	public int getvdPort()
	{
		return vdport;
	}

	public int getadPort() 
	{
		return adport;
	}
	
	public int getdrPort()
	{
		return drport;
	}

	public boolean getLockStatus()
	{
		return lockstatus;
	}

	public ExecutorService getExecutorObject()
	{
		return ex;
	}

	public String getStreamUri() 
	{
		return StreamUri;
	}

	public int getPing()
	{
		return ping;
	}
	
	public SharedPreferences getSharedPreference()
	{
		return prf;
	}
	
	public Intent getIntent()
	{
		return intnt;
	}
	
	public Socket getSocket()
	{
		return sck;
	}
	
	public DataOutputStream getOS()
	{
		return os;
	}
	public DataInputStream getIS()
	{
		return is;
	}
	
	public NotificationManager getNM()
	{
		return NM;
	}
	
	public boolean getMic()
	{
		return mic;
	}
	
	public boolean getSpeaker()
	{
		return speaker;
	}
	
	public ImageButton getPingButton()
	{
		return pingbtn;
	}
	
	public Date getTime()
	{
		return time;
	}
	
	public boolean getLightStatus()
	{
		return lightstatus;
	}
}