package com.example.lab2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab2.network.Game;
import com.example.lab2.network.GbObjectResponse;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_DECK, DatabaseHelper.COLUMN_DESCRIPTION};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<GbObjectResponse> getFavGames(){
        ArrayList<GbObjectResponse> games = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String deck = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DECK));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
                games.add(new GbObjectResponse(id, name, deck, description));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  games;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public GbObjectResponse getFavGame(long id){
        GbObjectResponse game = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            String deck = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DECK));
            String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
            game = new GbObjectResponse(id, name, deck, description);
        }
        cursor.close();
        return  game;
    }

    public long insert(GbObjectResponse user){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, user.getName());
        cv.put(DatabaseHelper.COLUMN_DECK, user.getDeck());
        cv.put(DatabaseHelper.COLUMN_DESCRIPTION, user.getDescription());

        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public long delete(long userId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }
}
