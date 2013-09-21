package com.example.airportstatus.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.airportstatus.GoogleClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TravelTimeEstimate {
	private static String MODE_DRIVING = "driving";
	private static String MODE_TRANSIT = "transit";
	private static String ALTERNATIVE_ROUTES = "false";
	private static String DEVICE_SENSOR = "true";
	
	public static void getDrivingTime(String destination, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("alternatives", ALTERNATIVE_ROUTES);
		params.put("sensor", DEVICE_SENSOR);
		params.put("mode", MODE_DRIVING);
		
		// using location
		// try to get Place location from local storage
		// if not existent, get Place from query
		
		Location origin = new Location(37.76030, -122.41051); // my house
		params.put("origin", origin.toString());
		
		// TODO: improve destination resolution with cached lat/long coords
		params.put("destination", destination);
		
		GoogleClient.getDirections(params, handler);
	}
	
	public static String getTransitTime() {
		return "39";
	}
	
	public static int parseDirections(JSONObject response) {
		/**
		 * Parses the response for the first route.
		 * Adds up the durations of the legs in the route.
		 */
		int minutes = 0;
		try {
			if (response.getString("status").equals("OK")) {
				JSONArray routes = response.getJSONArray("routes");
				if (routes.length() > 0) {
					JSONObject bestRoute = routes.getJSONObject(0);
					JSONArray routeLegs = bestRoute.getJSONArray("legs");
					
					for (int i = 0; i < routeLegs.length(); i++) {
						try {
							long seconds = routeLegs.getJSONObject(i).getJSONObject("duration").getLong("value");
							
							if (seconds % 60 > 0) {
								// Round up seconds to match Google's error rounding
								seconds += 60 - (seconds % 60);
							}
							
							minutes += seconds / 60;
						} catch (JSONException keyError) {
							Log.e("DURATION", "Could not read duration from route leg");
						}
					}
				}
			}
		} catch (JSONException e) {
			Log.e("JSONException", e.getMessage());
		}
		return minutes;
	}
	
	public static String getFormattedDuration(int minutes) {
		int hrs = minutes / 60;
		int mins = minutes % 60;
		if (hrs > 0) {
			return hrs + " hours, " + mins + " mins";
		}
		return mins + " mins";
	}
}
