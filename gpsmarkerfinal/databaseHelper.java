package com.example.w8a39.gpsmarkerfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "location";
    private static final int DB_VERSION = 1;

    public databaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // called in the instance of creation
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createDb(db);
    } //

    // creates the structure for the database and the test data
    public void createDb(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE Record ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "Name TEXT, "
                + "Longitude TEXT, "
                + "Altitude TEXT, "
                + "Latitude TEXT, "
                + "Notes TEXT);"
        );
        insertRecord(db, "James", "i dont know where i am",
                3.3792, 80.250, 426.3984);
        insertRecord(db, "Roadmap", "i will die",
                28.501, 85.390, 888.000);

    }

    // inserting new data into database
    public static long insertRecord(SQLiteDatabase db, String name, String notes, double latitude, double longitude, double altitude)
    {
        ContentValues recordValues = new ContentValues();
        recordValues.put("Name", name);
        recordValues.put("Longitude", longitude);
        recordValues.put("Altitude", altitude);
        recordValues.put("Latitude", latitude);
        recordValues.put("Notes", notes);

        long newRecordID = db.insert("Record", null, recordValues);
        return newRecordID;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }



    // updating database data
    public static void updateRecord(SQLiteDatabase db, Long id, String name, String notes)
    {
        ContentValues recordValues = new ContentValues();
        recordValues.put("Notes", notes);
        recordValues.put("Name", name);
        db.update("Record", recordValues, "_id=?", new String[] {Long.toString(id)});
    }

    // method used to delete data from database
    public static void deleteRecord(SQLiteDatabase db, Long id) {db.delete("Record", "_id=?", new String[] {Long.toString(id)});}

    // method used to delete all records from database
    public static void deleteAll(SQLiteDatabase db) {
        db.delete("Record", null, null);
    }


    public static String getDatabaseContentsAsString(SQLiteDatabase db) //converts all records into a single string for email
    {
        Cursor cursor = db.query("Record", new String[]{"Name","Longitude", "Altitude", "Latitude", "Notes"}, null, null, null, null, null);
        String databaseAsString = System.getProperty("line.separator");
        if(cursor.getCount() != 1)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                for (int i=0; i < cursor.getColumnCount(); i++) {
                    databaseAsString += cursor.getString(i) + System.getProperty("line.separator");
                }
                databaseAsString += System.getProperty("line.separator") + System.getProperty("line.separator");
                cursor.moveToNext();
            }
            if(cursor != null) cursor.close();
        }
        return databaseAsString.trim();
    }
}
