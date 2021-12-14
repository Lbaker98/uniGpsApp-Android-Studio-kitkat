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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class allLocals extends AppCompatActivity
{
    private databaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor pointer;
    ListView listView;


    // Getting all locations action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alllocalactionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alllocallayout);

        // allow back button on toolbar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // query database to populate listView
        listView = (ListView) findViewById(R.id.alllocationslist);
        databaseHelper = new databaseHelper(this);
        try
        {
            db = databaseHelper.getReadableDatabase();
            pointer = db.query("Record", new String[]{"_id", "Name", "Notes", "Latitude", "Longitude", "Altitude"}, null, null, null, null, null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, pointer, new String[]{"Name"}, new int[]{android.R.id.text1}, 0);
            listView.setAdapter(listAdapter);

        }
        catch (SQLiteException e)
        {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();

        }

        // making the list views clickable in order to edit and open edit
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> listView, View itemView, int position, long id)
                    {
                        Intent intent = new Intent(getApplicationContext(), editLocation.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                };
        listView.setOnItemClickListener(itemClickListener);
    }

    // enabling menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // send email containing full contents of location database
            case R.id.send:
                Intent sintent = new Intent(Intent.ACTION_SEND);
                sintent.putExtra(Intent.EXTRA_TEXT, databaseHelper.getDatabaseContentsAsString(db));
                sintent.setType("message/rfc822");
                sintent.putExtra(Intent.EXTRA_SUBJECT, ("Location GPS information"));
                sintent.putExtra(Intent.EXTRA_EMAIL, new String[]{"t.kyriacou@keele.ac.uk"});
                startActivity(Intent.createChooser(sintent, "Choose an Email client:"));
                return true;
            // delete all locations
            case R.id.bin:
                AlertDialog.Builder builder = new AlertDialog.Builder(this); //asking the user if they are sure that they want to delete all the records
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        try
                        {
                            // deletes all records
                            databaseHelper.deleteAll(db);
                            pointer = db.query("Record", new String[]{"_id", "Name", "Notes", "Latitude", "Longitude", "Altitude"}, null, null, null, null, null);
                            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, pointer, new String[]{"Name"}, new int[]{android.R.id.text1}, 0);
                            listView.setAdapter(listAdapter);
                            Toast.makeText(getApplicationContext(), "Deletion successful", Toast.LENGTH_LONG).show();
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(getApplicationContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "Deletion cancelled", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setTitle("Are you sure?");//confirming they are sure that they want to delete
                builder.setMessage("Are you sure you want to delete all locations?");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // close cursor and db incase of any problems
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (pointer != null) pointer.close();
        if (db != null) db.close();
    }
}

