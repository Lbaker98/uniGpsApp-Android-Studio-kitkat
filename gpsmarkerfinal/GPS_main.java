package com.example.w8a39.gpsmarkerfinal;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class GPS_main extends AppCompatActivity
{

    //creating my location listener
    LocationManager locationManager;
    LocationListener locationListener = new MyLocationListener();

    //defining where coordinate valeus will be stored
    String latitude;
    String longitude;
    String altitude;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_main);

        // retrieving location information and displaying message if there is a error
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        }
        catch (SecurityException e)
        {
            System.out.println( e.toString() );
        }
    }
    // Getting main action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mainactionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class MyLocationListener implements LocationListener
    {
        // sets a text view object whenever the location changes
        @Override
        public void onLocationChanged(Location location)
        {
            final TextView textView_Latitude = (TextView) findViewById(R.id.lattidis);
            final TextView textView_Longitude = (TextView) findViewById(R.id.longdis);
            final TextView textView_Altitude = (TextView) findViewById(R.id.altdis);

            latitude = location.getLatitude()+"";
            longitude = location.getLongitude()+"";
            altitude = location.getAltitude()+"";

            textView_Latitude.setText(latitude);
            textView_Longitude.setText(longitude);
            textView_Altitude.setText(altitude);
        }
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    }

    // react to menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // add location info to a new intent before starting new activity to save the location data
            case R.id.currentlocal:
                Intent intent = new Intent(this, newLocation.class);
                intent.putExtra(newLocation.ALTITUDE, altitude);
                intent.putExtra(newLocation.LONGITUDE, longitude);
                intent.putExtra(newLocation.LATITUDE, latitude);
                startActivity(intent);
                return true;

            // start activity to view saved locations
            case R.id.locals:
                startActivity( new Intent(this, allLocals.class) );
                return true;
                //taking user back to same page if error
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // remove location updates when inactive as to save battery and memory
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
