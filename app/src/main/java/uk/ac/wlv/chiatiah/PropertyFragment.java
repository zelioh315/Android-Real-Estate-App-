package uk.ac.wlv.chiatiah;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static uk.ac.wlv.chiatiah.Properties.bitmapToString;

public class PropertyFragment extends Fragment {
    private static final String ARG_PROPERTY_ID = "property_id";
    private static final int REQUEST_PHOTO =1;
    private static final int  UPLOAD_IMAGE =2;
    private Properties mProperties;
    private EditText mHeadingField;
    private EditText mDescriptionField;
    private EditText mPostCode;
    private EditText mAddress;
    private EditText mPrice;
    private ImageButton propertyDeleteButton;
    private ImageButton propertyImageCaptureButton;
    private Button mPropertySaveButton;
    private File mPhotoFile;
    private Button mDateButton;
    private Button mUploadImage;

    public static PropertyFragment newInstance(UUID propertyId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROPERTY_ID,propertyId);
        PropertyFragment propertyFragment = new PropertyFragment();
        propertyFragment.setArguments(args);
        return propertyFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID propertyId = (UUID) getArguments().getSerializable(ARG_PROPERTY_ID);
        mProperties = PropertiesLab.get(getActivity()).getProperties(propertyId);
        mPhotoFile = PropertiesLab.get(getActivity()).getPhotoFile(mProperties);
     //   mProperties = new Properties();
    }
    @Override
    public void onPause(){
        super.onPause();
        PropertiesLab.get(getActivity()).updateProperties(mProperties);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_property, container, false);
        mDateButton = (Button) v.findViewById(R.id.date_button);
        String dFormat = "EEE, MMM d, ''yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dFormat);
        String myDate = simpleDateFormat.format(mProperties.getDate());
        mDateButton.setText(myDate);
        mHeadingField = (EditText) v.findViewById(R.id.property_heading);
        mHeadingField.setText(mProperties.getHeading());
        mDescriptionField = (EditText) v.findViewById(R.id.property_description);
        mDescriptionField.setText(mProperties.getDescription());
        mPostCode = (EditText) v.findViewById(R.id.property_postCode);
        mPostCode.setText(mProperties.getPostCode());
        mAddress = (EditText) v.findViewById(R.id.property_address);
        mAddress.setText(mProperties.getAddress());
        mPrice = (EditText) v.findViewById(R.id.property_price);
        mPrice.setText(String.valueOf(mProperties.getPrice()));
        mUploadImage = (Button) v.findViewById(R.id.image_upload_button);
        propertyImageCaptureButton = (ImageButton) v.findViewById(R.id.capture_button);
        PackageManager packageManager = getActivity().getPackageManager();
        final  Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile !=null && captureImage.resolveActivity(packageManager) !=null;
        propertyImageCaptureButton.setEnabled(canTakePhoto);
        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an Image"), UPLOAD_IMAGE);

            }
        });
        propertyImageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "uk.ac.wlv.chiatiah.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
      //  mPropertySaveButton = (Button) v.findViewById(R.id.property_save_button);
        propertyDeleteButton = (ImageButton) v.findViewById(R.id.property_delete_button);
        propertyDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String propertyName = mProperties.getHeading();
                PropertiesLab.get(getActivity()).deleteProperty(mProperties);
                Intent intent = PropertyListActivity.newIntent(getActivity(), propertyName);
//                EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
//                String message = crimeName + "deleted";
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
//        mPropertySaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mProperties.setPrice(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProperties.setAddress(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPostCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProperties.setPostCode(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProperties.setDescription(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mHeadingField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProperties.setHeading(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(), "uk.ac.wlv.chiatiah.fileprovider", mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mProperties.setImage(bitmapToString(bitmap));
        }else if(requestCode==UPLOAD_IMAGE){
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                mProperties.setImage(bitmapToString(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
