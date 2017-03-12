package com.shraddhajagruti.ost.jsexpensemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Graph extends AppCompatActivity {

    private ArrayList<Integer> results = new ArrayList<>();
    private SQLiteDatabase newDB;
    BarChart barChart;
    int foodsum;
    int rentsum;
    int transfersum;
    int transportsum;
    int othersum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        String strDate = sdf.format(new Date());
        try {
            JSDB dbHelper = new JSDB(this,"JSdatabase",2);
            newDB = dbHelper.getWritableDatabase();

            // Cursor c = newDB.query(tableName,String new[]{amount,category},"date likestrDate,)

            Cursor c = newDB.rawQuery("SELECT sum(amount) FROM expense where date like '%"+strDate+"%' and category='Food'", null);

            if (c != null ) {
                if (c.moveToFirst()) {
                    foodsum= c.getInt(0);
                }
            }
            Cursor cr = newDB.rawQuery("SELECT sum(amount) FROM expense where date like '%"+strDate+"%' and category='Rent'", null);

            if (cr != null ) {
                if (cr.moveToFirst()) {
                    rentsum= cr.getInt(0);
                }
            }
            Cursor ctr = newDB.rawQuery("SELECT sum(amount) FROM expense where date like '%"+strDate+"%' and category='Transport'", null);

            if (ctr != null ) {
                if (ctr.moveToFirst()) {
                    transportsum= ctr.getInt(0);
                }
            }
            Cursor ct = newDB.rawQuery("SELECT sum(amount) FROM expense where date like '%"+strDate+"%' and category='Transfer'", null);

            if (ct != null ) {
                if (ct.moveToFirst()) {
                    transfersum= ct.getInt(0);
                }
            }
            Cursor co = newDB.rawQuery("SELECT sum(amount) FROM expense where date like '%"+strDate+"%' and category='Other'", null);

            if (co != null ) {
                if (co.moveToFirst()) {
                    othersum= co.getInt(0);
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }

        barChart = (BarChart) findViewById(R.id.bargraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(foodsum,0));
        barEntries.add(new BarEntry(rentsum,1));
        barEntries.add(new BarEntry(transfersum,2));
        barEntries.add(new BarEntry(transportsum,3));
        barEntries.add(new BarEntry(othersum,4));
        BarDataSet barDataSet = new BarDataSet(barEntries,"Total amount spent");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("F");
        categories.add("R");
        categories.add("Trf");
        categories.add("Trt");
        categories.add("O");


        BarData theData = new BarData(categories,barDataSet);
        barChart.setData(theData);


        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);

    }
}
