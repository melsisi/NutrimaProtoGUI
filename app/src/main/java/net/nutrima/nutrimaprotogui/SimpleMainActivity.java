package net.nutrima.nutrimaprotogui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.facebook.login.LoginManager;
import com.google.android.gms.wearable.internal.GetLocalNodeResponse;

import net.nutrima.aws.RestaurantMenuItem;
import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.UserProfile;
import net.nutrima.nutrimaprotogui.fragments.ListContentFragment;
import net.nutrima.nutrimaprotogui.fragments.LogFragment;
import net.nutrima.nutrimaprotogui.fragments.MapFragment;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SimpleMainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabs;
    private Adapter adapter;

    private FloatingActionButton logSaveFab;
    private boolean showLogAdd;
    private DrawerLayout mDrawerLayout;

    private MapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_main);

        Globals.getInstance().setRunningInLambda(true);

        handleConfigFilterButton();

        // NOTE: This was only for an experiment.
        // This demonstrates how to write to
        // DynamoDB from this app.
        // populateTestMenusList();

        Intent intent = this.getIntent();
        String fromPage = intent.getStringExtra("FROM");
        if(fromPage != null && fromPage.equals("PROFILE")){
            Snackbar.make(findViewById(android.R.id.content), "Profile saved!", Snackbar.LENGTH_LONG)
                    .show();
        }

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new Adapter(getSupportFragmentManager());

        // This is a handler to the log save FloatingActionButton.
        // Will be added back later.
        //logSaveFab = (FloatingActionButton) findViewById(R.id.save_log_fab);

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager("find");
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(Globals.getInstance().getFbResponse() != null) {
            RelativeLayout navHeaderView = (RelativeLayout) navigationView.getHeaderView(0);
            TextView navHeaderTextView = (TextView) navHeaderView.findViewById(R.id.nav_header_text_view);
            navHeaderTextView.setText(Globals.getInstance().getFbResponse().getString("name"));

            ImageView navHeaderImageView = (ImageView) navHeaderView.findViewById(R.id.nav_header_image_view);
            Drawable ratingImage = null;
            try {
                ratingImage = new UrlAsyncTask().execute(
                        Globals.getInstance().getFbResponse().getString("profile_pic")).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            navHeaderImageView.setImageDrawable(ratingImage);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator =
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        if(menuItem.getTitle().equals("Dashboard")) {
                            setupViewPager("main");
                        }
                        else if(menuItem.getTitle().equals("My profile")) {
                            Intent activityChangeIntent = new Intent(SimpleMainActivity.this,
                                    PersonalInfoActivity.class);
                            startActivity(activityChangeIntent);
                        }
                        else if(menuItem.getTitle().equals("Log out")) {
                            LoginManager.getInstance().logOut(); //Log out from Facebook
                            CognitoSyncClientManager.clear();
                            Intent activityChangeIntent = new Intent(SimpleMainActivity.this, SimpleMainActivity.class);
                            startActivity(activityChangeIntent);
                            finish();
                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save log
                setupViewPager("main");
            }
        });*/

        if(!Globals.getInstance().isRunningInLambda())
            populateLocalAWSRestaurantList();

    }

    private void handleConfigFilterButton() {
        ImageButton configFilterButton = (ImageButton) findViewById(R.id.config_filter_button);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.config_filter, null);

        SeekBar maxCaloriesSeekBar = (SeekBar) dialogView.findViewById(R.id.config_filter_max_calories_seekbar);
        final TextView maxCaloriesValue = (TextView) dialogView.findViewById(R.id.max_calories_value);

        maxCaloriesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                maxCaloriesValue.setText(Integer.toString(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        configFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(dialogView)
                        // Add action buttons
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        findViewById(R.id.initial_loading_progress).setVisibility(View.GONE);
    }

    // Add Fragments to Tabs
    private void setupViewPager(String page) {
        if(page.equals("find")) {
            CoordinatorLayout.LayoutParams layoutParams =
                    new CoordinatorLayout.LayoutParams(viewPager.getLayoutParams().width,
                            viewPager.getLayoutParams().height);
            viewPager.setLayoutParams(layoutParams);
            //logSaveFab.setVisibility(View.INVISIBLE);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            //adapter.removeAll();
            mapFragment = new MapFragment();
            adapter.addFragment(mapFragment, "NutriMap");
            adapter.addFragment(new ListContentFragment(), "NutriList");
            //adapter.notifyDataSetChanged();
            //showLogAdd = false;
            //invalidateOptionsMenu();
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            notifyDataSetChanged();
        }

        public void removeAll(){
            mFragmentList.clear();
            mFragmentTitleList.clear();
            notifyDataSetChanged();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position >= getCount()) {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(showLogAdd);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ((LogFragment)adapter.getItem(0)).speak(this);
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateLocalAWSRestaurantList() {
        ArrayList<String> list = new ArrayList<>();

        InputStream file = null;
        try {
            file = getResources().openRawResource(getResources().getIdentifier("aws_restaurant_names",
                    "raw", getPackageName()));

            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            //Create Workbook instance holding reference to .xlsx file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip first two rows
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                if(cellIterator.hasNext())
                    list.add(cellIterator.next().getStringCellValue());
                else
                    break;
            }
            file.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Globals.getInstance().setAWSRestaurants(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapFragment.REQUEST_LOCATION){
            mapFragment.onActivityResult(requestCode, resultCode, data);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void populateTestMenusList() {
        ArrayList<RestaurantMenuItem> list = new ArrayList<>();

        InputStream file = null;
        try {
            file = getResources().openRawResource(getResources().getIdentifier("menus_test",
                    "raw", getPackageName()));

            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            //Create Workbook instance holding reference to .xlsx file
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip first row
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int i = 0;
                RestaurantMenuItem toAdd = new RestaurantMenuItem();
                while(cellIterator.hasNext()) {
                    if (i == 0)
                        toAdd.setRestaurant(cellIterator.next().getStringCellValue());
                    else if (i == 1)
                        toAdd.setFoodCategory(cellIterator.next().getStringCellValue());
                    else if (i == 2)
                        toAdd.setItemName(cellIterator.next().getStringCellValue());
                    else if (i == 3)
                        toAdd.setItemDescription(cellIterator.next().getStringCellValue());
                    else if (i == 4) {
                        toAdd.setServingsPerItem("13");
                        cellIterator.next();
                    }
                    else if (i == 5) {
                        toAdd.setServingSizeMetric("34");
                        cellIterator.next();
                    }
                    else if (i == 6)
                        toAdd.setServingSizeUnit(cellIterator.next().getStringCellValue());
                    else if (i == 7)
                        toAdd.setServingsSizePieces(cellIterator.next().getStringCellValue());
                    else if (i == 8) {
                        toAdd.setCalories("300");
                        cellIterator.next();
                    }
                    else if (i == 9) {
                        //TODO: fix later
                        toAdd.setTotalFat("100");
                        cellIterator.next();
                    } else if (i == 10) {
                        toAdd.setSaturatedFat("100");
                        cellIterator.next();
                    }
                    else if (i == 11) {
                        toAdd.setTransFat("100");
                        cellIterator.next();
                    }
                    else if (i == 12) {
                        toAdd.setCholesterol("100");
                        cellIterator.next();
                    }
                    else if (i == 13) {
                        toAdd.setSodium("100");
                        cellIterator.next();
                    }
                    else if (i == 14) {
                        toAdd.setPotassium("100");
                        cellIterator.next();
                    }
                    else if (i == 15) {
                        toAdd.setCarbohydrates("100");
                        cellIterator.next();
                    }
                    else if (i == 16) {
                        toAdd.setFiber("100");
                        cellIterator.next();
                    }
                    else if (i == 17) {
                        toAdd.setSugar("100");
                        cellIterator.next();
                    }
                    else if (i == 18) {
                        toAdd.setProtein("100");
                        cellIterator.next();
                    }
                    else {
                        break;
                    }
                    i++;
                }
                list.add(toAdd);
            }
            file.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Globals.getInstance().setTempMenuesList(list);
    }

    static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
