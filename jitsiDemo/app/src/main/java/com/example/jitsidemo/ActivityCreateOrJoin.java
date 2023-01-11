package com.example.jitsidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCreateOrJoin extends AppCompatActivity {
 Button btnCreate,btnJoin,btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join);
        btnCreate=findViewById(R.id.btnCreate);
        btnJoin=findViewById(R.id.btnJoin);
        btnHistory=findViewById(R.id.btnHistory);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("action","create");
                startActivity(i);



            }
        });
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("action","join");
                startActivity(i);



            }
        });
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),ActivityHistory.class);
                startActivity(i);
            }
        });

    }
}