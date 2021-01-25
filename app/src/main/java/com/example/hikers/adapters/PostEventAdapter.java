package com.example.hikers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.activities.CommentActivity;
import com.example.hikers.activities.FollowersActivity;
import com.example.hikers.fragment.EventDataFragment;
import com.example.hikers.fragment.ProfileFragment;
import com.example.hikers.helpers.Const;
import com.example.hikers.models.Event;
import com.example.hikers.models.User;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostEventAdapter extends RecyclerView.Adapter<PostEventAdapter.ViewHolder> {


    private Context mContext;

    private List<Event> eventList;

    private FirebaseUser firebaseUser;

    public PostEventAdapter(Context mContext) {
        this.mContext = mContext;
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile,like, comment, save, more;
        TextView userName, likes, publisher, eventDescription, comments, goingTxt, interestedTxt;
        RadioButton radioBtnGoing, radioBtnInterested;
        PhotoView postEventImage;
        RadioGroup radioGroup;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            userName = itemView.findViewById(R.id.username);
            more = itemView.findViewById(R.id.more);
            postEventImage = itemView.findViewById(R.id.post_event_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            likes = itemView.findViewById(R.id.likes);
            goingTxt = itemView.findViewById(R.id.txtGoingCount);
            interestedTxt = itemView.findViewById(R.id.txtInterestedCount);
            publisher = itemView.findViewById(R.id.publisher);
            eventDescription = itemView.findViewById(R.id.event_description);
            comments = itemView.findViewById(R.id.comments_event);
            radioBtnGoing = itemView.findViewById(R.id.radioBtnGoing);
            radioBtnInterested = itemView.findViewById(R.id.radioBtnInterested);
            radioGroup=itemView.findViewById(R.id.rGroup);

        }
    }


    Context newContext;
    @NonNull
    @Override

    public PostEventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        newContext=viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_event_item, viewGroup, false);
        return new PostEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Event event = eventList.get(i);

        Log.i(Const.LOG, event.toString());

        Glide.with(newContext)
                .load(event.getEventimage())
                .fitCenter()
                .into(viewHolder.postEventImage);


        if (event.getEventdescription().equals("")) {
            viewHolder.eventDescription.setVisibility(View.GONE);
        } else {

            viewHolder.eventDescription.setVisibility(View.VISIBLE);
            viewHolder.eventDescription.setText(event.getEventdescription());
        }

        publisherInfo(viewHolder.imageProfile, viewHolder.userName, viewHolder.publisher, event.getPublisher());
        isLiked(event.getEventid(), viewHolder.like);
        nrLike(viewHolder.likes, event.getEventid());
        getComments(event.getEventid(), viewHolder.comments);
        isSaved(event.getEventid(), viewHolder.save);

        viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {

                  String txt = checkedRadioButton.getText().toString();
                  if (txt.equals("Going")){
                      final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                     //FirebaseDatabase.getInstance().getReference().child("Likes").child(eventid);

                      FirebaseDatabase.getInstance().getReference().child("Status").child("Going").child(event.getEventid()).child(firebaseUser.getUid()).setValue(true);
                      FirebaseDatabase.getInstance().getReference().child("Status").child("Interested").child(event.getEventid()).child(firebaseUser.getUid()).removeValue();


                  }else {
                      final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                      FirebaseDatabase.getInstance().getReference().child("Status").child("Going").child(event.getEventid()).child(firebaseUser.getUid()).removeValue();
                      FirebaseDatabase.getInstance().getReference().child("Status").child("Interested").child(event.getEventid()).child(firebaseUser.getUid()).setValue(true);

                  }

                }
            }
        });

        viewHolder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = newContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", event.getPublisher());
                editor.apply();

                ((FragmentActivity) newContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();

            }
        });


        viewHolder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = newContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", event.getPublisher());
                editor.apply();

                ((FragmentActivity) newContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();

            }
        });


        viewHolder.publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = newContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", event.getPublisher());
                editor.apply();

                ((FragmentActivity) newContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();

            }
        });


