package in.prasilabs.eagleeye;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Log extends Activity
{

	ListView lv1;
	ListView lv2;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logmenu);
		
		lv1 = (ListView) findViewById(R.id.listView1);

		final Activity act = getParent();
		
		
		String[] menu = new String[] 
		{
			"Last 10 log",
			"All logs (100)",
			"Delete old logs(other than recent 10)",
			"Delete All logs"
	
		};
		
		ArrayAdapter<String> menuadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, menu);
		
		lv1.setAdapter(menuadapter);
		
		lv1.setOnItemClickListener(new OnItemClickListener() 
		{
			DBSync dbs;
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				switch (position) {
				case 0:
					dbs = new DBSync(position, Log.this, act);
					dbs.execute();
					
					break;
				case 1:
					dbs = new DBSync(position, Log.this, act);
					dbs.execute();
					
					break;
				case 2:
					//TODO
					Toast.makeText(getApplicationContext(),"Deleting old logs", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//TODO
					Toast.makeText(getApplicationContext(),"call logs cleared", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(getApplicationContext(),"An error occured", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}
			
			
		});
		
		
	}
	
	
	@Override
	public void onBackPressed() 
	{
		
		finish();
	}
	
	
	class DBSync extends AsyncTask<String, Void, Void>
	{
		int q;
		String[] time = new String[]{"No logs"};
		String[] status = new String[]{"closed"};
		Context cnt;
		Activity act;
		public DBSync(int qu, Log lg, Activity ac)
		{
			q = qu;
			cnt = lg;
			act = ac;
		}
		
		@Override
		protected Void doInBackground(String... params) 
		{	
			String sql;
			if(q == 0)
			{
				sql = "select * from eagle";
			}
			else if (q == 1)
			{
				sql = "select * from eagle";
			}
			else
			{
				sql = "";
			}
		
			
			try 
			{
				
		        Class.forName("com.mysql.jdbc.Driver");
				java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://192.168.1.104:8080/example","prasi","prasi123");
				java.sql.PreparedStatement st = con.prepareStatement(sql);
		        java.sql.ResultSet rs = st.executeQuery();
		        
		        int i = 0;
		        while(rs.next())
		        {
		        	time[i] = rs.getString(1);
		        	status[i] = rs.getString(2);
		        	i++;
		        }
		    } catch (ClassNotFoundException e) {
		        e.printStackTrace();

		    } catch (SQLException e) {
				// TODO: handle exception
			}
				
			return null;
		}
	
		@Override
	    protected void onPostExecute(Void result)
		{
			//setContentView(R.layout.logmenu);

			
			time = new String[]{"1.10","2.20"};
			status = new String[]{"no", "yes"};
			boolean isFirstXml=true;//evaluatingConditionFunction();
			LayoutInflater inflator=getLayoutInflater();
			View view=inflator.inflate(isFirstXml?R.layout.logmenu:R.layout.log, null, false);
			view.startAnimation(AnimationUtils.loadAnimation(cnt, android.R.anim.slide_out_right));
			setContentView(view);

			
			lv2 = (ListView) findViewById(R.id.listView1);

			 
			
			ListViewAdapter lva = new ListViewAdapter(Log.this, time, status);
			lv2.setAdapter(lva);
			lv2.refreshDrawableState();
			
		}		
	}
}
