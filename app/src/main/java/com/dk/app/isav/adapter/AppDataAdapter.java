/**
 * 
 */
package com.dk.app.isav.adapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.dk.app.isav.AppInfoData;
import com.dk.rhythm.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Vasundhra
 *
 */
public class AppDataAdapter extends ArrayAdapter<AppInfoData> {
	private ArrayList<AppInfoData> infoData = new ArrayList<AppInfoData>();
	private Context contextApp=null;
	private static LayoutInflater inflater=null;
	AppInfoData tempValues=null;
	private PackageManager pm;
	public Resources res;
	public AppDataAdapter(Context context, int textViewResourceId,ArrayList<AppInfoData> arr) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
		this.contextApp=context;
		this.infoData=arr;
		inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pm = contextApp.getPackageManager();
	}
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;
		Log.i("Adapter:","1");
		if(convertView==null){

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			vi = inflater.inflate(R.layout.applist, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/
			Log.i("Adapter:","2");
			holder = new ViewHolder();
			holder.text = (TextView) vi.findViewById(R.id.text2);
			holder.text1=(TextView)vi.findViewById(R.id.text1);
			holder.image=(ImageView)vi.findViewById(R.id.imageView1);

			/************  Set holder with LayoutInflater ************/
			vi.setTag( holder );
		}
		else 
			holder=(ViewHolder)vi.getTag();

		if(infoData.size()<=0)
		{
			holder.text.setText("No Data");

		}
		else
		{
			/***** Get each Model object from Arraylist ********/
			tempValues=null;
			tempValues = ( AppInfoData ) infoData.get( position );

			/************  Set Model values in Holder elements ***********/

			holder.text.setText( tempValues.appName );
			if(tempValues.endDateTime!=null){
				holder.text1.setText( convert( tempValues.timeSpendThisTime));
			}else{
				holder.text1.setText("In Use");
			}
			
			try {
				if(!tempValues.appName.equals("Phone Power Off")){
				Drawable dw=pm.getApplicationIcon(tempValues.appName);
				Log.i("Image Size of "+tempValues.appName+": ",dw.getMinimumHeight()+" Width"+dw.getMinimumWidth());
				holder.image.setBackground(new ScaleDrawable(dw ,01,75,75).getDrawable());
				}else{
					holder.image.setBackground(contextApp.getResources().getDrawable(R.drawable.smiley));
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			/******** Set Item Click Listner for LayoutInflater for each row *******/

			// vi.setOnClickListener(new OnItemClickListener( position ));
		}
		return vi;
	}


	public  String  convert(long miliSeconds){
		int seconds = (int) (miliSeconds / 1000) % 60 ;
		int minutes = (int) ((miliSeconds / (1000*60)) % 60);
		int hours   = (int) ((miliSeconds / (1000*60*60)) % 24);
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public int getCount() {

		if(infoData.size()<=0)
			return 1;
		return infoData.size();
	}

	public AppInfoData getItem(int position) {
		return infoData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	//    @Override
	//      public void onClick(View v) {
	//              Log.v("CustomAdapter", "=====Row button clicked=====");
	//      }
	//       
	//      /********* Called when Item click in ListView ************/
	//      private class OnItemClickListener  implements OnClickListener{           
	//          private int mPosition;
	//           
	//          OnItemClickListener(int position){
	//               mPosition = position;
	//          }
	//           
	//          @Override
	//          public void onClick(View arg0) {
	//
	//     
	//            CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;
	//
	//           /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
	//
	//              sct.onItemClick(mPosition);
	//          }               
	//      } 
	public static class ViewHolder{

		public TextView text;
		public TextView text1;
		public TextView textWide;
		public ImageView image;

	}
}
