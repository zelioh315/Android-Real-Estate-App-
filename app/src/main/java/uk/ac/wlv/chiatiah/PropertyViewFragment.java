  package uk.ac.wlv.chiatiah;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PropertyViewFragment extends Fragment {
    private static final String ARG_PROPERTY_ID = "property_id";
    private Properties mProperties;
    private TextView mHeadingField;
    private TextView mDescriptionField;
    private TextView mPostCode;
    private TextView mPrice;
    private Button mShareViaSms;
    private Button mAddressButton;
    private Button mShareViaSocials;
    private Button editButton;
    private File mPhotoFile;
    private ImageView mImageView;

    public static PropertyViewFragment newInstance(UUID propertyId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROPERTY_ID,propertyId);
        PropertyViewFragment propertyViewFragment = new PropertyViewFragment();
        propertyViewFragment.setArguments(args);
        return propertyViewFragment;
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
        View v = inflater.inflate(R.layout.property_view_fragment, container, false);
        mHeadingField = (TextView) v.findViewById(R.id.property_view_heading);
        mHeadingField.setText(mProperties.getHeading());
        mDescriptionField = (TextView) v.findViewById(R.id.property_view_description);
        mDescriptionField.setText(mProperties.getDescription());
       // mPostCode = (EditText) v.findViewById(R.id.property_postCode);
     //   mPostCode.setText(mProperties.getPostCode());
        mImageView = (ImageView) v.findViewById(R.id.property_view_image);
        mImageView.setImageBitmap(mProperties.getImageBitmap());
        mAddressButton = (Button) v.findViewById(R.id.property_view_address);
        mAddressButton.setText(mProperties.getAddress());
        mPrice = (TextView) v.findViewById(R.id.property_view_price);
        mPrice.setText(String.valueOf("£"+mProperties.getPrice()+"PCM"));
        mShareViaSocials = (Button) v.findViewById(R.id.property_share_via_social_media);
        mShareViaSocials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = saveImageToExternalStorage(mProperties.getImageBitmap());
                Uri uri = FileProvider.getUriForFile(getActivity(),"uk.ac.wlv.chiatiah.fileprovider", file);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.putExtra(Intent.EXTRA_SUBJECT,mProperties.getHeading());
                intent.putExtra(Intent.EXTRA_TEXT, mProperties.getDescription());
                intent.putExtra("com.pinterest.EXTRA_DESCRIPTION", mProperties.getHeading()+ " " +mProperties.getDescription());
                //create a chooser for the share/upload  intent
                Intent shareIntent =Intent.createChooser(intent,"Share with");

                PackageManager packageManager = getContext().getPackageManager();

                List<ResolveInfo> activityList = packageManager.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList)
                {
                    if ((app.activityInfo.name).contains("facebook"))
                    {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

                        shareIntent.setComponent(name);
                        break;
                    }else if((app.activityInfo.name).contains("instagram"))
                    {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.setComponent(name);
                        break;
                    }
                    else if((app.activityInfo.name).contains("pinterest"))
                    {

                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.setComponent(name);
                        break;
                    }
                }
                startActivity(shareIntent);
            }


        });
        mShareViaSms = (Button) v.findViewById(R.id.property_share_via_sms);
        mShareViaSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.setType("application/image");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Property for rent");
                emailIntent.putExtra(Intent.EXTRA_TEXT,getSmsReport());
                File file = saveImageToExternalStorage(mProperties.getImageBitmap());
                Uri uri = FileProvider.getUriForFile(getActivity(), "uk.ac.wlv.chiatiah.fileprovider", file);
                emailIntent.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(emailIntent);
            }
        });
        mAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ mProperties.getPostCode());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        editButton = (Button) v.findViewById(R.id.edit_property);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PropertyActivity.newIntent(getContext(), mProperties.getId());
                startActivity(intent);
            }
        });
        updatePhotoView();
        return v;
    }
    private String getSmsReport(){
        String report = getString(R.string.property_report, mProperties.getHeading(), mProperties.getAddress(),mProperties.getPrice(), mProperties.getDescription());
        return report;
    }

    public void updatePhotoView(){
        if(mProperties.getImageAsString()==null){
            mImageView.setImageDrawable(null);
        }else {
            mImageView.setImageBitmap(mProperties.getImageBitmap());
        }
    }

    //The below code was obtained from stackoverflow.
    //https://stackoverflow.com/questions/7887078/android-saving-file-to-external-storage
    // The code is used to saved images to external storage
    //which is then loaded and attached when to either an email or to a social platform.

    private File saveImageToExternalStorage(Bitmap finalBitmap) {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/property_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getActivity(), new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        return file;

    }

}
