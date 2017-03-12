package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Addexpense extends AppCompatActivity {

    SQLiteDatabase sqldb;
    EditText t;
    Spinner spinner;
    Cursor c;
    JSDB mydb = new JSDB(this, "JSdatabase", 2);
    Button btn, b;
    String strDate;
    private Firebase nRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpense);

        nRef = new Firebase("https://js-expense-manager.firebaseio.com/User");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        t = (EditText) findViewById(R.id.amountex);
        final Spinner spinner = (Spinner) findViewById(R.id.expense_type);
        strDate = sdf.format(new Date());
        btn = (Button) findViewById(R.id.button1);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Addexpense.this, MainActivity.class);
                startActivity(intent);

            }
        });


        b = (Button) findViewById(R.id.add_expenseex);

        b.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Toast.makeText(Addexpense.this, "Expense Added", Toast.LENGTH_SHORT).show();
                int amt = Integer.parseInt(t.getText().toString());
                String type = (String) spinner.getSelectedItem();
                ContentValues values = new ContentValues();
                values.put("amount", amt);
                values.put("category", type);
                values.put("date", strDate);
                mydb.insert(values);
            }
        });
    }

    @Override
    public void onBackPressed() {



    }
}
class JSDB extends SQLiteOpenHelper
{
    public final static String tname="expense",colname="amount",colphno="category";
    private static String DBName;
    private int version=1;
    SQLiteDatabase sqldb;

    public JSDB(Context c, String JSdatabase, int v)
    {
        super(c,JSdatabase,null,v);
        DBName=JSdatabase;
        version=v;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table expense(amount INTEGER,category TEXT,date DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(ContentValues values)
    {
        sqldb=getWritableDatabase();
        sqldb.insert(tname,null,values);
    }

}

