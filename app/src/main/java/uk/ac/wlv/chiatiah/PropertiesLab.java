package uk.ac.wlv.chiatiah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.ac.wlv.chiatiah.database.PropertiesCursorWrapper;
import uk.ac.wlv.chiatiah.database.PropertiesDbSchema;
import uk.ac.wlv.chiatiah.database.PropertyBaseHelper;

public class PropertiesLab {
    private static  PropertiesLab sPropertiesLab;
    private List<Properties> mProperties;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public File getPhotoFile(Properties properties){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, properties.getPhotoFilename());
    }

    public static PropertiesLab get(Context context){
        if(sPropertiesLab == null){
            sPropertiesLab = new PropertiesLab(context);
        }
        return sPropertiesLab;
    }

    private PropertiesLab(Context context){
        mProperties = new ArrayList<>();
        mContext = context.getApplicationContext();
        mDatabase = new PropertyBaseHelper(mContext).getWritableDatabase();
    }

    public List<Properties> getProperties(){
        List<Properties> properties = new ArrayList<>();
        PropertiesCursorWrapper cursor = queryProperties(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                properties.add(cursor.getProperties());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return properties;
    }

    public void addProperties(Properties properties){
        ContentValues values = getContentValues(properties);
        mDatabase.insert(PropertiesDbSchema.PropertiesTable.NAME, null, values);
        //mProperties.add(properties);
    }

    public void deleteProperty(Properties properties){
        String propertiesId = properties.getId().toString();
        String whereClause= PropertiesDbSchema.PropertiesTable.Cols.UUID + " = ?";
        mDatabase.delete(PropertiesDbSchema.PropertiesTable.NAME,whereClause, new String[] {propertiesId});
    }

    public  Properties getProperties(UUID id){
        PropertiesCursorWrapper cursor = queryProperties(
                PropertiesDbSchema.PropertiesTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProperties();
        }finally {
            cursor.close();
        }
    }
    public void updateProperties(Properties properties){
        String uuidString = properties.getId().toString();
        ContentValues values = getContentValues(properties);
        mDatabase.update(PropertiesDbSchema.PropertiesTable.NAME, values,
                PropertiesDbSchema.PropertiesTable.Cols.UUID + " =?",
                new String[]{uuidString});
    }

    private PropertiesCursorWrapper queryProperties(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                PropertiesDbSchema.PropertiesTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new PropertiesCursorWrapper(cursor);
    }

    private  static ContentValues getContentValues(Properties properties){
        ContentValues values = new ContentValues();
        values.put(PropertiesDbSchema.PropertiesTable.Cols.UUID, properties.getId().toString());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.HEADING, properties.getHeading());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.DESCRIPTION, properties.getDescription());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.ADDRESS, properties.getAddress());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.POSTCODE, properties.getPostCode());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.PRICE, properties.getPrice());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.DATE, properties.getDate().getTime());
        values.put(PropertiesDbSchema.PropertiesTable.Cols.IMAGE, properties.getImageAsString());
        return values;
    }
}
