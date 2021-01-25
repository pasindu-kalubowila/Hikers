package com.example.hikers.activities;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;

import com.example.hikers.R;
import com.example.hikers.adapters.UserAdapter;
import com.example.hikers.models.User;

import java.util.List;

public class Req extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req);

        Fragment selectedFragment = new TrackSearchFriends();
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.req_container, selectedFragment).commit();
        }

    }
}
