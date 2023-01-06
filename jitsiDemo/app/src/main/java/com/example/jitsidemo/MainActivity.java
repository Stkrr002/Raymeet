package com.example.jitsidemo;
import static java.text.DateFormat.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  EditText edt ;
  Button btn;
  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt= findViewById(R.id.edt);
        btn= findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid= user.getUid();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=edt.getText().toString();
                if(url!=null && url!=" ")
                {
                    JitsiMeetConferenceOptions options = null;
                    try {
                        uploadData();
                        options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setRoom(url)
                                .setFeatureFlag("welcomepage.enabled", false)
                                .setAudioMuted(false)
                                .setVideoMuted(false)
                                .setAudioOnly(false)
                                .setConfigOverride("requireDisplayName", true)
                                .build();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JitsiMeetActivity.launch(MainActivity.this, options);

                }
            }
        });
    }
    public void uploadData(){
        String title = edt.getText().toString();
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ", Locale.getDefault());
        String date=sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String time = sdf.format(new Date());
        ClassProfile ClassProfile = new ClassProfile(title,time);
        FirebaseDatabase.getInstance().getReference("History").child(mAuth.getUid()).child(date).child(time)
                .setValue(ClassProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
