package uk.ac.wlv.chiatiah.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import uk.ac.wlv.chiatiah.Properties;


public class PropertiesCursorWrapper extends CursorWrapper {
    public PropertiesCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Properties getProperties(){
        String uuidString = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.UUID));
        String heading = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.HEADING));
        String description = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.DESCRIPTION));
        String address = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.ADDRESS));
        String postcode = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.POSTCODE));
        String price = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.PRICE));
        String image = getString(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.IMAGE));
        long date = getLong(getColumnIndex(PropertiesDbSchema.PropertiesTable.Cols.DATE));
        Properties properties = new Properties(UUID.fromString(uuidString));
        properties.setHeading(heading);
        properties.setDescription(description);
        properties.setAddress(address);
        properties.setPostCode(postcode);
        properties.setPrice(price);
        properties.setDate(new Date(date));
        properties.setImage(image);
        return properties;
    }
}
