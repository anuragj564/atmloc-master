package com.tgs.atm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.internal.mh;

import android.os.Handler;
import android.util.Log;

public class PlacesService {

	private String API_KEY;
	public String status="";
	public String error_Message="";
	public ArrayList<Place> result_Places;

	public PlacesService(String apikey) {
		this.API_KEY = apikey;
		result_Places=new ArrayList<Place>();
	}

	public void setApiKey(String apikey) {
		this.API_KEY = apikey;
	}

// find the places method passing lat and lang
	public String findPlaces(double latitude, double longitude,
			String placeSpacification) {

		String urlString = makeUrl(latitude, longitude, placeSpacification);

		try {
			String json = getJSON(urlString);
			if(json.equalsIgnoreCase(""))
				return status;
			System.out.println(json);
			JSONObject object = new JSONObject(json);
			status=object.getString("status");
			
			if(status.equalsIgnoreCase("OK"))
			{
			JSONArray array = object.getJSONArray("results");

			
			for (int i = 0; i < array.length(); i++) {
				try {
					Place place = Place
							.jsonToPontoReferencia((JSONObject) array.get(i));
					Log.v("Places Services ", "" + place);
					result_Places.add(place);
				} catch (Exception e) {
				}
			 }
			}
			else
				if(status.equalsIgnoreCase("ZERO_RESULTS"))
				{
					error_Message="No results found";
				}
			else
			{
				error_Message=object.getString("error_message");
			}
			
		} catch (JSONException ex) {
			Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return status;
		
	}
	public ArrayList<Place> getPlacesList()
	{
		return result_Places;
	}

	// https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=apikey
	private String makeUrl(double latitude, double longitude, String place) {
		StringBuilder urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/search/json?");
		try
		{
		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=1000");
			 urlString.append("&types=atm"+place);
			//urlString.append("&name="+URLEncoder.encode("Domino's Pizza", "utf-8"));
			urlString.append("&sensor=false&key=" + API_KEY);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=1000");
			urlString.append("&types=atm");// + place);
		//	urlString.append("&name="+URLEncoder.encode("Domino's Pizza", "utf-8"));
			urlString.append("&sensor=false&key=" + API_KEY);
		}
     }
	catch(Exception e)
	{
		Log.d("Exception", "Exception:"+e.getMessage());
	}
		return urlString.toString();
	}

	protected String getJSON(String url) {
		return getUrlContents(url);
	}

	private String getUrlContents(String theUrl) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		}
		catch(UnknownHostException uhe)
		{
			status="ConnectionError";
			error_Message="Unable to connect to server";
		}
		catch(HttpHostConnectException e)
		{
			status="ConnectionError";
			error_Message="Unable to connect to server";
		}
		catch (Exception e) {
			status="Error";
			error_Message="Could not connect to server";
			e.printStackTrace();
		}
		return content.toString();
	}
}
