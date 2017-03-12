package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchDate extends AppCompatActivity {

    EditText d,m,y;
    Button btn,btn1;
    ListView listView;
    private ArrayList<String> results = new ArrayList<String>();
    private String tableName = "expense";
    private SQLiteDatabase newDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date);

        d=(EditText)findViewById(R.id.d);
        m=(EditText)findViewById(R.id.m);
        y=(EditText)findViewById(R.id.y);

        btn = (Button) findViewById(R.id.search);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openAndQueryDatabase();
               displayResultList();
            }
        });
        btn1 = (Button) findViewById(R.id.btn);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SearchDate.this , Search.class);
                startActivity(intent);

            }
        });


    }
    private void displayResultList() {
        TextView tView = new TextView(this);
        listView = (ListView) findViewById(R.id.date_list);
        listView.addHeaderView(tView);

        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, results));
        listView.setTextFilterEnabled(true);
    }
    private void openAndQueryDatabase() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String strDate = m.getText().toString()+" "+d.getText().toString()+", "+y.getText().toString();
        try {
            JSDB dbHelper = new JSDB(this,"JSdatabase",2);
            newDB = dbHelper.getWritableDatabase();

            // Cursor c = newDB.query(tableName,String new[]{amount,category},"date likestrDate,)

            Cursor c = newDB.rawQuery("SELECT amount, category FROM expense where date like '%"+strDate+"%'", null);

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
                        results.add(category + ": " + amt +"   "+ strDate);
                    }while (c.moveToNext());
                }
            }

        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }
}
