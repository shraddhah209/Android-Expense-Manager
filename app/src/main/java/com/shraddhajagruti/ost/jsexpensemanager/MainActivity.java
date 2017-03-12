package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

   /* JSDB sqlmydb;
    SQLiteDatabase sqldb;
    Cursor cursor; */
    private ArrayList<String> results = new ArrayList<String>();
    private String tableName = "expense";
    private SQLiteDatabase newDB;
    ListView listView;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser=null;
    int totalamtexp;
    int totalamtinc;
    int balance;
    int totalamtexp1;

    TextView totalexpense,balanceamt;

   public static final String ANONYMOUS = "anonymous";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        totalexpense= (TextView) findViewById(R.id.todays_total_expense);
        balanceamt = (TextView) findViewById(R.id.total_balance);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        else {
            openAndQueryDatabase();
            displaytotalandbalance();
            displayResultList();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    switch(id) {
                        case R.id.nav_addexp:addexp(); break;
                        case R.id.nav_addinc:addinc(); break;
                        case R.id.nav_graphmonth: viewgraphmonth(); break;
                        case R.id.nav_search: search(); break;
                        case R.id.nav_about: break;

                    }

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
            //mUsername = mFirebaseUser.getDisplayName();
            //if (mFirebaseUser.getPhotoUrl() != null) {
             //   mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
              //  mUsername = ANONYMOUS;
               // mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mFirebaseUser = null;
                mUsername = ANONYMOUS;
                mPhotoUrl = null;
                startActivity(new Intent(this, SignInActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
*/
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void displaytotalandbalance() {
        totalexpense.setText(String.valueOf(totalamtexp1));
        balanceamt.setText(String.valueOf(balance));
    }
    private void displayResultList() {
        TextView tView = new TextView(this);
        listView = (ListView) findViewById(R.id.todays_expenses_list);
        listView.addHeaderView(tView);

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, results));
        listView.setTextFilterEnabled(true);

    }
    private void openAndQueryDatabase() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String strDate = sdf.format(new Date());
        try {
            JSDB dbHelper = new JSDB(this,"JSdatabase",2);
            newDB = dbHelper.getWritableDatabase();

           // Cursor c = newDB.query(tableName,String new[]{amount,category},"date likestrDate,)

            Cursor c = newDB.rawQuery("SELECT amount, category FROM expense where date like '"+strDate+"%'", null);

            if (c != null ) {
                if (c.moveToFirst()) {
                    do {
                        String category = c.getString(c.getColumnIndex("category"));
                        int amt = c.getInt(c.getColumnIndex("amount"));
                        if(category==null)
                        {
                            category="Income";
                            //totalamtinc +=amt;
                        }
                        else
                        totalamtexp1 +=amt;
                        results.add(category + ": " + amt);
                    }while (c.moveToNext());
                }
            }
            Cursor c1 = newDB.rawQuery("SELECT amount, category FROM expense", null);

            if (c1 != null ) {
                if (c1.moveToFirst()) {
                    do {
                        String category = c1.getString(c1.getColumnIndex("category"));
                        int amt = c1.getInt(c1.getColumnIndex("amount"));
                        if(category==null)
                        {
                            totalamtinc +=amt;
                        }
                        else
                        totalamtexp +=amt;
                    }while (c1.moveToNext());
                    balance = totalamtinc - totalamtexp;
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }
    public void addexp()
    {
        Toast.makeText(MainActivity.this, "Add todays expense", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Addexpense.class));
    }
    public void addinc()
    {
        Toast.makeText(MainActivity.this, "Add your income", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, AddIncome.class));
    }
    public void viewgraphmonth()
    {
        Toast.makeText(MainActivity.this, "Monthly Report", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Graph.class));
    }
    public void search()
    {
        Toast.makeText(MainActivity.this, "Select Search Option", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Search.class));
    }
}
