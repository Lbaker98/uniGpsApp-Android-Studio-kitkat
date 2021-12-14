package com.example.w8a39.gpsmarkerfinal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class newLocation extends AppCompatActivity
{

    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ALTITUDE = "altitude";

    private SQLiteDatabase db;
    private databaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlocallayout);

        ActionBar actionBar = getSupportActionBar();// addingback button on toolbar
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Location");//setting new location as app bar title


        // TextView text to match location information by passing it as an intent
        Intent intent = getIntent();
        TextView textViewLatitude = (TextView) findViewById(R.id.lattidis);
        textViewLatitude.setText( intent.getStringExtra(LATITUDE) );
        TextView textViewAltitude = (TextView) findViewById(R.id.altdis);
        textViewAltitude.setText( intent.getStringExtra(ALTITUDE) );
        TextView textViewLongitude = (TextView) findViewById(R.id.longdis);
        textViewLongitude.setText( intent.getStringExtra(LONGITUDE) );

    }

    // setting new location action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.newlocationactionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // reacting to menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // saving location to database
            case R.id.save:
                EditText editTextName = (EditText) findViewById(R.id.plcname);
                String name = editTextName.getText().toString();
                if( name.isEmpty() )
                {
                    Toast.makeText(this, "You must specify a location name!", Toast.LENGTH_LONG).show();
                } else
                    {
                    EditText editnotes = (EditText) findViewById(R.id.plcnotes);
                    String notes = editnotes.getText().toString();
                    Intent intent = getIntent();
                    double latitude = Double.parseDouble(intent.getStringExtra(LATITUDE));
                    double altitude = Double.parseDouble(intent.getStringExtra(ALTITUDE));
                    double longitude = Double.parseDouble(intent.getStringExtra(LONGITUDE));

                    databaseHelper = new databaseHelper(this);
                    db = databaseHelper.getReadableDatabase();

                    databaseHelper.insertRecord(db, name, notes, latitude, longitude, altitude);
                    startActivity(new Intent(this, GPS_main.class));
                    Toast.makeText(getApplicationContext(), "Saved new location", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // close db if unexpected problems arise
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(db != null) db.close();
    }
}