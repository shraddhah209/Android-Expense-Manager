package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddIncome extends AppCompatActivity {

    SQLiteDatabase sqldb;
    EditText t;
    Cursor c;
    JSDB mydb=new JSDB(this,"JSdatabase",2);
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        t=(EditText)findViewById(R.id.amount);

        btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(AddIncome.this , MainActivity.class);
                startActivity(intent);

            }
        });

    }

    public void set(View v)
    {   SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String strDate = sdf.format(new Date());
        //int id=new Random().nextInt();
        //String name=t.getText().toString();
        int amt=Integer.parseInt(t.getText().toString());
        ContentValues values = new ContentValues();
        values.put("amount",amt);
        values.put("date", strDate);
        //values.put("phone",ph);
        //values.put("_id",id);
        mydb.insert(values);
    }




}

class JSDBinc extends SQLiteOpenHelper
{
    private final static String tname="income",colname="amount";//colphno="category";
    private static String DBName;
    private int version=1;
    SQLiteDatabase sqldb;

    public JSDBinc(Context c, String JSdatabase, int v)
    {
        super(c,JSdatabase,null,v);
        DBName=JSdatabase;
        version=v;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table income(amount INTEGER,date DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP table "+tname);
        ///onCreate(sqldb);

    }

    public void insert(ContentValues values)
    {
        sqldb=getWritableDatabase();
        sqldb.insert(tname,null,values);
    }

    /*public Cursor get()
    { no needt get over here
        sqldb=getWritableDatabase();
        return sqldb.query(tname,null,null,null,null,null,null,null);
    } */



}

