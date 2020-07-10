package com.project.findme.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class LocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.project.findme.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action)){
                LocationResult result = LocationResult.extractResult(intent);
                if(result!=null){
                  //  Log.i("MyLog","Result is available!");
                    Location location = result.getLastLocation();
                  //  Log.i("MyLog","location = "+location.toString());
                    String latitude = String.valueOf(location.getLatitude());

                    String longitude = String.valueOf(location.getLongitude());
                    try {
                    MapsActivity.getInstance().updateTextViews(latitude,longitude);

                    }catch (Exception e){
                       // Toast.makeText(context,latitude,Toast.LENGTH_LONG).show();
                    }

                }

            }

        }
    }
}

