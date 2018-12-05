package com.example.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;

import com.example.lab2.R;
import com.example.lab2.gamedetails.GameDetailsActivity;
import com.example.lab2.network.FavGame;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gameDB";
    private static final int SCHEMA = 1;
    static final String TABLE = "Favorites";

    public static final String COLUMN_ID = "guid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DECK = "deck";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public TextView recordId;

    private static DatabaseHelper databaseHelper;
    public DatabaseHelper databaseHelper2;

    public static DatabaseHelper createInstance(Context context){
        if (databaseHelper == null){
            databaseHelper = new DatabaseHelper(context);
            databaseHelper.createDatabase();
        }

        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    protected void createDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + COLUMN_ID
                + " TEXT PRIMARY KEY," + COLUMN_NAME
                + " TEXT, " + COLUMN_DECK + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_IMAGE + " BLOB );");
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

    public ArrayList<FavGame> getFromDb(){
        ArrayList<FavGame> games = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE,
                  new String[]{COLUMN_NAME, COLUMN_DECK, COLUMN_DESCRIPTION, COLUMN_IMAGE},
                null,null,null,null, null);

        if(cursor.moveToFirst()){
            do{
                int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);

                long id = cursor.getLong(0);
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String deck = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DECK));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0,blob.length);

                Log.i("description", description);
                games.add(new FavGame(id, name, deck,description, bmp));

            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return games;
    }

    public void insertValues(String name, String deck, String  description, Bitmap image){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100,out);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DECK, deck);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, out.toByteArray());

        long idRecord = db.insert(TABLE, null, values);

        Log.i("recordId", String.valueOf(idRecord));

        db.close();
    }

    public void deleteValue(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE + " WHERE " + COLUMN_ID + id);
        db.close();
    }

}