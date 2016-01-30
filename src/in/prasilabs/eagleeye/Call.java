package in.prasilabs.eagleeye;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Call extends Activity 
{
	Ringtone r;
	
	Button acc;
	Button rej;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call);

		ImageView img = (ImageView) findViewById(R.id.callpic);
	
		img.setVisibility(1);
		
		acc = (Button) findViewById(R.id.accept);
		rej = (Button) findViewById(R.id.reject);
		
		Uri sounduri = Uri.parse("android.resource://" +this.getPackageName() + "/" + R.raw.tone);
		r = RingtoneManager.getRingtone(getApplicationContext(), sounduri);
		
		r.play();
		
		acc.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				r.stop();
				 Intent vd = new Intent(Call.this, Cam.class);
				 startActivity(vd);
				 
				 finish();
			}
		});
		rej.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View arg0) 
			{
				r.stop();
				finish();
				
			}
		});
		
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
}
