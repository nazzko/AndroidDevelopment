package com.example.lab2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gameDB.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "Favorites"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DECK = "deck";
    public static final String COLUMN_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_DECK + " TEXT, " + COLUMN_DESCRIPTION + " TEXT);" );
        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
                + ", " + COLUMN_DECK  + ", " + COLUMN_DESCRIPTION  + ") VALUES ('GTA', 'DECK', 'DESCRIPTION' );");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}