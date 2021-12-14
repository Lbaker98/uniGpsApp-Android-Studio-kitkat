package com.example.w8a39.gpsmarkerfinal;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class editLocation extends AppCompatActivity
{

    private SQLiteDatabase db;
    private Cursor pointer;
    private databaseHelper databaseHelper;
    long localId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editlocallayout);

        // allow back button on toolbar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Location");

        // set TextView and EditText text to match location info of selected item passed through intent
        localId = (Long) getIntent().getExtras().get("id");
        databaseHelper = new databaseHelper(this);


        try
        {
            db = databaseHelper.getReadableDatabase();
            pointer = db.query("Record", new String[]{"_id", "Name", "Longitude", "Altitude","Latitude", "Notes"}, "_id = ?", new String[]{Long.toString(localId)}, null, null, null);
            pointer.moveToFirst();

            String name = pointer.getString(1);
            EditText editTextName = (EditText) findViewById(R.id.plcname);
            editTextName.setText(name);

            double longitude = pointer.getDouble(2);
            TextView Longitudetv = (TextView) findViewById(R.id.longdis);
            Longitudetv.setText(longitude+"");

            double altitude = pointer.getDouble(3);
            TextView Altitudetv = (TextView) findViewById(R.id.altdis);
            Altitudetv.setText(altitude+"");

            double latitude = pointer.getDouble(4);
            TextView Latitudetv = (TextView) findViewById(R.id.lattidis);
            Latitudetv.setText(latitude+"");

            String notes = pointer.getString(5);
            EditText editnotes = (EditText) findViewById(R.id.plcnotes);
            editnotes.setText(notes);


        } catch(SQLiteException e)
        {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    // setting edit location menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.editlocalactionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // reacting to menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // saving the revised location to database
            case R.id.save2:
                EditText editTextName = (EditText) findViewById(R.id.plcname);
                String name = editTextName.getText().toString();
                if( name.isEmpty() )
                {
                    Toast.makeText(this, "You must specify a location name!", Toast.LENGTH_LONG).show();//validating the names entry
                }
                else
                {
                    EditText editTextNotes = (EditText) findViewById(R.id.plcnotes);
                    String notes = editTextNotes.getText().toString();
                    databaseHelper.updateRecord(db, localId, name, notes);
                    startActivity(new Intent(getApplicationContext(), allLocals.class));
                    Toast.makeText(getApplicationContext(), "Location updated", Toast.LENGTH_LONG).show(); // confirmation to user with feedback
                }
                return true;
            // deleting the selected location
            case R.id.bin2:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        databaseHelper.deleteRecord(db, localId);
                        startActivity(new Intent(getApplicationContext(), allLocals.class));
                        Toast.makeText(getApplicationContext(), "Location deleted", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) //confirming the deletion of the location
                    {
                        Toast.makeText(getApplicationContext(), "Deletion cancelled", Toast.LENGTH_LONG).show();
                    }
                });
                //asking user if they are sure they want to delete the record
                builder.setTitle("Are you sure?");
                builder.setMessage("Are you sure you want to delete this location?");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // close cursor and db in case of any unexpected errors
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(pointer != null) pointer.close();
        if(db != null) db.close();
    }
}
