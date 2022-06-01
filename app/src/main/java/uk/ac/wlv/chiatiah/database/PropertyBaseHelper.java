package uk.ac.wlv.chiatiah.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PropertyBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "propertyBase.db";
    public PropertyBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + PropertiesDbSchema.PropertiesTable.NAME+ "("+
                "_id integer primary key autoincrement, "+
                PropertiesDbSchema.PropertiesTable.Cols.UUID+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.HEADING+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.DESCRIPTION+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.ADDRESS+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.POSTCODE+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.PRICE+ ", "+
                PropertiesDbSchema.PropertiesTable.Cols.DATE+ ","+
                PropertiesDbSchema.PropertiesTable.Cols.IMAGE+
                ")"
        );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
