package uk.ac.wlv.chiatiah;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertiesListFragment extends Fragment {
    private RecyclerView mPropertiesRecyclerView;
    private PropertiesAdapter mPropertiesAdapter;
    private List<Properties> selectedProperties;
  //  private File mPhotoFile;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_property_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String userInput) {
                PropertiesLab propertiesLab=PropertiesLab.get(getActivity());
                List<Properties> properties= propertiesLab.getProperties();
                List<Properties> searchedProperties=new ArrayList<>();
                for (Properties p : properties){
                    String heading,description, address, price,postcode;
                    if(p.getDescription()!=null ||
                            p.getHeading()!=null ||
                            p.getPrice()!=null ||
                            p.getPostCode()!=null ||
                            p.getAddress()!=null) {
                        heading=p.getHeading().toLowerCase();
                        description=p.getDescription().toLowerCase();
                        postcode=p.getPostCode().toLowerCase();
                        address=p.getAddress().toLowerCase();
                        price=p.getPrice();

                        if (heading.contains(userInput.toLowerCase()) ||
                                description.contains(userInput.toLowerCase())||
                                address.contains(userInput.toLowerCase()) ||
                                postcode.contains(userInput.toLowerCase())||
                                price.contains(userInput.toLowerCase()) ) {
                            searchedProperties.add(p);
                        }
                    }
                }
                mPropertiesAdapter.setProperties(searchedProperties);
                mPropertiesAdapter.notifyDataSetChanged();
                return true;
            }
        });


//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

      //  return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_new_property:
                Properties properties = new Properties();
                PropertiesLab.get(getActivity()).addProperties(properties);
                Intent intent =PropertyActivity.newIntent(getActivity(), properties.getId());
                startActivity(intent);
                return true;
            case R.id.delete_multiple_properties:
                PropertiesLab propertiesLab = PropertiesLab.get(getActivity());
                List<Properties> properties1 = propertiesLab.getProperties();
                for (Properties p: selectedProperties){
                    PropertiesLab.get(getActivity()).deleteProperty(p);
                }
                updateUI();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_properties_list, container, false);
        mPropertiesRecyclerView = (RecyclerView) v.findViewById(R.id.properties_recycler_view);
        mPropertiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    private void updateUI(){
        PropertiesLab propertiesLab = PropertiesLab.get(getActivity());
        List<Properties> properties = propertiesLab.getProperties();
        if(mPropertiesAdapter == null){
            mPropertiesAdapter = new PropertiesAdapter(properties);
            mPropertiesRecyclerView.setAdapter(mPropertiesAdapter);
        }else{
            mPropertiesAdapter.setProperties(properties);
            mPropertiesAdapter.notifyDataSetChanged();
        }

    }
    private class PropertiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mHeadingTextView;
        public TextView mDescriptionTextView;
        public TextView mAddressTextView;
      //  public TextView mPostCodeTextView;
        public TextView mPriceTextView;
        public TextView mDateTextView;
        private Properties mProperties;
        public ImageView mImageView;
        private CheckBox mCheckBox;

        public  PropertiesHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mHeadingTextView = (TextView) itemView.findViewById(R.id.property_heading_text_view);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.property_description_text_view);
            mAddressTextView = (TextView) itemView.findViewById(R.id.property_address_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.property_image);
          //  mPostCodeTextView = (TextView) itemView.findViewById(R.id.property_postcode_text_view);
            mPriceTextView = (TextView) itemView.findViewById(R.id.property_price_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.property_date_text_view);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.check_box);
           //
        }
        public void onClick(View v){
            Intent intent = PropertyPagerActivity.newIntent(getActivity(), mProperties.getId());

            startActivity(intent);
        }

        public void bindProperties(Properties p){
            mProperties = p;
            mHeadingTextView.setText(p.getHeading());
            mDescriptionTextView.setText(p.getDescription());
            mAddressTextView.setText(p.getAddress());
          //  mPostCodeTextView.setText(p.getPostCode());
            mPriceTextView.setText("£"+(p.getPrice()) + " PCM");
            String dFormat = "EEE, MMM d, ''yy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dFormat);
            String myDate = simpleDateFormat.format(mProperties.getDate());

            mDateTextView.setText("Posted on "+myDate);
            mImageView.setImageBitmap(mProperties.getImageBitmap());
          //  mPhotoFile = PropertiesLab.get(getActivity()).getPhotoFile(mProperties);
            updatePhotoView();

        }

        private void updatePhotoView(){
            if(mProperties.getImageAsString()==null){
                mImageView.setImageDrawable(null);
            }else {
                mImageView.setImageBitmap(mProperties.getImageBitmap());
            }
        }
    }
    private class PropertiesAdapter extends RecyclerView.Adapter<PropertiesHolder>{
        private List<Properties> mProperties;

        public void setProperties(List<Properties> properties){
            mProperties=properties;
        }
        public PropertiesAdapter(List<Properties>properties){
            mProperties=properties;
        }

        public PropertiesHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View  v = layoutInflater.inflate(R.layout.list_item_properties, parent, false);
            return  new PropertiesHolder(v);
        }
        @Override
        public void onBindViewHolder(PropertiesHolder propertiesHolder, int position){
            selectedProperties = new ArrayList<>();
            Properties p = mProperties.get(position);
            propertiesHolder.bindProperties(p);

            propertiesHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        p.setmSelected(propertiesHolder.mCheckBox.isChecked());
                        selectedProperties.add(p);
                    }
                    if(selectedProperties.size()>0){
                        Toast.makeText(getContext(), "Properties selected: \n" + selectedProperties.size(), Toast.LENGTH_SHORT);
                    }
                }
            });
            propertiesHolder.mCheckBox.setChecked(false);
        }
        @Override
        public int getItemCount(){
            return mProperties.size();
        }
    }



}
