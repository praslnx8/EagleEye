package in.prasilabs.eagleeye;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class EagleClient extends Thread
{

	public int ret = 0;
	private String ip;
	private int port;
	private Socket sck;
	private boolean estb = false;
	private DataInputStream is;
	private DataOutputStream os;
	private String msg;

	public String reply;

	Data dt = new Data();

	public EagleClient()
	{
		ip = dt.getIp();
		port = dt.getServerPort();
	}

	public void run()
	{


	}
	public void establish()
	{
		int time_out = 2000;
		try 
		{
			System.out.println("trying to connect");
			sck = new Socket();
			sck.connect(new InetSocketAddress(ip,port),time_out);
			estb = true;
			System.out.println("Established");

		}
		catch (UnknownHostException e) 
		{
			ret =0;
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			ret = 0;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(estb == true)
		{
			try {
				is = new DataInputStream(sck.getInputStream());
				os = new DataOutputStream(sck.getOutputStream());
				System.out.println("Messages are ready to send");
			} catch (IOException e) 
			{
				ret = 0;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void sendInfo(String user,String password)
	{
		establish();

		if(sck.isConnected())
		{
			String drport = Integer.toString(dt.getdrPort());
			try {
				os.writeUTF("logn");
				System.out.println("Sending user info");
				os.writeUTF(dt.getUsername());
				os.writeUTF(dt.getPassword());
				os.writeUTF(drport);
				reply = is.readUTF();
				if(reply.equals("success"))
					dt.setLoginStatus(true);
				else
					dt.setLoginStatus(false);
				System.out.println("ACk recieved");
			} catch (IOException e) {
				ret = 0;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Unable to connect to PI");
		}
	}

	public int sendMessage(String mesg)
	{
		msg = mesg;

		if(sck.isConnected())
		{

			try {

				os.writeUTF(msg);
				System.out.println("Client :" +msg);
				ret = 1;
			} catch (IOException e) {
				ret = -1;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			dt.setLoginStatus(false);
			ret = -1;
		}
		return ret;
	}
}
