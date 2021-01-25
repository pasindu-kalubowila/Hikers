package com.example.hikers.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hikers.R;
import com.example.hikers.activities.AddStoryActivity;
import com.example.hikers.activities.StoryActivity;
import com.example.hikers.models.Story;
import com.example.hikers.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Story> mStory;


    public StoryAdapter(Context mContext, List<Story> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
    }

Context newContext;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        newContext=viewGroup.getContext();
        if (i == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_story_item, viewGroup, false);
            return new StoryAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.story_item, viewGroup, false);
            return new StoryAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Story story = mStory.get(i);
        userInfo(viewHolder, story.getUserid(), i);

        if (viewHolder.getAdapterPosition() != 0) {
            seenStory(viewHolder, story.getUserid());
        }

        if (viewHolder.getAdapterPosition() == 0) {
            myStory(viewHolder.addstory_text, viewHolder.story_plus, false);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.getAdapterPosition() == 0) {

                    myStory(viewHolder.addstory_text, viewHolder.story_plus, true);
                } else {
                    //go to story
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra("userid", story.getUserid());
                    newContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView story_photo, story_plus, Story_photo_seen;
        TextView story_username, addstory_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            Story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_username = itemView.findViewById(R.id.username_story);
            addstory_text = itemView.findViewById(R.id.add_story_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void userInfo(final ViewHolder viewHolder, String userid, final int pos) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    Glide.with(mContext).load(user.getImageUrl()).into(viewHolder.story_photo);
                }
                if (pos != 0) {
                    if (user != null) {
                        Glide.with(mContext).load(user.getImageUrl()).into(viewHolder.Story_photo_seen);
                    }
                    if (user != null) {
                        viewHolder.story_username.setText(user.getUsername());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void myStory(final TextView textView, final ImageView imageView, final boolean click) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count = 0;
                long timecurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    if (story != null && timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                        count++;
                    }
                }

                if (click) {

                    if (count > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(newContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "View Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //go to story
                                Intent intent = new Intent(newContext, StoryActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                newContext.startActivity(intent);
                                dialog.dismiss();

                            }
                        });

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(newContext, AddStoryActivity.class);
                                newContext.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();

                    } else {
                        Intent intent = new Intent(newContext, AddStoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        newContext.startActivity(intent);
                    }

                } else {
                    if (count > 0) {

                        textView.setText("My story");
                        imageView.setVisibility(View.GONE);

                    } else {
                        textView.setText("Add Story");
                        imageView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenStory(final ViewHolder viewHolder, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .exists() && System.currentTimeMillis() < snapshot.getValue(Story.class).getTimeend()) {
                        i++;
                    }
                }

                if (i > 0) {
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.Story_photo_seen.setVisibility(View.GONE);

                } else {

                    viewHolder.story_photo.setVisibility(View.GONE);
                    viewHolder.Story_photo_seen.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
