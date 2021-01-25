package com.example.hikers.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hikers.R;
import com.example.hikers.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAlarm extends BroadcastReceiver {
    private static final String CHANNEL_ID = "13";
    static int count = 0;


    @Override
    public void onReceive(final Context context, Intent intent) {
        //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
        //MediaPlayer mediaPlayer=MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        // mediaPlayer.start();

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent1 = new Intent(context, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);
        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent);

        Intent BATTERYintent = context.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        if (BATTERYintent != null) {
            int level = BATTERYintent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference productsRef = null;
        if (currentUser != null) {
            productsRef = FirebaseDatabase.getInstance().getReference().child("Request").child(currentUser.getUid()).child("Resive_Request");
        }

//        DatabaseReference productsRef1 = FirebaseDatabase.getInstance().getReference().child("Request").child("dlvxY10Sz4eYxmWjG9YCPbrhe9k2").child("Resive_Request");

        if (productsRef != null) {
            productsRef = productsRef.orderByChild("timestamp").getRef();
        }

        if (productsRef != null) {
            productsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (count != 0) {
                        System.out.println("not 0");
                     //   Toast.makeText(context, "Service working" + count, Toast.LENGTH_SHORT).show();
                        if (count < (int) dataSnapshot.getChildrenCount()) {

                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                DatabaseReference uData = FirebaseDatabase.getInstance().getReference().child("Users").child(d.getKey());

                                uData.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);

                                        if (dataSnapshot.exists()) {
                                            // Toast.makeText(context, "ok awa " + user.getFullName(), Toast.LENGTH_SHORT).show();

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                CharSequence name = context.getString(R.string.channel_name);
                                                String description = context.getString(R.string.channel_description);
                                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                                                channel.setDescription(description);
                                                // Register the channel with the system; you can't change the importance
                                                // or other notification behaviors after this
                                                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                                                notificationManager.createNotificationChannel(channel);
                                            }

                                            // methanin thama notification ekeedi button wala wenade liyanne
                                            Intent intent = new Intent(context, MapHicker.class);
                                            Intent intent2 = new Intent(context, ActionReceiver.class);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                                            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);

                                            NotificationCompat.Builder builder = null;
                                            if (user != null) {
                                                builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                                                        .setSmallIcon(R.drawable.ic_menu_manage)
                                                        .setContentTitle(user.getFullName() + " Sent a Tracking Request")
                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                                                        // Set the intent that will fire when the user taps the notification
                                                        .setContentIntent(pendingIntent)
                                                        .setAutoCancel(true).addAction(R.drawable.ic_menu_manage, context.getString(R.string.massage), pendingIntent)
                                                        .addAction(R.drawable.ic_menu_manage, context.getString(R.string.massage2), pendingIntent2);
                                            }


                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                                            // notificationId is a unique int for each notification that you must define
                                            int notificationId = 80;
                                            if (builder != null) {
                                                notificationManager.notify(notificationId, builder.build());
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                break;
                            }
                        }
                    }
                    count = (int) dataSnapshot.getChildrenCount();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if (productsRef != null) {
            productsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if (dataSnapshot.getValue() != null) {
                        // Toast.makeText(context, "add data done" + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                        System.out.println("not long" + dataSnapshot.getChildrenCount());
                    } else {
                        //  Toast.makeText(context, "No change", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    // Toast.makeText(context, "ok awa " + dataSnapshot.getKey().toString(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    // Toast.makeText(context, "remove you", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
