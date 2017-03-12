package com.shraddhajagruti.ost.jsexpensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Search extends AppCompatActivity {

    Button dateb, monthb,yearb,catb,btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        dateb = (Button) findViewById(R.id.date);

        dateb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this , SearchDate.class);
                startActivity(intent);

            }
        });

        monthb = (Button) findViewById(R.id.month);

        monthb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this , SearchMonth.class);
                startActivity(intent);

            }
        });

        yearb = (Button) findViewById(R.id.year);

        yearb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this , SearchYear.class);
                startActivity(intent);

            }
        });

        catb = (Button) findViewById(R.id.category);

        catb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this , SearchCat.class);
                startActivity(intent);

            }
        });
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Search.this , MainActivity.class);
                startActivity(intent);

            }
        });

    }




}
