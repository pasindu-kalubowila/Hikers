package com.example.hikers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapterRequest extends RecyclerView.Adapter<UserAdapterRequest.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isfragment;

    private FirebaseUser firebaseUser;

    public UserAdapterRequest(Context mContext, List<User> mUsers, boolean isfragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isfragment = isfragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item_request, viewGroup, false);
        return new UserAdapterRequest.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(i);
        viewHolder.btn_request.setVisibility(View.VISIBLE);

        viewHolder.username.setText(user.getUsername());
        viewHolder.fullname.setText(user.getFullName());
        Glide.with(mContext).load(user.getImageUrl()).into(viewHolder.image_profile);

        is_request(user.getId(), viewHolder.btn_request);

        if (user.getId().equals(firebaseUser.getUid())) {
            viewHolder.btn_request.setVisibility(View.GONE);
        }


        viewHolder.btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Request");

                if (viewHolder.btn_request.getText().toString().equals("Request")) {

                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("time_stamp", System.currentTimeMillis());

                    databaseReference
                            .child(firebaseUser.getUid()).child("Sent_Request")
                            .child(user.getId()).setValue(map);

                    databaseReference
                            .child(user.getId()).child("Resive_Request")
                            .child(firebaseUser.getUid()).setValue(map);


                } else {
                    databaseReference
                            .child(firebaseUser.getUid()).child("Sent_Request")
                            .child(user.getId()).removeValue();

                    databaseReference
                            .child(user.getId()).child("Resive_Request")
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_request;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_Request);
            fullname = itemView.findViewById(R.id.fullname_Request);
            image_profile = itemView.findViewById(R.id.image_profile_Request);
            btn_request = itemView.findViewById(R.id.btn_request);
        }


    }

    private void is_request(final String userid, final Button button) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Request")

                .child(firebaseUser.getUid()).child("Sent_Request");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userid).exists()) {
                    button.setText("Requested");
                } else {
                    button.setText("Request");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
