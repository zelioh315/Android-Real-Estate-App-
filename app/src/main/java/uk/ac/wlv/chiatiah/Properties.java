package uk.ac.wlv.chiatiah;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.UUID;

public class Properties {
    private UUID mId;
    private String mHeading;
    private String mDescription;
    private String mPostCode;
    private String mAddress;
    private String mPrice;
    private Date mDate;
    private String image;
    private boolean mSelected;


    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }

    public Properties(){
        this(UUID.randomUUID());
    }

    public  Properties(UUID id){
        mId = id;
        mDate = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getHeading() {
        return mHeading;
    }

    public void setHeading(String heading) {
        mHeading = heading;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPostCode() {
        return mPostCode;
    }

    public void setPostCode(String postCode) {
        mPostCode = postCode;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }
    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getImageAsString() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public Bitmap getImageBitmap() {
        return stringToBitmap(this.image);
    }

    //method for converting bitmaps to strings
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputS);
        byte[] b = byteArrayOutputS.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    //method for converting strings to bitmaps
    private static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


}
