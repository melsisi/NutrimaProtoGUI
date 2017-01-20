package net.nutrima.nutrimaprotogui;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import net.nutrima.engine.NutrimaMetrics;
import net.nutrima.engine.UserProfile;
import net.nutrima.nutrimaprotogui.fragments.CardContentFragment;
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

public class MainActivity extends AppCompatActivity {

    private boolean mainFabPressed = false;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Adapter adapter;

    private FloatingActionButton mainFab;
    private FloatingActionButton logSaveFab;
    private boolean showLogAdd;

    private ViewGroup.LayoutParams layoutParams;

    private ViewGroup.LayoutParams originalLayoutParams;

    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;

    Animation show_fab_2;
    Animation hide_fab_2;

    Animation show_fab_3;
    Animation hide_fab_3;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        String fromPage = intent.getStringExtra("FROM");
        if(fromPage != null && fromPage.equals("PROFILE")){
            Snackbar.make(findViewById(android.R.id.content), "Profile saved!", Snackbar.LENGTH_LONG)
                    .show();
        }

        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);

        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new Adapter(getSupportFragmentManager());

        logSaveFab = (FloatingActionButton) findViewById(R.id.save_log_fab);

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        originalLayoutParams = viewPager.getLayoutParams();
        layoutParams = viewPager.getLayoutParams();
        setupViewPager("main");
        viewPager.setAdapter(adapter);

        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                            Intent activityChangeIntent = new Intent(MainActivity.this,
                                    PersonalInfoActivity.class);
                            startActivity(activityChangeIntent);
                        }
                        else if(menuItem.getTitle().equals("Log out")) {
                            LoginManager.getInstance().logOut(); //Log out from Facebook
                            CognitoSyncClientManager.clear();
                            Intent activityChangeIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(activityChangeIntent);
                            finish();
                        }

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        // Adding Floating Action Button to bottom right of main view
        mainFab = (FloatingActionButton) findViewById(R.id.fab);
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mainFabPressed) {
                    showFabs();
                }
                else {
                    hideFabs();
                }
            }
        });

        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton findFab = (FloatingActionButton) findViewById(R.id.fab_3);
        findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                setupViewPager("find");
            }
        });

        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton logFab = (FloatingActionButton) findViewById(R.id.fab_2);
        logFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                setupViewPager("log");
            }
        });

        logSaveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save log
                setupViewPager("main");
            }
        });

        populateLocalAWSRestaurantList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Populate info from file ///////////////////////////////////////
        UserProfile savedUserProfile = readDataFromFile();
        if(savedUserProfile == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("You need to provide us some info to get started.")
                    .setTitle("Oops");

            builder.setPositiveButton("Add now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent activityChangeIntent = new Intent(MainActivity.this,
                            ProfileCreatorActivity.class);
                            //PersonalInfoActivity.class);
                    startActivity(activityChangeIntent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        //////////////////////////////////////////////////////////////////

        findViewById(R.id.initial_loading_progress).setVisibility(View.GONE);
    }

    // TODO: Move this somewhere else
    @Nullable
    public UserProfile readDataFromFile() {
        UserProfile userProfile = null;

        try {
            FileInputStream fis = openFileInput(getString(R.string.profile_data_file_name));
            ObjectInputStream is = null;

            is = new ObjectInputStream(fis);

            userProfile = (UserProfile) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        NutrimaMetrics nutrimaMetrics = new NutrimaMetrics();
        nutrimaMetrics.calcNutrima(userProfile);
        Globals.getInstance().setNutrimaMetrics(nutrimaMetrics);
        Globals.getInstance().setUserProfile(userProfile);
        return userProfile;
    }

    // Add Fragments to Tabs
    private void setupViewPager(String page) {
        if(page.equals("main")) {
            if(mainFab!= null)
                mainFab.setVisibility(View.VISIBLE);
            viewPager.setLayoutParams(originalLayoutParams);
            logSaveFab.setVisibility(View.INVISIBLE);
            adapter.removeAll();
            adapter.addFragment(new CardContentFragment(), "Your Feed");
            adapter.notifyDataSetChanged();
            showLogAdd = false;
            invalidateOptionsMenu();
        }
        else if(page.equals("find")) {
            mainFab.setVisibility(View.INVISIBLE);
            CoordinatorLayout.LayoutParams layoutParams =
                    new CoordinatorLayout.LayoutParams(viewPager.getLayoutParams().width,
                            viewPager.getLayoutParams().height);
            viewPager.setLayoutParams(layoutParams);
            logSaveFab.setVisibility(View.INVISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            adapter.removeAll();
            adapter.addFragment(new MapFragment(), "NutriMap");
            adapter.addFragment(new ListContentFragment(), "NutriList");
            adapter.notifyDataSetChanged();
            showLogAdd = false;
            invalidateOptionsMenu();
        }
        else if(page.equals("log")) {
            mainFab.setVisibility(View.INVISIBLE);
            viewPager.setLayoutParams(originalLayoutParams);
            logSaveFab.setVisibility(View.VISIBLE);
            adapter.removeAll();
            adapter.addFragment(new LogFragment(), "Log");
            adapter.notifyDataSetChanged();
            showLogAdd = true;
            invalidateOptionsMenu();
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

    private void showFabs() {
        mainFabPressed = !mainFabPressed;
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        TextView fab1Text = (TextView) findViewById(R.id.fab_1_text);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        TextView fab2Text = (TextView) findViewById(R.id.fab_2_text);

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        TextView fab3Text = (TextView) findViewById(R.id.fab_3_text);

        ImageView tintImage = (ImageView) findViewById(R.id.tint_image);
        layoutParams1.rightMargin += (int) (fab1.getWidth() * 1.5);
        layoutParams1.bottomMargin += (int) (fab1.getHeight() * 1.5);
        fab1.setLayoutParams(layoutParams1);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);
        fab1Text.startAnimation(show_fab_1);
        fab1Text.setLayoutParams(layoutParams1);

        layoutParams2.rightMargin += (int) (fab2.getWidth() * 2.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 2.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);
        fab2Text.startAnimation(show_fab_2);
        fab2Text.setLayoutParams(layoutParams2);

        layoutParams3.rightMargin += (int) (fab3.getWidth() * 3.5);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 3.5);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);
        fab3Text.startAnimation(show_fab_3);
        fab3Text.setLayoutParams(layoutParams3);

        tintImage.setVisibility(View.VISIBLE);
    }

    private void hideFabs() {
        mainFabPressed = !mainFabPressed;
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        TextView fab1Text = (TextView) findViewById(R.id.fab_1_text);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        TextView fab2Text = (TextView) findViewById(R.id.fab_2_text);

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        TextView fab3Text = (TextView) findViewById(R.id.fab_3_text);

        ImageView tintImage = (ImageView) findViewById(R.id.tint_image);

        layoutParams1.rightMargin -= (int) (fab1.getWidth() * 1.5);
        layoutParams1.bottomMargin -= (int) (fab1.getHeight() * 1.5);
        fab1.setLayoutParams(layoutParams1);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);
        fab1Text.startAnimation(hide_fab_1);
        fab1Text.setLayoutParams(layoutParams1);

        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 2.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 2.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);
        fab2Text.startAnimation(hide_fab_2);
        fab2Text.setLayoutParams(layoutParams2);

        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 3.5);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 3.5);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
        fab3Text.startAnimation(hide_fab_3);
        fab3Text.setLayoutParams(layoutParams3);

        tintImage.setVisibility(View.INVISIBLE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
}
