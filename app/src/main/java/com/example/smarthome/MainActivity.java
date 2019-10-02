package com.example.smarthome;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference LEDRef,lockRef,curtainsRef,intruderRef;
    Button ledButton,lockButton,curtainButton;
    int ledStatus;
    int lockStatus;
    int curtainStatus;
    int intruderStatus;
    Context mContext;
    private static final String TAG = "Main Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ledButton = findViewById(R.id.LED_button);
        lockButton = findViewById(R.id.Lock_button);
        curtainButton = findViewById(R.id.curtains_button);
        mContext = this;
        database = FirebaseDatabase.getInstance();
        LEDRef = database.getReference("Appliances/LED");
        lockRef = database.getReference("Appliances/lock");
        curtainsRef = database.getReference("Appliances/curtains");
        intruderRef = database.getReference("Appliances/intruder");

        createNotificationChannel();
        configureLEDButton();
        configureLockButton();
        configureCurtainsButton();
        configureIntruderNotification();

    }

    private void configureIntruderNotification() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                intruderStatus = (int) dataSnapshot.getValue(Integer.class);
                if (intruderStatus==1){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Intruder alert")
                            .setContentText("There has been an intruder")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);


                    notificationManager.notify(0, builder.build());
                }
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        intruderRef.addValueEventListener(postListener);


    }

    private void configureLockButton() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lockStatus = (int) dataSnapshot.getValue(Integer.class);
                Log.i(TAG, "onDataChange: lockstatus "+lockStatus);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        lockRef.addValueEventListener(postListener);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lockStatus==1){
                    lockRef.setValue(0);
                }else{

                    lockRef.setValue(1);
                }

            }
        });
    }

    private void configureCurtainsButton() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                curtainStatus = (int) dataSnapshot.getValue(Integer.class);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        curtainsRef.addValueEventListener(postListener);
        curtainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curtainStatus==1){
                    curtainsRef.setValue(0);
                }else{
                    curtainsRef.setValue(1);
                }

            }
        });
    }

    private void configureLEDButton() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ledStatus =  dataSnapshot.getValue(Integer.class);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        LEDRef.addValueEventListener(postListener);
        ledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ledStatus==1){
                    LEDRef.setValue(0);
                }else{
                    LEDRef.setValue(1);
                }

            }
        });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Smart Home";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Smart Home", name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
