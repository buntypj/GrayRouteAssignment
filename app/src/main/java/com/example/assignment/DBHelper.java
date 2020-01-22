package com.example.assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "assignment.db";
    public static final String CONTACTS_TABLE_NAME = "addressData";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_ADDRESS = "address";
    public static final String CONTACTS_COLUMN_LAT = "lat";
    public static final String CONTACTS_COLUMN_LONGITUDE = "lang";
    public static final String CONTACTS_COLUMN_IMAGE = "imgData";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table IF NOT EXISTS addressData " + "(id integer primary key autoincrement, address text,lat text,lang text, imgData BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS addressData");
        onCreate(db);
    }

    public boolean insertData (String address, String latitude, String longitude, byte[] imgByte) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("address", address);
            contentValues.put("lat", latitude);
            contentValues.put("lang", longitude);
            contentValues.put("imgData", imgByte);
            db.insert("addressData", null, contentValues);
            db.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }



    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from addressData where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    /*public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }*/

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("addressData",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<DataModel> getAlldata() {
        ArrayList<DataModel> array_list = new ArrayList<DataModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from addressData", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            DataModel dataModel = new DataModel();
            dataModel.setAddress(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ADDRESS)));
            dataModel.setLat(res.getString(res.getColumnIndex(CONTACTS_COLUMN_LAT)));
            dataModel.setLang(res.getString(res.getColumnIndex(CONTACTS_COLUMN_LONGITUDE)));
            dataModel.setImage(res.getBlob(res.getColumnIndex(CONTACTS_COLUMN_IMAGE)));
            array_list.add(dataModel);
            res.moveToNext();
        }
        return array_list;
    }
}
