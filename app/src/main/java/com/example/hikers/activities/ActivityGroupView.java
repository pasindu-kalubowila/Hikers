package com.example.hikers.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hikers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ActivityGroupView extends AppCompatActivity {
private ListView listView;
private Button ok,remove;
private Context context;
    private    String fullName="pasindu";

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

context=this;

    //Fullname eka
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    reference.child("fullname").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //   Toast.makeText(context, ""+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            fullName=dataSnapshot.getValue().toString();


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


  final  ArrayList<String> strings = new ArrayList<>();
strings.add("groups you in");
    // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
    // and the array that contains the data
 final    ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, strings);



    listView=findViewById(R.id.list);
    listView.setAdapter(stringArrayAdapter);

    ok=findViewById(R.id.ok);
    remove=findViewById(R.id.remove);

    remove.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


      //





        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("groups");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String groupName=dataSnapshot.getKey();

                reference1.child(dataSnapshot.getKey()).child("clients").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot.getKey())){

                            System.out.println(groupName);

                            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("groups");


                            reference1.child(groupName).removeValue();

                            strings.remove(groupName);
                            // next thing you have to do is check if your adapter has changed
                            stringArrayAdapter.notifyDataSetChanged();
                            Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();

                        };
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
});



    final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("groups");
    reference1.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
final String groupName=dataSnapshot.getKey();

            reference1.child(dataSnapshot.getKey()).child("clients").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(dataSnapshot.getKey())){

                        System.out.println(groupName);


                        // this line adds the data of your EditText and puts in your array
                        strings.add(groupName);
                        // next thing you have to do is check if your adapter has changed
                        stringArrayAdapter.notifyDataSetChanged();

                    };
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



    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            String s = listView.getItemAtPosition(i).toString();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                        {
                            Toast.makeText(context, "okkkkkk", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ActivityGroupView.this, MapHicker.class);
                            startActivity(intent);
                        }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                        {
                            Toast.makeText(context, "nooo", Toast.LENGTH_SHORT).show();

                        }

                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityGroupView.this);
            builder.setMessage("Are you sure want to join this group ").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
    });


//ok.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        Toast.makeText(ActivityGroupView.this, listView.getSelectedItem()+"", Toast.LENGTH_SHORT).show();
//    }
//});

    }
}
