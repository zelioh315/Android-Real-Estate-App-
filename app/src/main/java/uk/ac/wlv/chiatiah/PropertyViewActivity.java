package uk.ac.wlv.chiatiah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.UUID;

public class PropertyViewActivity extends AppCompatActivity {
    public static final String EXTRA_PROPERTY_ID="uk.ac.wlv.chiatiah.property_id";

    public  static Intent newIntent(Context packageContext, UUID propertyId){
        Intent intent = new Intent(packageContext, PropertyActivity.class);
        intent.putExtra(EXTRA_PROPERTY_ID, propertyId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);
        //adding fragment to the PropertyActivity
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            UUID propertyId = (UUID) getIntent().getSerializableExtra(EXTRA_PROPERTY_ID);
            fragment = PropertyFragment.newInstance(propertyId);
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
