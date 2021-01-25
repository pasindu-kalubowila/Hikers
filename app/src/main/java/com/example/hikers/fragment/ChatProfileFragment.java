package com.example.hikers.fragment;
        import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import com.example.hikers.R;
        import com.example.hikers.activities.EditProfileActivity;
        import com.example.hikers.activities.FollowersActivity;
        import com.example.hikers.models.Event;
        import com.example.hikers.models.Post;
        import com.example.hikers.models.User;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.squareup.picasso.Picasso;
        import java.text.MessageFormat;
        import java.util.HashMap;
        import java.util.Objects;
        import de.hdodenhof.circleimageview.CircleImageView;
        import static android.content.Context.MODE_PRIVATE;


public class ChatProfileFragment extends Fragment {


    private CircleImageView image_profile_pic;
    private TextView posts, followers, following, fullname, bio, username, event_count;
    private Button edit_profile_button;
    private FirebaseUser firebaseUser;
    private String profileid;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        image_profile_pic = view.findViewById(R.id.image_profile_pic);
        posts = view.findViewById(R.id.posts_);
        event_count = view.findViewById(R.id.event_count);
        followers = view.findViewById(R.id.followers_);
        following = view.findViewById(R.id.following_);
        fullname = view.findViewById(R.id.fullname_profile);
        bio = view.findViewById(R.id.bio_);
        username = view.findViewById(R.id.username_profile_);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", MODE_PRIVATE);
            profileid = prefs.getString("profileid", "none");

            userInfo();
            getFollowers();
            getNrPosts();
            get_event_count();

            if (profileid.equals(firebaseUser.getUid())) {

                edit_profile_button.setText(getString(R.string.edit_profile));

            }else {
                checkFollow();
            }
        }

        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile_button.getText().toString();

                if (btn.equals("Edit Profile")) {

                    startActivity(new Intent(getContext(), EditProfileActivity.class));


                } else if (btn.equals("follow")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileid).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower").child(firebaseUser.getUid()).setValue(true);

                    addNotification();


                } else if (btn.equals("following")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileid).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower").child(firebaseUser.getUid()).removeValue();
                }


            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);
            }
        });


    }

    private void addNotification() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "start following yoy");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }


    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    fullname.setText(user.getFullName());
                }
                if (user != null) {
                    username.setText(user.getUsername());
                }
                if (user != null) {
                    bio.setText(user.getBio());
                }
                if (user != null) {
                    Picasso.get().load(user.getImageUrl()).into(image_profile_pic);
                }
                //Glide.with(getApplicationContext()).load(user.getImageUrl()).into(imag_profile_edit);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()) {
                    edit_profile_button.setText("following");

                } else {
                    edit_profile_button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid).child("follower");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                followers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference().child("Follow").child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                following.setText(String.valueOf(dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNrPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null && post.getPublisher().equals(profileid)) {
                        i++;
                    }
                }

                posts.setText(String.valueOf(i));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void get_event_count() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null && event.getPublisher().equals(profileid)) {
                        i++;
                    }
                }
                event_count.setText(MessageFormat.format("Organized {0} Events", String.valueOf(i)));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
