package com.example.hikers.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hikers.R;
import com.example.hikers.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class  HikingModeMainActivity extends AppCompatActivity {

    private CardView cardViewHost;
    private CardView cardViewMap;
    private CardView cardViewCompass;
    private CardView cardViewTorch;
    private CardView cardViewCalling;
    private CardView cardViewMessage;
    private CardView cardViewWeather;
    private CardView cardViewGroup;
    private CardView cardViewExit;
    private ToggleButton toggleButton;
private Context context;
 private    String fullName="pasindu";

    private void bindViews() {
        cardViewHost = findViewById(R.id.cardViewHost);
        cardViewMap = findViewById(R.id.cardViewMap);
        cardViewCompass = findViewById(R.id.cardViewCompass);
        cardViewTorch = findViewById(R.id.cardViewTorch);
        cardViewCalling = findViewById(R.id.cardViewCalling);
        cardViewMessage = findViewById(R.id.cardViewMessage);
        cardViewWeather = findViewById(R.id.cardViewWeather);
        cardViewExit = findViewById(R.id.cardViewExit);
        cardViewGroup = findViewById(R.id.Group);
        toggleButton = findViewById(R.id.on_off_btn);

    }


    public void FullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.INVISIBLE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_mode_main);
        bindViews();
        FullScreencall();
context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }

        cardViewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HikingModeMainActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });

        cardViewCalling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                startActivity(i);

            }
        });

        cardViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri SMS_URI = Uri.parse("smsto:");
                Intent sms = new Intent(Intent.ACTION_VIEW, SMS_URI);
                sms.putExtra("sms_body", "Please help me im *DOWN..!"); //Replace the message witha a vairable
                startActivity(sms);
            }
        });

        cardViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HikingModeMainActivity.this, ActivityGroupView.class);
                startActivity(intent);
            }
        });

        cardViewCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HikingModeMainActivity.this, Compass.class);
                startActivity(intent);
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean checked) {
                if (checked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null;
                        try {
                            cameraId = camManager.getCameraIdList()[0];
                            camManager.setTorchMode(cameraId, true);   //Turn ON
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null;
                        try {
                            cameraId = camManager.getCameraIdList()[0];
                            camManager.setTorchMode(cameraId, false); //Turn OF
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        cardViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null;
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false); //Turn OF
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                finish();
            }
        });
 final boolean [] isNext ={true};

        cardViewHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(HikingModeMainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                              AlertDialog.Builder alertDialog= new AlertDialog.Builder(context);

                LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflate = layoutInflater.inflate(R.layout.alertx, null);

             final EditText editText= inflate.findViewById(R.id.group);
              final Button button= inflate.findViewById(R.id.save);

if (isNext[0]){
    button.setText("Next");
}else{

    button.setText("Save group");
}

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (isNext[0]){
          Toast.makeText(context, ""+editText.getText(), Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(HikingModeMainActivity.this,Req.class);
          startActivity(intent);

          Toast.makeText(context,  FirebaseAuth.getInstance().getCurrentUser().getUid()+"ishaaaaaaaaaaaaan               " +FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
      }else{
          Toast.makeText(context, ""+editText.getText(), Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(HikingModeMainActivity.this,HikingModeMainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent);
          isNext[0]=true;

          FirebaseDatabase database = FirebaseDatabase.getInstance();
        final  DatabaseReference myRef = database.getReference("groups");

        //Fullname eka
          DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
          reference.child("fullname").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //   Toast.makeText(context, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                  fullName=dataSnapshot.getValue().toString();

                  myRef.child(editText.getText().toString()).
                          child("clients").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("0");
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });


          //Request eka
          DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Request").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
          reference1.child("Sent_Request").addChildEventListener(new ChildEventListener() {
              @Override
              public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                  System.out.println("ishan"+dataSnapshot.getKey());

                  DatabaseReference clients = myRef.child(editText.getText().toString()).
                          child("clients").getRef();

                  clients.child(dataSnapshot.getKey()).setValue("0");



              }

              @Override
              public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

              }

              @Override
              public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

              }

              @Override
              public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });





      }



        isNext[0]=false;
        button.setText("Save group");
    }
});

alertDialog.setView(inflate);

alertDialog.show();
            }
        });







        cardViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HikingModeMainActivity.this, MapHicker.class);
                startActivity(intent);

            }
        });



    }


}


