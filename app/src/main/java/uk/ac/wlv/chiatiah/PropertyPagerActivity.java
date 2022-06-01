package uk.ac.wlv.chiatiah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class PropertyPagerActivity extends AppCompatActivity {
    private static final String EXTRA_PROPERTY_ID = "uk.ac.wlv.chiatiah.property_id";
    private ViewPager mViewPager;
    private List<Properties> mProperties;

    public static Intent newIntent(Context packageContext, UUID propertyId){
        Intent intent = new Intent(packageContext, PropertyPagerActivity.class);
        intent.putExtra(EXTRA_PROPERTY_ID, propertyId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_pager);
        UUID propertyId = (UUID) getIntent().getSerializableExtra(EXTRA_PROPERTY_ID);
        mViewPager = (ViewPager) findViewById(R.id.activity_property_pager_view);
        mProperties = PropertiesLab.get(this).getProperties();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Properties properties = mProperties.get(position);
                return PropertyViewFragment.newInstance(properties.getId());
            }

            @Override
            public int getCount() {
                return mProperties.size();
            }
        });
        for(int i=0; i<mProperties.size();i++){
            if(mProperties.get(i).getId().equals(propertyId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
