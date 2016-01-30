package in.prasilabs.eagleeye;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter
{
	Activity context;
	String time[];
	String status[];

	public ListViewAdapter(Activity context, String[] title, String[] description) {
		super();
		this.context = context;
		this.time = title;
		this.status = description;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return time.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {
        TextView txtViewTime;
        TextView txtViewstatus;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater =  context.getLayoutInflater();

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.logrow, null);
			holder = new ViewHolder();
			holder.txtViewTime = (TextView) convertView.findViewById(R.id.time);
			holder.txtViewstatus = (TextView) convertView.findViewById(R.id.status);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtViewTime.setText(time[position]);
		holder.txtViewstatus.setText(status[position]);

	return convertView;
	}

}
