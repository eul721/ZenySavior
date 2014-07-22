package com.example.ZenySavior;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.*;

/**
 * TODO: CHANGE ALL HARDCODED MONTH OFFSETS TO ARBITRARY IDENTIFIER COMPARISON
 */

/**
 * Created by Jacky on 7/19/14.
 * This class is a helper class that provides DB transaction helper functions
 * Also provides date computations
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

    private static String KEY_ID = "id";
    private static String KEY_YEAR = "year";
    private static String KEY_MONTH= "month";
    private static String KEY_DATE = "date";
    private static String KEY_SPENT_VALUE = "spentValue";





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



    private Cursor retrieveCursorForSearches(final Date inputDate){
        String[] strForms = retrieveSeperateStringsPerDate(inputDate);
        String query = "SELECT * FROM " + DB_TABLE_NAME +
                " WHERE year=? AND month=? AND date=?";


        return this.getReadableDatabase().rawQuery(query,strForms);
    }

    private Cursor retrieveCursorForCurMonthSearches(final int year, final int month){
        String query = "SELECT * FROM " + DB_TABLE_NAME +
                " WHERE year=? AND month=?";

        return this.getReadableDatabase().rawQuery(query,new String[]{String.valueOf(year),String.valueOf(month)});
    }

    //Used to search and determine whether a value already exists for a date
    private boolean searchIfValueExistsForDate(final Date inputDate){

        return retrieveCursorForSearches(inputDate).getCount()>0;
    }

    private DataTableRow searchRowForDate(final Date inputDate){
        Cursor searchCursor = retrieveCursorForSearches(inputDate);
        assert searchCursor.getCount() == 1;
        searchCursor.moveToFirst();

        return new DataTableRow( //construct a tablerow obj from the search
                searchCursor.getInt(searchCursor.getColumnIndex(KEY_ID)),
                searchCursor.getInt(searchCursor.getColumnIndex(KEY_YEAR)),
                searchCursor.getInt(searchCursor.getColumnIndex(KEY_MONTH)),
                searchCursor.getInt(searchCursor.getColumnIndex(KEY_DATE)),
                searchCursor.getDouble(searchCursor.getColumnIndex(KEY_SPENT_VALUE)));
    }

    public double searchValueForDate(final Date inputDate){
        double val;
        if(searchIfValueExistsForDate(inputDate)){
            DataTableRow dateRow = searchRowForDate(inputDate);
            val = dateRow.getspentValue();
        }else{
            val = 0.0;
        }
        return BigDecimal.valueOf(val).setScale(2, BigDecimal.ROUND_UP).doubleValue();

    }

    public double getSumForMonth(final Date inputDate){
        Calendar calInstance = Calendar.getInstance();
        calInstance.setTime(inputDate);
        final int year = calInstance.get(Calendar.YEAR);
        final int month = calInstance.get(Calendar.MONTH)+1;
        Cursor resultCursor = retrieveCursorForCurMonthSearches(year,month);
        double sum = 0.00;
        if(resultCursor.getCount()>0){
            resultCursor.moveToFirst();
            do{
                sum += resultCursor.getDouble(resultCursor.getColumnIndex(KEY_SPENT_VALUE));
            }while(resultCursor.moveToNext());
        }else{
            return 0.00;
        }

        return sum;

    }


    /**
     * This method updates a certain date with the provided value
     * @param inputDate - the provided date
     * @param value - the provided value. Can be negative
     * @return sum of the values
     */
    private double updateValueForDate(final Date inputDate, final double value){
        DataTableRow rowForDate = searchRowForDate(inputDate);
        double sum = rowForDate.getspentValue() + value;
        ContentValues values = new ContentValues();
        values.put("spentValue",sum);
        this.getWritableDatabase().update(DB_TABLE_NAME,values,KEY_ID + "=?",new String[]{String.valueOf(rowForDate.getId())});
        return sum;
    }

    public double insertValueForDate(final Date inputDate, final double value) throws Exception{
        //Todo: If already have value for date, update instead
        if(searchIfValueExistsForDate(inputDate)){
            try {
                return updateValueForDate(inputDate, value);
            }
            catch(Exception e){
                e.printStackTrace();
                throw e;
            }
        }
        else {
            int[] intForms = retrieveSeperateIntsPerDate(inputDate);
            ContentValues newDay = new ContentValues();
            newDay.put("year", intForms[0]);
            newDay.put("month", intForms[1]);
            newDay.put("date", intForms[2]);
            newDay.put("spentValue", value);
            try {
                this.getWritableDatabase().insert(DB_TABLE_NAME, null, newDay);
                return value;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private String[] retrieveSeperateStringsPerDate(final Date inputDate) {
        return Arrays.toString(retrieveSeperateIntsPerDate(inputDate)).split("[\\[\\]]")[1].split(", ");
    }

    private int[] retrieveSeperateIntsPerDate(final Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        return new int[]{cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE)};
    }

    public ArrayList<DataTableRow> getAllRows(){
        String query = "SELECT * FROM " + DB_TABLE_NAME;
        Cursor cursor = this.getReadableDatabase().rawQuery(query,new String[]{});
        ArrayList<DataTableRow> tableRows = new ArrayList<DataTableRow>();

        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            do {
                final int id = cursor.getInt(cursor.getColumnIndex("id"));
                final int year = cursor.getInt(cursor.getColumnIndex("year"));
                final int month = cursor.getInt(cursor.getColumnIndex("month"));
                final int date = cursor.getInt(cursor.getColumnIndex("date"));
                final double spentValue = cursor.getDouble(cursor.getColumnIndex("spentValue"));
                tableRows.add(new DataTableRow(id, year, month, date, spentValue));
            } while (cursor.moveToNext());
        }
        return tableRows;
    }

    public String[] getColumnIndexes(){
        return new String[]{KEY_ID,KEY_YEAR,KEY_MONTH,KEY_DATE,KEY_SPENT_VALUE};
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
