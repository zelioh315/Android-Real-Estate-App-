package uk.ac.wlv.chiatiah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class PropertyListActivity extends AppCompatActivity {

    public static final String EXTRA_PROPERTY_NAME = "uk.ac.wlv.criminalintent.crime_id";

    public  static Intent newIntent(Context packageContext, String propertyName){
        Intent intent = new Intent(packageContext, PropertyListActivity.class);
        intent.putExtra(EXTRA_PROPERTY_NAME, propertyName);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.properties_list_activity);
        //adding a fragment which will show the list of properties
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment==null){
            fragment=new PropertiesListFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
