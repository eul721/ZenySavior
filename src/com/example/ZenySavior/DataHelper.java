package com.example.ZenySavior;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.*;

/**
 * Created by Jacky on 7/19/14.
 * This class is a helper class that provides DB transaction helper functions
 */
public class DataHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 3;
    private static final String DB_TABLE_NAME = "spendings";
    private static final String DB_TABLE_CREATE =
            "CREATE TABLE " + DB_TABLE_NAME + " (" +
                    "id integer primary key autoincrement not null," +
                    "year integer not null," +
                    "month integer not null," +
                    "date integer not null," +
                    "spentValue real null" +
                    " )";
    //private final int N_COLUMNS = 5; //Number of cols in Table


    public DataHelper(Context context){
        super(context,"ZenySaviorDB",null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DB_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(db);
    }


    //Used to search and determine whether a value already exists for a date
    public double searchValueForDate(final Date inputDate){

        String[] strForms = retrieveSeperateStringsPerDate(inputDate);
        String query = "SELECT spendValue FROM " + DB_TABLE_NAME +
                " WHERE year=? AND month=? AND date=?";
        Cursor cursor = this.getReadableDatabase().rawQuery(query,strForms);
        //SHOULD ONLY RETURN ONE ROW
        assert cursor.getCount()==1;
        cursor.moveToFirst();
        return cursor.getDouble(cursor.getColumnIndex("spentValue"));



    }

    public boolean insertValueForDate(final Date inputDate, final double value) throws Exception{
        //Todo: If already have value for date, update instead
        int[] intForms = retrieveSeperateIntsPerDate(inputDate);
        ContentValues newDay = new ContentValues();
        newDay.put("year",intForms[0]);
        newDay.put("month",intForms[1]+1);
        newDay.put("date",intForms[2]);
        newDay.put("spentValue",value);
        try {
            this.getWritableDatabase().insert(DB_TABLE_NAME, null, newDay);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private String[] retrieveSeperateStringsPerDate(final Date inputDate) {
        return Arrays.toString(retrieveSeperateIntsPerDate(inputDate)).split("[\\[\\]]")[1].split(", ");
    }

    private int[] retrieveSeperateIntsPerDate(final Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        return new int[]{cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE)};
    }

    public ArrayList<DataTableRow> getAllRows(){
        String query = "SELECT * FROM " + DB_TABLE_NAME;
        Cursor cursor = this.getReadableDatabase().rawQuery(query,new String[]{});
        ArrayList<DataTableRow> tableRows = new ArrayList<DataTableRow>();

        cursor.moveToFirst();
        do{
            final int id = cursor.getInt(cursor.getColumnIndex("id"));
            final int year = cursor.getInt(cursor.getColumnIndex("year"));
            final int month = cursor.getInt(cursor.getColumnIndex("month"));
            final int date = cursor.getInt(cursor.getColumnIndex("date"));
            final double spentValue = cursor.getDouble(cursor.getColumnIndex("spentValue"));
            tableRows.add(new DataTableRow(id,year,month,date,spentValue));
        }while(cursor.moveToNext());

        return tableRows;
    }

    public String[] getColumnIndexes(){
        return new String[]{"id","year","month","date","spentValue"};
    }

    public ArrayList<TableRow> getAllRowsInTableRowArrayList(Context context){
        ArrayList<TableRow> listOfTableRows = new ArrayList<TableRow>();

        for (DataTableRow dataTableRow : getAllRows()){ //O(n^2)
            TableRow newTableRow = new TableRow(context);
            HashMap<String,Object> hashmapForRow = dataTableRow.getHashMap();
            for (String index : this.getColumnIndexes()){
                Object val = hashmapForRow.get(index);
                TextView labelForVal = new TextView(context);
                labelForVal.setText(val.toString());
                newTableRow.addView(labelForVal);
            }
            listOfTableRows.add(newTableRow);
        }
        return listOfTableRows;
    }

    //DEBUG ROWS
    public void debugGetAllRows(){
        String query = "SELECT * FROM " + DB_TABLE_NAME;
        Cursor cursor = this.getReadableDatabase().rawQuery(query,new String[]{});
        for (boolean hasNext = cursor.moveToFirst();hasNext;hasNext=cursor.moveToNext()){
            String curID = cursor.getString(cursor.getColumnIndex("id"));
            Log.d("DB Testing - ID: " + curID,"Year: " + cursor.getString(cursor.getColumnIndex("year")));
            Log.d("DB Testing - ID: " + curID,"Month: " + cursor.getString(cursor.getColumnIndex("month")));
            Log.d("DB Testing - ID: " + curID,"Date: " + cursor.getString(cursor.getColumnIndex("date")));
            Log.d("DB Testing - ID: " + curID,"SpentValue: " + cursor.getString(cursor.getColumnIndex("spentValue")));
        }
    }
}
