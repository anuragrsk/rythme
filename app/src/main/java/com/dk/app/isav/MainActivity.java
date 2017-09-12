package com.dk.app.isav;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dk.app.isav.dto.Result;
import com.dk.app.isav.restservice.ProcessResponse;
import com.dk.app.isav.restservice.RestServiceCall;
import com.dk.app.isav.restservice.RestServiceCall.RequestMethod;
import com.dk.app.isav.service.GetDataAsync;
import com.dk.app.isav.service.TimerService;
import com.dk.app.isav.util.DateUtil;
import com.dk.rhythm.R;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import dto.AppDataArray;
import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private final static int
    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    //private Location mLocationClient;
    private Location location;
    private LocationRequest mLocationRequest;
    private boolean mUpdatesRequested;
    private SharedPreferences mPrefs;
    private Editor mEditor;
    


    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HERE","is one");
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        Log.d("HERE","is one1");
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
      
      
        	  Log.d("HERE","is one2");
//        	
//        	  mPrefs = getSharedPreferences("SharedPreferences",
//                      Context.MODE_PRIVATE);
//              // Get a SharedPreferences editor
//              mEditor = mPrefs.edit();
//
//          	 mLocationRequest = LocationRequest.create();
//          	  Log.d("HERE","is one5");
//               // Use high accuracy
//               mLocationRequest.setPriority(
//                       LocationRequest.PRIORITY_HIGH_ACCURACY);
//               // Set the update interval to 5 seconds
//               mLocationRequest.setInterval(UPDATE_INTERVAL);
//               // Set the fastest update interval to 1 second
//               mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//
//        mLocationClient = new LocationClient(this, this, this);
        Log.d("HERE","is one3");
      //  mLocationClient.connect();
        Log.d("HERE","is one4");
        //location=  mLocationClient.getLastLocation();
                Log.d("HERE","is one6");
                startService(new Intent(this, TimerService.class));
                
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
//        mLocationClient.connect();
        
    }
   
    /*
     * Called when the Activity is no longer visible.
     */
   
    @Override
    protected void onPause() {
//        // Save the current setting for updates
//        mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
//        mEditor.commit();
        super.onPause();
    }
    @Override
    protected void onResume() {
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
    	super.onResume();
//        if (mPrefs.contains("KEY_UPDATES_ON")) {
//            mUpdatesRequested =
//                    mPrefs.getBoolean("KEY_UPDATES_ON", false);
//
//        // Otherwise, turn off location updates
//        } else {
//            mEditor.putBoolean("KEY_UPDATES_ON", false);
//            mEditor.commit();
//        }
    }
    @Override
    protected void onStop() {
        // If the client is connected
//        if (mLocationClient.isConnected()) {
//            /*
//             * Remove location updates for a listener.
//             * The current Activity is the listener, so
//             * the argument is "this".
//             */
//            removeLocationUpdates(this);
//        }
//        /*
//         * After disconnect() is called, the client is
//         * considered "dead".
//         */
//        mLocationClient.disconnect();
        super.onStop();
    }


    private void removeLocationUpdates(MainActivity mainActivity) {
		// TODO Auto-generated method stub
    	//mLocationClient.removeLocationUpdates( this);
	}


	/**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Button getLocation,activity;
        private EditText editText;
        private SharedPreferences mPrefs;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            getLocation=(Button)rootView.findViewById(R.id.getLocation);
            activity=(Button)rootView.findViewById(R.id.getActivity);
            editText = (EditText)rootView.findViewById(R.id.editText1);
            mPrefs=rootView.getContext().getSharedPreferences("SharedPreferences",
                    Context.MODE_PRIVATE);
            activity.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Date todayDate = new Date();
					String date=SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,Locale.US).format(todayDate);
					String dateOnly=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.US).format(todayDate);
					AppInfoData hss=(AppInfoData) AppData.getAppInstance().getDataForDay().get(dateOnly+"_"+AppData.APP_APPLICATION_DATA);
					Long timeSpendtillNow=(Long) AppData.getAppInstance().getDataForDay().get(dateOnly+"_"+AppData.APP_APPLICATION_TIME_SPEND);
					if(hss!=null){
   					Log.e("Here is list:"," timeSpendtillNow:"+((timeSpendtillNow!=null)?timeSpendtillNow.intValue()/1000:"0")+hss.startDateTime+": "+hss.endDateTime+" App Name:"+hss.appName+" Time Spend"+hss.timeSpendThisTime+"\n"+((hss.pastApp!=null)?hss.pastApp.toString():""));
   					}else{
   						editText.setText("No location data");
   					}
					GetDataAsync async = new GetDataAsync(activity.getContext(),"getDataFromResult");
    				ArrayList<NameValuePair> params= new ArrayList<NameValuePair>();
    				NameValuePair userName = new BasicNameValuePair("userName", "anuragdehra@yahoo.com");
    				params.add(userName);
    				async.execute(params);
					Intent i =new Intent(v.getContext(), AppList.class);
					v.getContext().startActivity(i);
				}});
            getLocation.setOnClickListener(new OnClickListener(){
            	

    			@Override
    			public void onClick(View v) {
    				String message="";
    				if (mPrefs.contains("Location_data")) {
    					Date todayDate = new Date();
    					String date=SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,Locale.US).format(todayDate);
    					String dateOnly=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.US).format(todayDate);
    					LocationData hss=(LocationData) AppData.getAppInstance().getDataForDay().get(dateOnly+"_"+AppData.APP_LOCATION_DATA);
    					if(hss!=null && hss.location!=null){
    					 editText.setText(hss.startDateTime+": "+hss.endDateTime+" :"+hss.location.getLatitude()+hss.location.getLongitude()+" Distance:"+hss.distance+"\n"+((hss.pastLocation!=null)?hss.pastLocation.toString():""+" Distance:"+hss.distance));
    					}else{
    						editText.setText("No location data");
    					}
    				}else{
    					editText.setText("No Data");
    				}
    				
    			}

				});
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
        public static void getDataFromResult(Result result) {
			// TODO Auto-generated method stub
			
		}
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
           
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                   
                    break;
                }
        
        }
     }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = resultCode;
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    errorCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
        }
        return false;
    }

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onConnected(Bundle arg0) {
//		// TODO Auto-generated method stub
//		Toast.makeText(this, "Connected.",
//                Toast.LENGTH_SHORT).show();
//
//		if (mUpdatesRequested) {
//            mLocationClient.requestLocationUpdates(mLocationRequest,  this);
//        }
//		location=  mLocationClient.getLastLocation();
//		String msg = 
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//		if (mPrefs.contains("Location_data")) {
//			msg=new java.util.Date().toLocaleString()+":"+msg+"\n"+mPrefs.getString("Location_data", "");
//			mEditor.putString("Location_data", msg);
//		}else{
//			mEditor.putString("Location_data", new java.util.Date().toLocaleString()+":"+msg);
//		}
//	        mEditor.commit();
//	        processLocationData();
//        
//        Toast.makeText(this, "Updated Location: " +msg, Toast.LENGTH_SHORT).show();

	}

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void processLocationData() {
		AppData appData=AppData.getAppInstance();
		Date todayDate = new Date();
		String date=SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG,Locale.US).format(todayDate);
		String dateOnly=SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,Locale.US).format(todayDate);
		storeDate(dateOnly);
		@SuppressWarnings("unchecked")
		LocationData hss=(LocationData) appData.getDataForDay().get(dateOnly+"_"+AppData.APP_LOCATION_DATA);
		Location loc = new Location(location);
			if(hss!=null ){
				LocationData location=hss;
				float distance[] = new float[2];
				Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), hss.location.getLatitude(), hss.location.getLongitude(), distance);
				if(distance.length>0)
					Log.d("Distance",distance[0]+"");
				Log.d("Distance",loc.distanceTo(hss.location)+"");
				if(loc.distanceTo(hss.location) <100){
					Log.d("Main","Past Location Equal:"+location.toString()+" Date: "+date);
					Log.d("Main","New Location Equal:"+loc.toString()+" Date: "+date);
				}else{
					Log.d("Main","Past Location: Change:"+location.toString()+" Date: "+date);
					Log.d("Main","New Location: Change:"+loc.toString()+" Date: "+date);
					location.endDateTime=todayDate;
					LocationData obj = new  LocationData(loc,todayDate, null,null, false, null,location,String.valueOf(loc.distanceTo(hss.location))) ;
					appData.getDataForDay().put(dateOnly+"_"+AppData.APP_LOCATION_DATA, obj);
				}


			}else{
				ArrayList<LocationData> arrNew =new ArrayList<LocationData>();
				LocationData obj = new  LocationData(loc,todayDate, null,null, false, null,null,"0") ;

				appData.getDataForDay().put(dateOnly+"_"+AppData.APP_LOCATION_DATA, obj);
			}
		


		if(appData.getDataForDay()!=null && !appData.getDataForDay().isEmpty())
			Log.i("getLocationMap:",appData.getDataForDay().toString());


	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void storeDate(String dateOnly) {
		
//		AppData appData=AppData.getAppInstance();
//		HashMap dataForDay= appData.getDataForDay();
//		if(dataForDay!=null){
//			HashMap<String, ArrayList<LocationData>> dataForLocation= (HashMap<String, ArrayList<LocationData>>) dataForDay.get(dateOnly+"_"+AppData.APP_LOCATION_DATA);
//			if(dataForLocation!=null && !dataForLocation.isEmpty()){
//				//doNthing
//			}else{
//				HashMap<String, ArrayList<LocationData>> locationMap = new HashMap<String, ArrayList<LocationData>>();
//				dataForDay.put(dateOnly+"_"+AppData.APP_LOCATION_DATA,locationMap);
//			}
//		}
	}


	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Disconnected.",
                Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLocationChanged(Location arg0) {
//		// TODO Auto-generated method stub
//		location=  mLocationClient.getLastLocation();
//		String msg = 
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        Double.toString(location.getLongitude());
//		if (mPrefs.contains("Location_data")) {
//			msg=new java.util.Date().toLocaleString()+":"+msg+"\n"+mPrefs.getString("Location_data", "");
//			mEditor.putString("Location_data", msg);
//		}else{
//			mEditor.putString("Location_data", new java.util.Date().toLocaleString()+":"+msg);
//		}
//	        mEditor.commit();
//		
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


}
