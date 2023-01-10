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
import org.jitsi.meet.sdk.JitsiMeet;
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
  Button btn,btnShare,btnGenerate;
  FirebaseAuth mAuth;
  String userEmail,meetName=null,action=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt= findViewById(R.id.edt);
        btn= findViewById(R.id.btn);
        btnShare= findViewById(R.id.btnShare);
        btnGenerate = findViewById(R.id.btnGenerate);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid= user.getUid();
        Uri imageUrl= user.getPhotoUrl();
        userEmail=user.getEmail();


        action= getIntent().getStringExtra("action");
        if(action.equalsIgnoreCase("join"))
        {
            btnGenerate.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btn.setText("Join");
            edt.setHint("Enter already existing code");
        }
        else
        {
            btn.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
            btn.setText("Create meet");
        }

        URL serverURL = null;
        try {
            serverURL = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .build();

        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

         btnGenerate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                meetName=edt.getText().toString();
                 if(meetName!=null && !meetName.isEmpty()) {
                     meetName += generateUniqueCode() + uid.substring(1,4);
                     btn.setVisibility(View.VISIBLE);
                     btnShare.setVisibility(View.VISIBLE);
                     btnGenerate.setVisibility(View.GONE);
                     edt.setFocusable(false);
                 }else{
                     Toast.makeText(MainActivity.this, "enter valid text", Toast.LENGTH_SHORT).show();
                 }
             }
         });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action.equalsIgnoreCase("join"))
                meetName=edt.getText().toString();
                    JitsiMeetConferenceOptions options = null;
                    Bundle b= new Bundle();
                    b.putString("displayName", "sumit kumar");
                    b.putString("avatarURL",imageUrl.toString());
                    b.putString("email",userEmail);
                    JitsiMeetUserInfo userInfo= new JitsiMeetUserInfo(b);

                    options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(meetName)
                            .setFeatureFlag("welcomepage.enabled", false)
                            .setFeatureFlag("live-streaming.enabled",true)
                          //  .setFeatureFlag("invite.enabled",false)
                            .setFeatureFlag("video-share.enabled", false)
                            .setFeatureFlag("fullscreen.enabled", true)
                            .setUserInfo(userInfo)
                            .build();
                    uploadData();
                    JitsiMeetActivity.launch(MainActivity.this, options);
                  //  registerForBroadcastMessages();
                }

        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "To join the meeting on SkyMeet Conference, please use\n\n"
                        + "Code: " + meetName + "\n\nDownload SkyMeet:\n\n"
                        + "Android: https://play.google.com/store/apps/details?id=com.skymeet.videoConference\n\n"
                        + "iOS: An apple a day keeps a doctor away but visiting a lady doctor everyday could be your spouse someday.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Code via"));
            }
        });
    }

//    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//           onBroadcastReceived(intent);
//        }
//    };
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

//    private void registerForBroadcastMessages() {
//        IntentFilter intentFilter = new IntentFilter();
//
//        /* This registers for every possible event sent from JitsiMeetSDK
//           If only some of the events are needed, the for loop can be replaced
//           with individual statements:
//           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
//                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
//                ... other events
//         */
////        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
////            intentFilter.addAction(type.getAction());
////        }
//        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.getAction());
//
//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
//    }

    // Example for handling different JitsiMeetSDK events
//    private void onBroadcastReceived(Intent intent) {
//        if (intent != null) {
//            BroadcastEvent event = new BroadcastEvent(intent);
//
//            switch (event.getType()) {
//                case CONFERENCE_JOINED:
//                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
//                    uploadData();
//                    break;
//////                case PARTICIPANT_JOINED:
//////                    Timber.i("Participant joined%s", event.getData().get("name"));
//////                    break;
//                case READY_TO_CLOSE:
//                    Toast.makeText(this, "meet ended", Toast.LENGTH_SHORT).show();
//                  //  hangUp();
//                    break;
//////                case AUDIO_MUTED_CHANGED:
//////                    Toast.makeText(this, "mic changed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private String generateUniqueCode() {
        int max = 1000;
        int min = 1;
        int range = max - min + 1;

        // generate random numbers within 1 to 10
            int rand = (int) (Math.random() * range) + min;
            return String.valueOf(rand);

    }

    // Example for sending actions to JitsiMeetSDK
//    private void hangUp() {
//        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//    }
}
