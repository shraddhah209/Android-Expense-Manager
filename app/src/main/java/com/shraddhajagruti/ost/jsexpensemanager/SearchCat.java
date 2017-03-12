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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchCat extends AppCompatActivity {

    Button btn, btn1;
    ListView listView;
    private ArrayList<String> results = new ArrayList<String>();
    private SQLiteDatabase newDB;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cat);
        final Spinner spinner = (Spinner) findViewById(R.id.expense_type);

        btn = (Button) findViewById(R.id.search);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                type = (String) spinner.getSelectedItem().toString();
                openAndQueryDatabase();
                displayResultList();
            }
        });
        btn1 = (Button) findViewById(R.id.btn);

        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SearchCat.this, Search.class);
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


        try {
            JSDB dbHelper = new JSDB(this, "JSdatabase", 2);
            newDB = dbHelper.getWritableDatabase();

            Cursor c = newDB.rawQuery("SELECT amount, category,date FROM expense where category like '%"+type+"'", null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        int amt = c.getInt(c.getColumnIndex("amount"));
                        String strDate = c.getString(c.getColumnIndex("date"));
                            results.add("Amount: " + amt + "   " + strDate);
                    } while (c.moveToNext());
                }
            }

        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

    }
}