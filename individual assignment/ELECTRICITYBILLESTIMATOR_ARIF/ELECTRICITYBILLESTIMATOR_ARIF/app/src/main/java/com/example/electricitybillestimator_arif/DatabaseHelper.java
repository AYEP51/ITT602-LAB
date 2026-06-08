package com.example.electricitybillestimator_arif;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "electricity_bill.db";
    public static final String TABLE_NAME = "bill_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "month TEXT, " +
                "unit INTEGER, " +
                "totalCharges REAL, " +
                "rebate REAL, " +
                "finalCost REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String month, int unit, double totalCharges, double rebate, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("month", month);
        values.put("unit", unit);
        values.put("totalCharges", totalCharges);
        values.put("rebate", rebate);
        values.put("finalCost", finalCost);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?",
                new String[]{String.valueOf(id)});
    }

    public boolean updateData(int id, String month, int unit, double totalCharges, double rebate, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("month", month);
        values.put("unit", unit);
        values.put("totalCharges", totalCharges);
        values.put("rebate", rebate);
        values.put("finalCost", finalCost);

        int result = db.update(TABLE_NAME, values, "id = ?",
                new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "id = ?",
                new String[]{String.valueOf(id)});
        return result > 0;
    }
}