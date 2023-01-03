package com.example.jitsidemo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
  EditText edt ;
  Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt= findViewById(R.id.edt);
        btn= findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=edt.getText().toString();
                if(url!=null && url!=" ")
                {
                    JitsiMeetConferenceOptions options = null;
                    try {
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
}