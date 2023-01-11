package com.example.jitsidemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistory extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    List<ClassHistory> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mAuth=FirebaseAuth.getInstance();
        String uid= mAuth.getUid();
        dataList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("History").child(uid);



        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                ClassHistory classHistory;
                ClassTime classTime;
                String date=null;
                ArrayList<ClassTime> timeList;
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    date = itemSnapshot.getKey();
                    timeList=new ArrayList<>();
                    for (DataSnapshot it : itemSnapshot.getChildren()){
                        String time = it.getKey().toString();
                        ClassProfile classProfile= it.getValue(ClassProfile.class);
                        String email=classProfile.getemail(),meetName=classProfile.getmeetName();
                        classTime= new ClassTime(time,meetName,email);
                        timeList.add(classTime);
                    //  dataClass.setmeetName(itemSnapshot.getKey());

                    }
                    classHistory= new ClassHistory(date,timeList);
                    dataList.add(classHistory);

                }

                Toast.makeText(ActivityHistory.this, "done", Toast.LENGTH_SHORT).show();
                func();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    private void func()
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_history);
        AdapterHistory adapter = new AdapterHistory(dataList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}