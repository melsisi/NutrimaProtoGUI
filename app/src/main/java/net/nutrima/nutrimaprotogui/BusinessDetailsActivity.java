package net.nutrima.nutrimaprotogui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import net.nutrima.aws.RestaurantMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BusinessDetailsActivity extends AppCompatActivity {

    private static List<RestaurantMenuItem> plateNamesPM;
    private static List<RestaurantMenuItem> plateNamesFM;
    private static Business business;
    private ArrayList<RestaurantMenuItem> plateNamesToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting Restaurant name as title //////////////////////////////
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        Intent intent = this.getIntent();
        String businessName = intent.getStringExtra("BUSINESS_NAME");
        ctl.setTitle(businessName);
        //////////////////////////////////////////////////////////////////

        // Creating menu listview ////////////////////////////////////////
        final ListView menuListView = (ListView) findViewById(R.id.menu_listview);

        if(!Globals.getInstance().isRunningInLambda()) {
            business = null;
            plateNamesFM = new ArrayList<>();
            plateNamesPM = new ArrayList<>();

            // Get full menu ///////////////////////////////////
            for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                    Globals.getInstance().getRestaurantFullMenuMap().entrySet()) {
                if (entry.getKey().getName().toLowerCase().equals(businessName.toLowerCase())) {
                    plateNamesFM.addAll(entry.getValue());
                    business = entry.getKey();
                    break;
                }
            }
            ///////////////////////////////////////////////

            // Get personalized menu ///////////////////////////////////
            for (Map.Entry<Business, List<RestaurantMenuItem>> entry :
                    Globals.getInstance().getRestaurantPersonalizedMenuMap().entrySet()) {
                if (entry.getKey().getName().toLowerCase().equals(businessName.toLowerCase())) {
                    plateNamesPM.addAll(entry.getValue());
                    break;
                }
            }
            ///////////////////////////////////////////////

        }
        plateNamesToDisplay = new ArrayList<>();

        plateNamesToDisplay.addAll(plateNamesPM);

        final ListMenuItemAdapter customAdapter = new ListMenuItemAdapter(this,
                R.layout.item_list,
                R.id.menu_listview,
                plateNamesToDisplay);

        menuListView.setAdapter(customAdapter);
        //////////////////////////////////////////////////////////////////

        // Handle full menu switch ///////////////////////////////////////
        Switch fullMenuSwitch = (Switch) findViewById(R.id.full_menu_switch);
        fullMenuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                plateNamesToDisplay.clear();
                if (isChecked) {
                    plateNamesToDisplay.addAll(plateNamesFM);
                } else {
                    plateNamesToDisplay.addAll(plateNamesPM);
                }

                customAdapter.notifyDataSetChanged();
            }
        });

        // Adding Yelp rating image //////////////////////////////////////
        ImageView ratingImageView = (ImageView) findViewById(R.id.rating_imageview);
        Drawable ratingImage = null;
        try {
            ratingImage = new UrlAsyncTask().execute(business.getRatingImageUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ratingImageView.setImageDrawable(ratingImage);
        //////////////////////////////////////////////////////////////////

        // Adding business image //////////////////////////////////////
        ImageView businessImageView = (ImageView) findViewById(R.id.business_image);
        Drawable businessImage = null;
        try {
            businessImage = new UrlAsyncTask().execute(business.getImageUrl()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        businessImageView.setImageDrawable(businessImage);
        //////////////////////////////////////////////////////////////////
    }

    public static void setPlateNamesPM(List<RestaurantMenuItem> plateNamesPM) {
        BusinessDetailsActivity.plateNamesPM = plateNamesPM;
    }

    public static void setPlateNamesFM(List<RestaurantMenuItem> plateNamesFM) {
        BusinessDetailsActivity.plateNamesFM = plateNamesFM;
    }

    public static void setBusiness(Business business) {
        BusinessDetailsActivity.business = business;
    }
}
