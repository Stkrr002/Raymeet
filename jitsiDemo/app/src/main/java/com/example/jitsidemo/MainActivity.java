package com.example.jitsidemo;
import static java.text.DateFormat.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.BroadcastIntentHelper;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
  EditText edt ;
  Button btn;
  FirebaseAuth mAuth;
  String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt= findViewById(R.id.edt);
        btn= findViewById(R.id.btn);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid= user.getUid();
        Uri imageUrl= user.getPhotoUrl();
          userEmail=user.getEmail();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=edt.getText().toString();
                if(url!=null && url!=" ")
                {
                    JitsiMeetConferenceOptions options = null;
                    try {
                        uploadData();
                        Bundle b= new Bundle();
                        b.putString("displayName", "sumit kumar");
                        b.putString("avatarURL",imageUrl.toString());
                        b.putString("email",userEmail);
                        JitsiMeetUserInfo userInfo= new JitsiMeetUserInfo(b);
                        options = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(new URL("https://meet.jit.si"))
                                .setRoom(url)
                                .setFeatureFlag("welcomepage.enabled", false)
                                .setUserInfo(userInfo)
                                .build();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    JitsiMeetActivity.launch(MainActivity.this, options);
                    registerForBroadcastMessages();
                }
            }
        });
    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBroadcastReceived(intent);
        }
    };
    public void uploadData(){
        String title = edt.getText().toString();
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ", Locale.getDefault());
        String date=sdf.format(new Date());
        sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String time = sdf.format(new Date());
        ClassProfile ClassProfile = new ClassProfile(title,userEmail);
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

    private void registerForBroadcastMessages() {
        IntentFilter intentFilter = new IntentFilter();

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
         */
        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.getAction());
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    // Example for handling different JitsiMeetSDK events
    private void onBroadcastReceived(Intent intent) {
        if (intent != null) {
            BroadcastEvent event = new BroadcastEvent(intent);

            switch (event.getType()) {
                case CONFERENCE_JOINED:
                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
                    break;
                case PARTICIPANT_JOINED:
                    Timber.i("Participant joined%s", event.getData().get("name"));
                    break;
                case READY_TO_CLOSE:
                    Toast.makeText(this, "kaat rha h", Toast.LENGTH_SHORT).show();
                    break;
                case AUDIO_MUTED_CHANGED:
                    Toast.makeText(this, "mic changed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Example for sending actions to JitsiMeetSDK
//    private void hangUp() {
//        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//    }
}
