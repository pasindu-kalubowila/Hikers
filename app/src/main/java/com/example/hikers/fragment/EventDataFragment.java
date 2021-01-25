package com.example.hikers.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hikers.R;
import com.example.hikers.adapters.PostEventAdapter;
import com.example.hikers.models.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventDataFragment extends Fragment {


    String eventid;
    private RecyclerView recyclerView;
    private PostEventAdapter post_event_adapter;
    private List<Event> eventList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_data, container, false);

        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        eventid = preferences.getString("eventid", "none");

        recyclerView = view.findViewById(R.id.recycler_view_event_data);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        eventList = new ArrayList<>();
        post_event_adapter = new PostEventAdapter(getActivity());
        post_event_adapter.setEventList(eventList);
        recyclerView.setAdapter(post_event_adapter);

        readEvent();

        return view;


    }

    private void readEvent() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Event").child(eventid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventList.clear();
                Event event = dataSnapshot.getValue(Event.class);
                eventList.add(event);

                post_event_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
