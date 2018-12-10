package com.example.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.lab2.network.FavGame;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gameDB";
    private static final int SCHEMA = 1;
    private static final String TABLE = "Favorites";
    private static final String COLUMN_ID = "guid";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DECK = "deck";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE = "image";

    private static DatabaseHelper databaseHelper;

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

    private void createDatabase(){
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

    public ArrayList<FavGame> getFavoriteGames(){
        ArrayList<FavGame> games = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE,
                  new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DECK, COLUMN_DESCRIPTION, COLUMN_IMAGE},
                null,null,null,null, null);

        if(cursor.moveToLast()){
            do{
                String guid = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String deck = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DECK));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));
                Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0,blob.length);

                Log.i("description", description);
                games.add(new FavGame(guid, name, deck,description, bmp));
            }
            while (cursor.moveToPrevious());
        }
        cursor.close();
        return games;
    }

    public void saveGame(final FavGame game){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        game.getImage().compress(Bitmap.CompressFormat.PNG, 100,out);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, game.getId());
        values.put(COLUMN_NAME, game.getName());
        values.put(COLUMN_DECK, game.getDeck());
        values.put(COLUMN_DESCRIPTION, game.getDescription());
        values.put(COLUMN_IMAGE, out.toByteArray());
        long idRecord = db.insert(TABLE, null, values);
        Log.i("recordId", String.valueOf(idRecord));
        db.close();
    }

    public void removeGame(String guid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE + " WHERE " + COLUMN_ID + " =? ", new String[]{guid});
        Log.i("Deleted", String.valueOf(guid));
        db.close();
    }

    public boolean checkExist(String guid){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql ="SELECT " + COLUMN_ID + " FROM " + TABLE + " WHERE "+ COLUMN_ID +" = " + guid;
        cursor= db.rawQuery(sql,null);
        Log.i("CHECKED", String.valueOf(cursor.getCount()));

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
}