//        viewHolder.postEventImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("eventid", event.getEventid());
//                editor.apply();
//
//                ((FragmentActivity) mContext)
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container, new EventDataFragment())
//                        .addToBackStack("").commit();
//
//            }
//        });


        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(event.getEventid()).setValue(true);


                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(event.getEventid()).removeValue();


                }
            }
        });


        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(newContext, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                editEvent(event.getEventid());
                                return true;
                            case R.id.delete:

                                final String id = event.getEventid();
                                FirebaseDatabase.getInstance().getReference("Event").child(event.getEventid())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            deleteNotifications(id, firebaseUser.getUid());
                                        }
                                    }
                                });
                                return true;
                            case R.id.report:
                                Toast.makeText(newContext, "Reported!", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.post_menue);
                if (!event.getPublisher().equals(firebaseUser.getUid())) {
                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                }
                popupMenu.show();
            }
        });


        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.like.getTag().equals("like")) {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(event.getEventid())
                            .child(firebaseUser.getUid()).setValue(true);

                    addNotification(event.getPublisher(), event.getEventid());

                } else {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(event.getEventid())
                            .child(firebaseUser.getUid()).removeValue();

                }
            }
        });


        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newContext, CommentActivity.class);
                intent.putExtra("eventid", event.getEventid());
                intent.putExtra("publisherid", event.getPublisher());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newContext.startActivity(intent);
            }
        });


        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newContext, CommentActivity.class);
                intent.putExtra("eventid", event.getEventid());
                intent.putExtra("publisherid", event.getPublisher());
                newContext.startActivity(intent);
            }
        });


        viewHolder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(newContext, FollowersActivity.class);
                intent.putExtra("id", event.getEventid());
                intent.putExtra("title", "likes");
                newContext.startActivity(intent);
            }
        });


    }


    private void getComments(String eventid, final TextView comments) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(eventid);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All " + dataSnapshot.getChildrenCount() + " comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //like dalada kiyala check kannawa
    private void isLiked(String eventid, final ImageView imageView) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(eventid);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (firebaseUser != null) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        imageView.setImageResource(R.drawable.ic_like_color);
                        imageView.setTag("liked");
                    } else {
                        imageView.setImageResource(R.drawable.ic_like);
                        imageView.setTag("like");
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addNotification(String userid, String eventid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "liked your event");
        hashMap.put("eventid", eventid);
        hashMap.put("event", true);

        reference.push().setValue(hashMap);
    }

    private void deleteNotifications(final String eventid, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    final Object firebaseEventId = snapshot.child("eventid").getValue();

                    if (firebaseEventId != null && firebaseEventId.equals(eventid)) {
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(newContext, "Deleted!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nrLike(final TextView likes, String eventid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(eventid);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                likes.setText(dataSnapshot.getChildrenCount() + " likes");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    private void Going(final TextView going, String postid) {
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Status").child(postid);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                going.setText(dataSnapshot.getChildrenCount() + " Going");
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, String userid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    Glide.with(newContext).load(user.getImageUrl()).into(image_profile);
                }
                if (user != null) {
                    username.setText(user.getUsername());
                }
                assert user != null;
                publisher.setText(user.getUsername());


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private void isSaved(final String eventid, final ImageView imageView) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = null;
        if (firebaseUser != null) {
            reference = FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid());
        }

        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(eventid).exists()) {

                        imageView.setImageResource(R.drawable.ic_save_color);
                        imageView.setTag("saved");

                    } else {
                        imageView.setImageResource(R.drawable.ic_save);
                        imageView.setTag("save");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void editEvent(final String eventid) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(newContext);
        alertDialog.setTitle("Edit Event");

        final EditText editText = new EditText(newContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(eventid, editText);

        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("eventdescription", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Event")
                                .child(eventid).updateChildren(hashMap);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    private void getText(String eventid, final EditText editText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event")
                .child(eventid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText.setText(Objects.requireNonNull(dataSnapshot.getValue(Event.class)).getEventdescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
