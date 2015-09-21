package com.tgs.atm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tgs.atm.adapter.LocationsAdapter;
import com.tgs.atm.bean.LocationsBean;
import com.tgs.atm.util.ConnectionDetector;
import com.tgs.atm.util.GPSTracker;
import com.tgs.atm.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView lvLoc;
    public static String strlat;
    public static String strlang;

    private ProgressDialog pDialog;
    GPSTracker gpsTracker;
       // Locations JSONArray
    JSONArray jLocArr = null;
ConnectionDetector cd;
    // ArrayList for Locations ListView
    ArrayList<LocationsBean> locArr=new ArrayList<LocationsBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvLoc=(ListView)findViewById(R.id.lvJPMC);
 
        cd=new ConnectionDetector(getApplicationContext());
      
        gpsTracker=new GPSTracker(MainActivity.this);
        
         // Checking the internet connection
        if(cd.isConnectingToInternet()) {

            new GetLocations().execute();
        }else{
            Utils.showCustomAlert(MainActivity.this,getResources().getString(R.string.connectinternet));
        }

// List item clicked
        lvLoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* Utils.nLat=Double.parseDouble(locArr.get(position).getmLat());
                Utils.nLang=Double.parseDouble(locArr.get(position).getmLong());

*/
                if(!gpsTracker.canGetLocation)
                {
                    gpsTracker.showSettingsAlert();
                    return;
                }
                if(gpsTracker.getLatitude()==0.0)
                {
                    gpsTracker.improveLocation();
                    return;
                }
                // get lat and lang values
                strlat=locArr.get(position).getmLat();
                strlang=locArr.get(position).getmLong();
                Intent mapIntent=new Intent(MainActivity.this, AtmLocationMapActivity.class);
                mapIntent.putExtra("latitude", strlat);//gpsTracker.getLatitude());
                mapIntent.putExtra("longitude", strlang);//gpsTracker.getLongitude());
                startActivity(mapIntent);
            }
        });


    }
    
    // get the locations and places using Async Task
    public class GetLocations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

// Doing bakgrund operations
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Utils.urlLoc, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    jLocArr = jsonObj.getJSONArray(Utils.jArrLoc);

                    // looping through All Locations
                    for (int i = 0; i < jLocArr.length(); i++) {
                        JSONObject c = jLocArr.getJSONObject(i);

                       LocationsBean lbean=new LocationsBean();

                        if(c.getString(Utils.jName)!=null) {
                            lbean.setmName(c.getString(Utils.jName));
                        } if(c.getString(Utils.jAddress)!=null) {
                            lbean.setmAddress(c.getString(Utils.jAddress));
                        }

                        if(c.getString(Utils.jLat)!=null) {
                            lbean.setmLat(c.getString(Utils.jLat));
                        } if(c.getString(Utils.jLong)!=null) {
                            lbean.setmLong(c.getString(Utils.jLong));
                        }
                       
                        locArr.add(lbean);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
           if(locArr.size()!=0){
               lvLoc.setAdapter(new LocationsAdapter(MainActivity.this,locArr));

           }else{
               Toast.makeText(getApplicationContext(),"Locations not found!",Toast.LENGTH_LONG).show();
           }

        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        gpsTracker.getLocation();
        Log.d("Loc data","lat:"+gpsTracker.getLatitude());
    }

}
