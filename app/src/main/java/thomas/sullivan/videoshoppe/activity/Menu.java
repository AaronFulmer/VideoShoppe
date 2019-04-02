package thomas.sullivan.videoshoppe.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import thomas.sullivan.videoshoppe.fragment.HomeFragment;
import thomas.sullivan.videoshoppe.fragment.MoviesFragment;
import thomas.sullivan.videoshoppe.fragment.CustomersFragment;
import thomas.sullivan.videoshoppe.fragment.InventoryFragment;
import thomas.sullivan.videoshoppe.fragment.EmployeeFragment;
import thomas.sullivan.videoshoppe.fragment.LogoutFragment;
import thomas.sullivan.videoshoppe.resources.Database;
import thomas.sullivan.videoshoppe.resources.IntValueFormatter;

import java.util.ArrayList;

import java.util.Calendar;

public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Database database;
    String firstName;
    String lastName;

    BarChart chart;
    Calendar calendar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_CUSTOMERS = "customers";
    private static final String TAG_INVENTORY = "inventory";
    private static final String TAG_EMPLOYEE = "employee";
    private static final String TAG_LOGOUT = "logout";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    // Calender information
    private String currentDayString;
    private int currentDayInt;
    private int sundayCount;
    private int mondayCount;
    private int tuesdayCount;
    private int wednesdayCount;
    private int thursdayCount;
    private int fridayCount;
    private int saturdayCount;
    private int thirtyDayCounter;
    private int thirtyDayDisplay;
    private int rentalsInDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        database = new Database(this);
        firstName = database.getLoggedInUserFirstName();
        lastName = database.getLoggedInUserLastName();

        // Bar Graph
        chart = (BarChart) findViewById(R.id.chart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        //Calendar
        calendar = Calendar.getInstance();
        currentDayInt = calendar.get(Calendar.DAY_OF_WEEK);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        //Creates Bar Graph
        createChart();
        
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }


    public void createChart()
    {
        calendarRun();
        setData(7);
        chart.setFitBars(true);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);

    }

    public void calendarRun()
    {
        //Sets currentDay to the day of the week
        switch(currentDayInt)
        {
            case 1:
                currentDayString = "Sunday";
                sundayCount = 0;
                sundayCount += rentalsInDay;
                break;
            case 2:
                currentDayString = "Monday";
                mondayCount = 0;
                mondayCount += rentalsInDay;
                break;
            case 3:
                currentDayString = "Tuesday";
                tuesdayCount = 0;
                tuesdayCount += rentalsInDay;
                break;
            case 4:
                currentDayString = "Wednesday";
                wednesdayCount = 0;
                wednesdayCount += rentalsInDay;
                break;
            case 5:
                currentDayString = "Thursday";
                thursdayCount = 0;
                thursdayCount += rentalsInDay;
                break;
            case 6:
                currentDayString = "Friday";
                fridayCount = 0;
                fridayCount += rentalsInDay;
                break;
            case 7:
                currentDayString = "Saturday";
                saturdayCount = 0;
                saturdayCount += rentalsInDay;
                break;
            default:
                currentDayString = "INVALID";
                break;
        }
    }

    private void setData(int count)
    {
        int[] intArray = new int[7];
        String[] dayNames = new String[7];
        ArrayList<BarEntry> yVals = new ArrayList<>();

        int tempDate = currentDayInt;
        String tempDay = "";
        int arrayCounter = 0;
        boolean isFinished = false;

        while(isFinished == false)
        {
            int numberToAdd = -1;
            switch(tempDate)
            {
                case 1:
                    tempDay = "Sun";
                    numberToAdd = sundayCount;
                    break;
                case 2:
                    tempDay = "Mon";
                    numberToAdd = mondayCount;
                    break;
                case 3:
                    tempDay = "Tues";
                    numberToAdd = tuesdayCount;
                    break;
                case 4:
                    tempDay = "Wed";
                    numberToAdd = wednesdayCount;
                    break;
                case 5:
                    tempDay = "Thurs";
                    numberToAdd = thursdayCount;
                    break;
                case 6:
                    tempDay = "Fri";
                    numberToAdd = fridayCount;
                    break;
                case 7:
                    tempDay = "Sat";
                    numberToAdd = saturdayCount;
                    break;
            }


            intArray[6 - arrayCounter] = numberToAdd;
            dayNames[6 - arrayCounter] = tempDay;

            dayNames[6] = "Today";

            // controls day #s
            if(tempDate == 1)
            {
                tempDate = 7;
            } else {
                tempDate--;
            }

            arrayCounter++;

            if(arrayCounter == 7)
            {
                isFinished = true;
            }
        }

        for(int i=0;i<count; i++)
        {
            int value = i+1;
            yVals.add(new BarEntry(i, (int) value));
        }

        BarDataSet set = new BarDataSet(yVals, "Data Set");

        //  FORMATTING

        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setDrawValues(true);

        set.setLabel("Movie Rentals");

        //   END FORMATTING

        BarData data = new BarData(set);

        //  FORMATTING
        set.setHighlightEnabled(false);
        data.setValueTextSize(16);
        data.setValueFormatter(new IntValueFormatter());
        set.setValueFormatter(new IntValueFormatter());

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(14);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dayNames));
        xAxis.setDrawGridLines(false);

        YAxis rightYAxis = chart.getAxisRight();
        YAxis leftYAxis = chart.getAxisLeft();
        leftYAxis.setDrawGridLines(false);
        leftYAxis.setTextSize(14);
        rightYAxis.setEnabled(false);

        chart.setExtraOffsets(10, 10, 10, 10);
        chart.getLegend().setEnabled(false);

        //   END FORMATTING

        chart.setData(data);
        chart.invalidate();
        chart.animateY(1200);
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText(firstName);
        txtWebsite.setText(lastName);

        // loading header background image
        Glide.with(this).load(R.drawable.bg).into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(R.drawable.circleuser).into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }



        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // movies fragment
                MoviesFragment moviesFragment = new MoviesFragment();
                return moviesFragment;
            case 2:
                // customers fragment
                CustomersFragment customersFragment = new CustomersFragment();
                return customersFragment;
            case 3:
                // inventory fragment
                InventoryFragment inventoryFragment = new InventoryFragment();
                return inventoryFragment;
            case 4:
                // employee fragment
                EmployeeFragment employeeFragment = new EmployeeFragment();
                return employeeFragment;
            case 5:
                // logout fragment
                LogoutFragment logoutFragment = new LogoutFragment();
                return logoutFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_customers:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_CUSTOMERS;
                        break;
                    case R.id.nav_inventory:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_INVENTORY;
                        break;
                    case R.id.nav_employee:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_EMPLOYEE;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_LOGOUT;
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, (android.view.Menu) menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
