package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchMonth extends AppCompatActivity {

    EditText m,y;
    Button btn,btn1;
    ListView listView;
    private ArrayList<String> results = new ArrayList<String>();
    private String tableName = "expense";
    private SQLiteDatabase newDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_month);

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
                Intent intent = new Intent(SearchMonth.this , Search.class);
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
        String strDatem = m.getText().toString();
        String strDatey = y.getText().toString();
        try {
            JSDB dbHelper = new JSDB(this,"JSdatabase",2);
            newDB = dbHelper.getWritableDatabase();

            // Cursor c = newDB.query(tableName,String new[]{amount,category},"date likestrDate,)

            Cursor c = newDB.rawQuery("SELECT amount, category,date FROM expense where date like '%"+strDatem+"%"+strDatey+"%'", null);

            if (c != null ) {
                if (c.moveToFirst()) {
                    do {
                        String category = c.getString(c.getColumnIndex("category"));
                        int amt = c.getInt(c.getColumnIndex("amount"));
                        String strDate = c.getString(c.getColumnIndex("date"));
                        if(category==null)
                        {
                            category="Income";
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
