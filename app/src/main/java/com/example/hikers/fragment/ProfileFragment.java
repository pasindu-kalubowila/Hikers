package com.example.hikers.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikers.R;
import com.example.hikers.activities.EditProfileActivity;
import com.example.hikers.activities.FollowersActivity;
import com.example.hikers.activities.OptionsActivity;
import com.example.hikers.adapters.EventPhotosAdapter;
import com.example.hikers.adapters.FavouritesAdapter;
import com.example.hikers.adapters.PhotosAdapter;
import com.example.hikers.models.Event;
import com.example.hikers.models.Favourite;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    private ImageView option_;
    private CircleImageView image_profile_pic;
    private TextView posts, followers, following, fullname, bio, username, event_count;
    private Button edit_profile_button;

    private List<String> mySaves;
    private RecyclerView recyclerView_save;
    private FavouritesAdapter photosAdapter_save;
    private List<Post> postList_save;

    private RecyclerView recyclerView_event_post;
    private EventPhotosAdapter event_photosAdapter;
    private List<Event> eventList;

    private RecyclerView recyclerView1;
    private PhotosAdapter photosAdapter;
    private List<Post> postList;

    private FirebaseUser firebaseUser;
    private String profileid;

    private ImageButton my_fotos, save_fotos, event;
    private List<Favourite> favouriteList = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        image_profile_pic = view.findViewById(R.id.image_profile_pic);
        option_ = view.findViewById(R.id.option_);
        posts = view.findViewById(R.id.posts_);
        event_count = view.findViewById(R.id.event_count);
        followers = view.findViewById(R.id.followers_);
        following = view.findViewById(R.id.following_);
        fullname = view.findViewById(R.id.fullname_profile);
        bio = view.findViewById(R.id.bio_);
        username = view.findViewById(R.id.username_profile_);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        my_fotos = view.findViewById(R.id.my_fotos_);
        event = view.findViewById(R.id.event_post);
        save_fotos = view.findViewById(R.id.save_fotos_);
        recyclerView1 = view.findViewById(R.id.recycler_view_my_fotos);
        recyclerView_save = view.findViewById(R.id.recycler_view_save);
        recyclerView_event_post = view.findViewById(R.id.recycler_view_event);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {

            SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", MODE_PRIVATE);
            profileid = prefs.getString("profileid", "none");

            recyclerView1.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
            recyclerView1.setLayoutManager(linearLayoutManager);
            postList = new ArrayList<>();
            photosAdapter = new PhotosAdapter(getActivity(), postList);
            recyclerView1.setAdapter(photosAdapter);

            recyclerView_event_post.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager_event = new GridLayoutManager(getContext(), 3);
            recyclerView_event_post.setLayoutManager(linearLayoutManager_event);
            eventList = new ArrayList<>();
            event_photosAdapter = new EventPhotosAdapter(getActivity(), eventList);
            recyclerView_event_post.setAdapter(event_photosAdapter);


            recyclerView_save.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager_saves = new GridLayoutManager(getContext(), 3);
            recyclerView_save.setLayoutManager(linearLayoutManager_saves);
            postList_save = new ArrayList<>();
            photosAdapter_save = new FavouritesAdapter(getActivity(), postList_save);
            recyclerView_save.setAdapter(photosAdapter_save);
            photosAdapter_save.setFavouriteList(favouriteList);

            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView_save.setVisibility(View.GONE);
            recyclerView_event_post.setVisibility(View.GONE);

            userInfo();
            getFollowers();
            getNrPosts();
            get_event_count();
            myPhotos();
            mysaves();
            events();

            if (profileid.equals(firebaseUser.getUid())) {

                edit_profile_button.setText(getString(R.string.edit_profile));

            } else {
                checkFollow();
                save_fotos.setVisibility(View.GONE);
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

        option_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionsActivity.class);
                startActivity(intent);
            }
        });

        my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView_save.setVisibility(View.GONE);
                recyclerView_event_post.setVisibility(View.GONE);
            }
        });


        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView1.setVisibility(View.GONE);
                recyclerView_save.setVisibility(View.GONE);
                recyclerView_event_post.setVisibility(View.VISIBLE);
            }
        });

        save_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerView1.setVisibility(View.GONE);
                recyclerView_save.setVisibility(View.VISIBLE);
                recyclerView_event_post.setVisibility(View.GONE);

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
                if (user != null && user.getImageUrl() != null) {
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

    private void myPhotos() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Post post = snapshot.getValue(Post.class);
                    if (post != null && post.getPublisher().equals(profileid)) {
                        postList.add(post);
                    }

                }
                Collections.reverse(postList);
                photosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void events() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Event event = snapshot.getValue(Event.class);
                    if (event != null && event.getPublisher().equals(profileid)) {
                        eventList.add(event);
                    }

                }

                Collections.reverse(eventList);
                event_photosAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void mysaves() {

        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySaves.clear();
                favouriteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mySaves.add(snapshot.getKey());
                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readSaves() {

        FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : mySaves) {
                        if (post != null && post.getPostid().equals(id)) {
                            favouriteList.add(new Favourite(true, post.getPostimage(), id));
                        }
                    }
                }
                photosAdapter_save.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);

                    for (String id : mySaves) {
                        if (event != null && event.getEventid().equals(id)) {
                            favouriteList.add(new Favourite(false, event.getEventimage(), id));
                        }
                    }
                }
                photosAdapter_save.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
