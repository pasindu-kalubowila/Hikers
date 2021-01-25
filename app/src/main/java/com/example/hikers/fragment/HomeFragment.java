package com.example.hikers.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikers.R;
import com.example.hikers.activities.HikingModeMainActivity;
import com.example.hikers.activities.MainActivity;
import com.example.hikers.activities.MainActivityChat;
import com.example.hikers.adapters.PostAdapter;
import com.example.hikers.adapters.PostEventAdapter;
import com.example.hikers.adapters.StoryAdapter;
import com.example.hikers.models.Event;
import com.example.hikers.models.Post;
import com.example.hikers.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private PostEventAdapter postEventAdapter;
    private List<Post> postLists;

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;


    private List<Event> postEventLists = new ArrayList<>();
    private ImageView imgViewActionHikingModes, hik;
    private ImageView post_event;
    private RecyclerView recyclerViewEvents;

    private List<String> followingList;
    private List<String> followingList2;
    ProgressBar progressBar;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private Context mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = MainActivity.mContext2;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_home);
        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();

        postAdapter = new PostAdapter(getActivity().getApplicationContext(), postLists);
        recyclerView.setAdapter(postAdapter);
        recyclerView.smoothScrollToPosition(0);

        postEventAdapter = new PostEventAdapter(getActivity().getApplicationContext());
        postEventAdapter.setEventList(postEventLists);

        //story
        recyclerView_story = view.findViewById(R.id.recyclerViewStory);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager1);
        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(getActivity().getApplicationContext(), storyList);
        recyclerView_story.setAdapter(storyAdapter);

//event set wena recyclerViewEvents eka
        recyclerViewEvents.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        recyclerViewEvents.smoothScrollToPosition(100);
        recyclerViewEvents.setLayoutManager(linearLayoutManager2);
        recyclerViewEvents.setAdapter(postEventAdapter);
        recyclerViewEvents.setVisibility(View.GONE);

        progressBar = view.findViewById(R.id.progress_circular);
        imgViewActionHikingModes = view.findViewById(R.id.imgViewActionHikingModes);
        post_event = view.findViewById(R.id.event_post);
        hik = view.findViewById(R.id.hik);

        checkFollowing();
        checkFollowingStory();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  show Hiking Modes
        imgViewActionHikingModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), HikingModeMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        hik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), MainActivityChat.class);
                    startActivity(intent);
                }
            }
        });

        //show Event post
        post_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    recyclerView.setVisibility(View.GONE);
                    recyclerView_story.setVisibility(View.GONE);
                    recyclerViewEvents.setVisibility(View.VISIBLE);

                    // startActivityForResult(new Intent(getActivity(), PostEventActivity.class), 1112);

                }
            }
        });

        FirebaseDatabase.getInstance().getReference("Event").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postEventLists.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    postEventLists.add(snapshot.getValue(Event.class));
                    postEventAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checkFollowingStory() {
        followingList2 = new ArrayList<>();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            final String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                    .child(myId).child("following");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    followingList2.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        followingList2.add(snapshot.getKey());

                    }

                    readStory();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }


    private void checkFollowing() {
        followingList = new ArrayList<>();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            final String myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                    .child(myId).child("following");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    followingList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        followingList.add(snapshot.getKey());
                    }
                    followingList.add(myId);

                    readPosts();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void readPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postLists.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post != null && post.getPublisher().equals(id)) {
                            postLists.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readStory() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long timecurrent = System.currentTimeMillis();
                if (storyList != null) {
                    storyList.clear();
                }
                if (storyList != null) {
                    storyList.add(new Story("", 0, 0, "", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                }
                for (String id : followingList2) {
                    int countStory = 0;
                    Story story = null;

                    for (DataSnapshot snapshot : dataSnapshot.child(id).getChildren()) {
                        story = snapshot.getValue(Story.class);

                        if (story != null && timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                            countStory++;
                        }
                    }

                    if (countStory > 0) {
                        storyList.add(story);
                    }

                }


                if (storyAdapter != null) {
                    storyAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
