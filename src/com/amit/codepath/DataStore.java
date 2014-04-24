package com.amit.codepath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataStore {
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public final static String TABLE="TodoItems"; // name of table 
	public final static String ID="_id"; // id 
	public final static String TODO="name";  // task
	
	public DataStore(Context context){  
	    dbHelper = new DatabaseHelper(context);  
	    database = dbHelper.getWritableDatabase();  
	}


	public long createRecords(int id, String name){  
	   ContentValues values = new ContentValues();  
	   values.put(ID, id);  
	   values.put(TODO, name);  
	   return database.insert(TABLE, null, values);  
	}    

	public Cursor selectRecords() {
	   String[] cols = new String[] {ID, TODO};  
	   Cursor mCursor = database.query(true, TABLE,cols,null  
	            , null, null, null, null, null);  
	   if (mCursor != null) {  
	     mCursor.moveToFirst();  
	   }  
	   return mCursor; // iterate to get each value.
	}
}

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CodePath";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table TodoItems " +
    		"( _id text primary key, name text not null, done integer," +
    		"add_time real);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS TodoItems");
        onCreate(database);
    }
}
