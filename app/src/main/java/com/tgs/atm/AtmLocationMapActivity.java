package com.tgs.atm;


import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tgs.atm.util.Place;
import com.tgs.atm.util.PlacesService;
import com.tgs.atm.util.Utils;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AtmLocationMapActivity extends ActionBarActivity {

	 private GoogleMap map;
	 private final String TAG = getClass().getSimpleName();
 //private GoogleMap map;
 	private String[] places;
 	double latitude,longitude;
 	ArrayList<Place> result_PlacesList;
 	final Handler handler=new Handler(){
 		public void handleMessage(Message msg)
 		{
 			Toast.makeText(AtmLocationMapActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
 		}
 	};
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_places);
		try
		{
		   map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
    		Intent intent=getIntent();
    		latitude=Double.parseDouble(MainActivity.strlat);//intent.getDoubleExtra("latitude", 0.0);
    		longitude=Double.parseDouble(MainActivity.strlang);//intent.getDoubleExtra("longitude", 0.0);
    		final ActionBar actionBar = getSupportActionBar();
    		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.iconbg));
    		map.clear();
			new GetPlaces(AtmLocationMapActivity.this, "restaurant").execute();
		}
		catch(Exception e)
		{
			Log.d("Exception", "Exception:"+e.getMessage());
		}
    }	

    	private class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> {

    		private ProgressDialog dialog;
    		private Context context;
    		private String places;

    		public GetPlaces(Context context, String places) {
    			this.context = context;
    			this.places = places;
    		}

    		@Override
    		protected void onPostExecute(ArrayList<Place> result) {
    			super.onPostExecute(result);
    			try
    			{
    			if (dialog.isShowing()) {
    				dialog.dismiss();
    			}
    			result_PlacesList=result;
    			
    			for (int i = 0; i < result.size(); i++) {
    				map.addMarker(new MarkerOptions()
    						.title(result.get(i).getName())
    						.position(
    								new LatLng(result.get(i).getLatitude(), result
    										.get(i).getLongitude()))
    						.icon(BitmapDescriptorFactory
    								.fromResource(R.drawable.pin))
    						.snippet(result.get(i).getVicinity()));
    			}
    			double lat;
    			double longi;
    			if(result.size()>0)
    			{
    			 lat=result.get(0).getLatitude();
    			 longi=result.get(0).getLongitude();
    			} else if(latitude!=0.0)
    			{
    				lat=latitude;
    				longi=longitude;
    			}
    			else
    			{
    				lat=Double.parseDouble(MainActivity.strlat);//17.3700;
    				longi=Double.parseDouble(MainActivity.strlang);//78.4800;
    			}
    			CameraPosition cameraPosition = new CameraPosition.Builder()
    					.target(new LatLng(lat, longi)) // Sets the center of the map to
    											// Mountain View
    					.zoom(13) // Sets the zoom
    					.tilt(30) // Sets the tilt of the camera to 30 degrees
    					.build(); // Creates a CameraPosition from the builder
    			map.animateCamera(CameraUpdateFactory
    					.newCameraPosition(cameraPosition));
    			}
    			catch(Exception e)
    			{
    				Log.d("Exception", "Exception:"+e.getMessage());
    			}
    		}

    		@Override
    		protected void onPreExecute() {
    			super.onPreExecute();
    			dialog = new ProgressDialog(context);
    			dialog.setCancelable(false);
    			dialog.setMessage("Loading..");
    			dialog.isIndeterminate();
    			dialog.show();
    		}

    		@Override
    		protected ArrayList<Place> doInBackground(Void... arg0) {
    			ArrayList<Place> findPlaces =new ArrayList<Place>();
    			PlacesService service = new PlacesService(Utils.strBrowserKey);
    			try
    			{
    			
    			String status=service.findPlaces(latitude, longitude, places); // 77.218276
    			if(status.equalsIgnoreCase("OK"))
    			{
    			findPlaces=service.result_Places;	
    			for (int i = 0; i < findPlaces.size(); i++) {

    				Place placeDetail = findPlaces.get(i);
    				
    				Log.e(TAG, "places : " + placeDetail.getName());
    			  }
    			}
    			else
    			{
    				Message m=new Message();
    				m.obj=service.error_Message;
    				handler.sendMessage(m);
    			}
    			}
    			catch(Exception e)
    			{
    				
    			}
    			return findPlaces;
    			
    			
    		}

    	}

    	
    	
   	
}
