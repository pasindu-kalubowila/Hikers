package com.example.hikers.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hikers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Animation frombottom, fromtop;
    EditText username, fullname, email, password,homeTown,country;
    Button register;
    TextView txt_login;
    ImageView imageView;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        imageView = findViewById(R.id.imagelogo_reg);
        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.login);
        homeTown =findViewById(R.id.HomeTown);
        country =findViewById(R.id.Country);


        imageView.startAnimation(fromtop);
        username.startAnimation(fromtop);
        fullname.startAnimation(fromtop);
        email.startAnimation(fromtop);
        country.startAnimation(frombottom);
        homeTown.startAnimation(frombottom);
        password.startAnimation(frombottom);
        register.startAnimation(frombottom);

        auth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_country = country.getText().toString();
                String str_hometown = homeTown.getText().toString();
                String str_password = password.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)|| TextUtils.isEmpty(str_country) || TextUtils.isEmpty(str_hometown)) {
                    Toast.makeText(RegisterActivity.this, "All fileds are required! ", Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Password must have 8 characters", Toast.LENGTH_SHORT).show();
                } else {

                    pd = new ProgressDialog(RegisterActivity.this);
                    pd.setMessage("Please Wait....");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);

                    register(str_username, str_fullname, str_email, str_password,str_country,str_hometown);

                }

            }
        });


    }

    private void register(final String username, final String fullname, String email, String password, final String country, final String hometown) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = null;
                    if (firebaseUser != null) {
                        userid = firebaseUser.getUid();
                    }

                    if (userid != null) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                    }

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username.toLowerCase());
                    hashMap.put("fullname", fullname);
                    hashMap.put("country", country);
                    hashMap.put("hometown", hometown);
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/hikers-63ec8.appspot.com/o/16480.png?alt=media&token=4c450157-7398-448f-a1c5-6e1a189ba738");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                 pd.dismiss();
//                                 Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                 startActivity(intent);
//                                 finish();
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            pd.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Email verification  was send", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });


                } else {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
